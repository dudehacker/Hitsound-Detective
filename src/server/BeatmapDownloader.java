package server;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import detective.HitsoundDetective;
import server.model.ModResponse;
import server.model.exception.DeletedBeatmapException;
import server.model.exception.InvalidUrlException;
import server.model.osu.api.BeatmapInfoJSON;

public class BeatmapDownloader {

	private static WebDriver driver;
	public static final String downloadPath = System.getProperty("user.dir") + "\\maps";

	private static void initDriver() {
		if (driver == null) {
			driver = DriverUtilities.createDriver(false, true);
		}
	}

	static File getBeatmapFolder(String beatmapSet) {
		File downloadFolder = new File(downloadPath);
		for (File f : downloadFolder.listFiles()) {
			if (f.getName().startsWith(beatmapSet) && f.isDirectory()) {
				return f;
			}
		}
		return null;
	}

	static String waitForDownload(String beatmapSet) {
		File downloadFolder = new File(downloadPath);
		while (true) {
			for (File f : downloadFolder.listFiles()) {
				if (f.getName().startsWith(beatmapSet) && f.getName().endsWith(".osz")) {
					return f.toString();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static boolean checkUrl(String url) {
		// new website
		// https://osu.ppy.sh/beatmapsets/914328#mania/1940248
		Pattern newWebsite = Pattern.compile("^https://osu\\.ppy\\.sh/beatmapsets/\\d{5,10}(#mania(/\\d{5,10}/?)?)?$");

		// old website
		// https://osu.ppy.sh/b/1940248?m=3
		Pattern oldWebsite = Pattern.compile("^https://osu\\.ppy\\.sh/b/\\d{5,10}(\\?m=3)?$");

		return (newWebsite.matcher(url).matches() || oldWebsite.matcher(url).matches());

	}

	static void deleteBeatmapFolder(String beatmapSet) {
		File f = getBeatmapFolder(beatmapSet);
		if (f == null){
			return;
		}
		try {
			FileUtils.deleteDirectory(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static String downloadBeatmap(String beatmapSet) {
		System.out.println("downloading beatmap " + beatmapSet);
		initDriver();
		login();
		driver.navigate().to("https://osu.ppy.sh/beatmapsets/" + beatmapSet);
		driver.findElement(By.className("js-beatmapset-download-link")).click();
		String osz = waitForDownload(beatmapSet);
		driver.close();
		driver.quit();
		System.out.println("download finished: " +osz);
		return osz;
	}

	static void login() {
		driver.get("https://osu.ppy.sh");
		WebElement signInButton = driver.findElement(By.className("js-user-login--menu"));
		signInButton.click();
		WebElement usernameInput = driver.findElement(By.cssSelector("input[name='username']"));
		String username = System.getenv("osu_user");
		usernameInput.sendKeys(username);
		WebElement passwordInput = driver.findElement(By.cssSelector("input[name='password']"));
		passwordInput.sendKeys(System.getenv("osu_pw"));
		WebElement signInSubmit = driver.findElement(By.cssSelector("button.btn-osu-big--nav-popup"));
		signInSubmit.click();
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("page-mode__item")));
	}

	public static ModResponse modLocal(File folder) {
		HitsoundDetective hd = new HitsoundDetective(folder);
		return hd.mod();
	}
	
	public static ModResponse modUrl(String url) {
		if (!checkUrl(url)) {
			throw new InvalidUrlException(url);
		}

		// Get beatmap from local or download
		String beatmapSet;
		BeatmapInfoJSON beatmap = null;
		if (url.contains("/beatmapsets/")) {
			beatmapSet = url.split("/beatmapsets/")[1];
			if (beatmapSet.contains("#mania")){
				beatmapSet = beatmapSet.split("#mania")[0];
			}
			beatmap = OsuAPI.getBeatmapInfoFromSetID(beatmapSet).get(0);
		} else {
			String beatmapID = url.split("/b/")[1];
			if (beatmapID.contains("?=3")){
				beatmapID = beatmapID.split("?m=3")[0];
			}
			beatmap = OsuAPI.getBeatmapInfoFromBeatmapID(beatmapID).get(0);
			beatmapSet = beatmap.getBeatmapset_id();
		}
		
		if (beatmap == null){
			throw new DeletedBeatmapException(url);
		}
		
		Date updated = beatmap.getLast_update();
		File folder = HitsoundDetective.getFolder(beatmapSet);
		if (isLocalOutdated(beatmapSet, updated)) {
			deleteBeatmapFolder(beatmapSet);
			String osz = BeatmapDownloader.downloadBeatmap(beatmapSet);
			folder = BeatmapUnzip.unzip(osz);
		}

		// run hitsound detective
		HitsoundDetective hd = new HitsoundDetective(folder);
		return hd.mod();
	}

	static boolean isLocalOutdated(String setId, Date updated) {
		// get map update date
		System.out.println("Updated: " + updated);
		// get local last modified date
		File f = getBeatmapFolder(setId);
		if (f == null){
			return true;
		}
		Date lastModified = new Date(f.lastModified());
		System.out.println("Last Modified: " + lastModified);
		return lastModified.before(updated);
	}

}
