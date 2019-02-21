package server;

import java.io.File;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.client.RestTemplate;

import detective.TimedMistake;
import detective.mistake.Mistake;
import detective.mistake.MistakeType;
import server.model.Mod;
import server.model.ModResponse;
import server.model.exception.DeletedBeatmapException;
import server.model.exception.InvalidUrlException;

public class BeatmapDownloader {

	private static WebDriver driver;
	public static final String downloadPath = System.getProperty("user.dir") + "\\maps";

	private static void initDriver() {
		if (driver == null) {
			driver = DriverUtilities.createDriver(false, true);
		}
	}
	
	static void waitForDownload(String beatmapSet){
		File downloadFolder = new File(downloadPath);
		boolean downloading = true;
		while(downloading) {
			for (File f : downloadFolder.listFiles()){
				if (f.getName().startsWith(beatmapSet) && f.getName().endsWith(".osz")){
					downloading = false;
					break;
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
		Pattern newWebsite = Pattern.compile("^https://osu\\.ppy\\.sh/beatmapsets/\\d{5,10}#mania/\\d{5,10}/?$");

		// old website
		// https://osu.ppy.sh/b/1940248?m=3
		Pattern oldWebsite = Pattern.compile("^https://osu\\.ppy\\.sh/b/\\d{5,10}\\?m=3$");

		return (newWebsite.matcher(url).matches() || oldWebsite.matcher(url).matches());

	}

	static void downloadBeatmap(String beatmapSet) {
		initDriver();
		login();
		driver.navigate().to("https://osu.ppy.sh/beatmapsets/" + beatmapSet);
		driver.findElement(By.className("js-beatmapset-download-link")).click();
		waitForDownload(beatmapSet);
		driver.close();
		driver.quit();
		System.out.println("download finished");
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

	static String getBeatmapInfo(String beatmapSet) {
		String api_key = System.getenv("api_key");
		System.out.println(api_key);
		String dl_url = "https://osu.ppy.sh/api/get_beatmaps?k=" + api_key + "&s=" + beatmapSet;

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(dl_url, String.class);
		System.out.println(result);
		return result;
	}

	public static ModResponse modMap(String url) {
		if (!checkUrl(url)) {
			throw new InvalidUrlException(url);
		} else if (url.contains("dead")) {
			throw new DeletedBeatmapException(url);
		}

		ModResponse res = new ModResponse(url);

		// download beatmap
		String beatmapSet = url.split("beatmapsets/")[1].split("#")[0];
		System.out.println("beatmap set: " + beatmapSet);

		// TODO run hitsound detective

		// sample response
		res.setArtist("Unknown Artist");
		res.setTitle("Unknown Title");
		res.setMapper("Unknown Mapper");
		Mod all = new Mod("All");
		all.addMistake(new Mistake(MistakeType.MissingImage));
		all.addMistake(new Mistake(MistakeType.BadResolutionImage));
		res.addTab(all);

		Mod ez = new Mod("EZ");
		ez.addMistake(new TimedMistake(230, MistakeType.MutedHO));
		ez.addMistake(new TimedMistake(450, MistakeType.Inconsistency));
		ez.addMistake(new TimedMistake(870, MistakeType.Inconsistency));
		ez.addMistake(new TimedMistake(1020, MistakeType.SBwhenNoNote));
		res.addTab(ez);

		Mod nm = new Mod("NM");
		nm.addMistake(new TimedMistake(100, MistakeType.UnusedGreenTiming));
		nm.addMistake(new TimedMistake(200, MistakeType.DuplicateHitsound));
		nm.addMistake(new TimedMistake(480, MistakeType.Inconsistency));
		res.addTab(nm);

		return res;
	}

}
