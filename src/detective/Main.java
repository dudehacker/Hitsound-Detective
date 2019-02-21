package detective;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import detective.hitsound.HitsoundDetectiveThread;
import detective.image.ImageDetective;
import detective.mistake.Mistake;
import osu.beatmap.Beatmap;
import osu.beatmap.general.Mode;
import util.BeatmapUtils;

public class Main {
	private final static String propertyName = "Hitsound Detective config.properties";
	private static String OsuPath = "C:\\Program Files (x86)\\osu!\\Songs";
	private static String programStartPath = System.getProperty("user.dir");
	private static List<HitsoundDetectiveThread> threads = new ArrayList<HitsoundDetectiveThread>();
	private static int finishedThreadCount = 0;
	private static int maxThreadSize;
	private static File sourceFile;
	private static Set<String> imageMistakes = new HashSet<>();

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
		Set<String> images = new HashSet<>();
		for (File f : fileTargets) {
			Beatmap b;
			try {
				b = new Beatmap(f);
				if (b.getGeneralSection().getMode().equals(Mode.MANIA)) {
					images.add(b.getEventSection().getBgImage());
					HitsoundDetectiveThread hd = new HitsoundDetectiveThread(sourceFile, f);
					threads.add(hd);
					hd.start();
				}
			} catch (ParseException e) {
//				e.printStackTrace();
			} catch (IOException e) {
//				e.printStackTrace();
			} catch (NumberFormatException e) {
//				e.printStackTrace();
			}
			
		}
		System.out.println("after reading all beatmaps");
		maxThreadSize = threads.size();

		for (String image : images) {
			File f = new File(sourceFile.getParent()+"\\"+image);
			Mistake m = ImageDetective.check(f);
			if (m != null) {
				imageMistakes.add(m.getDescription());
			}
		}

	}

	public static synchronized void threadFinished() throws Exception {
		finishedThreadCount++;
		if (maxThreadSize == finishedThreadCount) {
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
			FilenameFilter hsFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String lowercaseName = name.toLowerCase();
					if (lowercaseName.endsWith(".wav") || lowercaseName.endsWith(".ogg")) {
						return true;
					} else {
						return false;
					}
				}
			};
			Set<String> physicalHS = new HashSet<>();
			File[] wavFiles = new File(OsuPath).listFiles(hsFilter);
			
			// Find wrong format hitsound
			Set<String> wrongFormatHitSounds = new HashSet<>();

			for (File wav : wavFiles) {
				physicalHS.add(wav.getName());
				if (!wav.getName().endsWith(".wav")) {
					wrongFormatHitSounds.add(wav.getName());
				}
			}
			
			frame.addTabForAllDifficulties("Wrong format hitsound",wrongFormatHitSounds);
			
			// Find un-used hitsound
			Set<String> unusedHitsounds = new HashSet<>(physicalHS);
			unusedHitsounds.removeAll(usedHitsound);
			frame.addTabForAllDifficulties("Unused hitsound",unusedHitsounds);
			
			// Find missing hitsound
			Set<String> missingHitsounds = new HashSet<>(usedHitsound);
			System.out.println("used\n" + usedHitsound.toString());
			System.out.println("exist\n"+physicalHS.toString());
			missingHitsounds.removeAll(physicalHS);
			frame.addTabForAllDifficulties("Missing hitsound",missingHitsounds);

//			frame.addTabForAllDifficulties("Bad Images", imageMistakes);
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
