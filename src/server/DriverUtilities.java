package server;

import java.io.File;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverUtilities {
	
	static String downloadFilepath = System.getProperty("user.dir") + "\\maps";
	static File downloadFolder = new File(downloadFilepath);

	public static WebDriver createDriver(boolean headless, boolean images) {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		if (headless) {
			options.addArguments("--headless");
		}
		HashMap<String, Object> prefs = new HashMap<>();
		prefs.put("profile.default_content_settings.popups", 0);
		prefs.put("download.default_directory", downloadFilepath);
		System.out.println(downloadFolder);
		if (!images) {
			prefs.put("profile.managed_default_content_settings.images", 2);
		}
		options.setExperimentalOption("prefs", prefs);
		return new ChromeDriver(options);
	}

}