package detective;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import detective.hitsound.HitsoundDetectiveThread;
import util.BeatmapUtils;

public class Main {
	private final static String propertyName = "Hitsound Detective config.properties";
	private static String OsuPath = "C:\\Program Files (x86)\\osu!\\Songs";
	private static String programStartPath = System.getProperty("user.dir");
	private static List<HitsoundDetectiveThread> threads = new ArrayList<HitsoundDetectiveThread>();
	private static int finishedThreadCount = 0;
	private static File sourceFile;

	public static void main(String[] args) {
		readFromProperty(programStartPath);
		File osuDefaultPath = new File(OsuPath);
		if (!osuDefaultPath.exists()) {
			OsuPath = programStartPath;
		}
		sourceFile = BeatmapUtils.getOsuFile(OsuPath);
		OsuPath = sourceFile.getParentFile().getAbsolutePath();

		writeToProperty(programStartPath);

		FilenameFilter textFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".osu")) {
					return true;
				} else {
					return false;
				}
			}
		};
		File[] fileTargets = new File(OsuPath).listFiles(textFilter);

		for (File f : fileTargets) {
			HitsoundDetectiveThread hd = new HitsoundDetectiveThread(sourceFile, f);
			threads.add(hd);
			hd.start();
		}

	}

	public static void threadFinished() throws Exception {
		finishedThreadCount++;
		if (threads.size() == finishedThreadCount) {
			ResultsWindow frame = new ResultsWindow();
			frame.setVisible(true);

			Set<String> usedHitsound = new HashSet<>();
			// get every used hitsound
			Collections.sort(threads);
			for (HitsoundDetectiveThread hd : threads) {
				frame.addTabForSpecificDifficulty(hd.getName(), hd.getMistakes());
				for (String s : hd.getUsedHS()) {
					usedHitsound.add(s);
				}
			}

			// get actual Hitsound
			FilenameFilter wavFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					if (lowercaseName.endsWith(".wav")) {
						return true;
					} else {
						return false;
					}
				}
			};
			Set<String> physicalHS = new HashSet<>();
			File[] wavFiles = new File(OsuPath).listFiles(wavFilter);
			for (File wav : wavFiles) {
				physicalHS.add(wav.getName());
			}
			
			// Find un-used hitsound
			Set<String> unusedHitsounds = new HashSet<>(physicalHS);
			unusedHitsounds.removeAll(usedHitsound);
			frame.addTabForAllDifficulties("Unused hitsound",unusedHitsounds);
			
			// Find missing hitsound
			Set<String> missingHitsounds = new HashSet<>(usedHitsound);
			missingHitsounds.removeAll(physicalHS);
			frame.addTabForAllDifficulties("Missing hitsound",missingHitsounds);

		}
	}

	private static void readFromProperty(String path) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			String propertyPath = path + "\\" + propertyName;
			File f = new File(propertyPath);
			if (f.exists() && f.isFile()) {
				input = new FileInputStream(propertyPath);
				prop.load(input);
				if (prop.getProperty("Path") != null) {
					OsuPath = prop.getProperty("Path");
				}

				input.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void writeToProperty(String path) {
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			String propertyPath = path + "\\" + propertyName;
			File f = new File(propertyPath);
			if (!f.exists()) {
				f.createNewFile();
			}
			FileInputStream input = new FileInputStream(propertyPath);
			prop.load(input);
			// System.out.println(OsuPath);
			prop.setProperty("Path", OsuPath);
			input.close();
			// save properties to project root folder
			output = new FileOutputStream(propertyPath);
			prop.store(output, null);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
