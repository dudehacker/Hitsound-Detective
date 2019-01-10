package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import osu.beatmap.Chord;
import osu.beatmap.event.Sample;
import osu.beatmap.hitobject.Addition;
import osu.beatmap.hitobject.HitObject;
import osu.beatmap.hitobject.HitsoundType;
import osu.beatmap.hitobject.SampleSet;
import osu.beatmap.timing.Timing;

public class BeatmapUtils {

	// Constants
	public final static String nl = System.getProperty("line.separator");
	// private static int SUPPORTED_OSU_FILE_VERSION=14;
	private static int SUPPORTED_PLAY_MODE = 3; // Osu!mania only
	public final static String defaultOsuPath = "C:/Program Files (x86)/osu!/Songs";
	public final static String startPath = System.getProperty("user.dir");
	
	public static String doubleToIntString(double doubleValue) {
		return Double.toString(doubleValue).split(Pattern.quote(".0"))[0];
	}

	public static Map<Long, Chord> convertToChordMapWithHitsound(List<HitObject> hitObjects, List<Sample> SB) {
		Map<Long, Chord> output = new TreeMap<Long, Chord>();
		for (Sample sample : SB) {
			Chord chord = null;
			if (!output.containsKey(sample.getStartTime())) {
				chord = new Chord();
				output.put(sample.getStartTime(), chord);
			} else {
				chord = output.get(sample.getStartTime());
			}
			chord.add(sample);
		}

		for (HitObject ho : hitObjects) {
			Chord chord = null;
			if (!output.containsKey(ho.getStartTime())) {
				chord = new Chord();
				output.put(ho.getStartTime(), chord);
			} else {
				chord = output.get(ho.getStartTime());
			}
			chord.add(ho);
		}
		return output;
	}

	public static void clearHitsounds(File f) {
		String outputText = "";
		boolean ho = false;
		if (f == null || !(f.exists())) {
			// error reading file
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
			String line;

			while ((line = br.readLine()) != null && ho == false) {
				boolean read = true;
				// read line by line
				if (line.contains("Sample,")) {
					// clear samples
					read = false;
				} else if (line.contains("HitObjects")) {
					ho = true;
				}

				if (read) {
					outputText += line + nl;
				}
			}
			ArrayList<HitObject> HOs = getListOfHitObjects(f, false);
			ArrayList<HitObject> newHOs = new ArrayList<HitObject>();
			for (HitObject hitObject : HOs) {
				HitObject newHO = hitObject.clone().clearHS();
				newHOs.add(newHO);

			}
			outputText += convertListToString(newHOs);
			exportBeatmap(f, outputText);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String convertListToString(List<?> al) {
		if (al == null) return "";
		String output = "";
		for (Object o : al) {
			output += o.toString() + nl;
		}
		return output;
	}

	public static List<Long> getDistinctStartTime(List<HitObject> hitObjects, List<HitObject> hitObjects2) {
		List<Long> output = new ArrayList<Long>();
		long t = -1;
		for (HitObject ho : hitObjects) {
			long startTime = ho.getStartTime();
			if (startTime != t) {
				t = startTime;
				output.add(t);
			}
		}
		t = -1;
		for (HitObject ho : hitObjects2) {
			long startTime = ho.getStartTime();
			if (startTime != t) {
				t = startTime;
				if (!output.contains(t)) {
					output.add(t);
				}

			}
		}
		return output;
	}

	public static ArrayList<Sample> getSamples(File f) throws Exception {
		ArrayList<Sample> output = new ArrayList<Sample>();
		if (f == null || !(f.exists())) {
			// error reading file
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// read line by line
				if (line.contains("Sample,")) {
					String[] parts = line.split(",");
					long startTime = Long.parseLong(parts[1]);
					String hs = parts[3];
					hs = hs.substring(1, hs.length() - 1);
					int vol = Integer.parseInt(parts[4]);
					Sample s = new Sample(startTime, hs, vol);
					output.add(s);
				}
			}
		}
		Collections.sort(output, Sample.StartTimeComparator);
		return output;
	}

	public static int getMode(File f) throws Exception {
		int mode = -1;
		System.out.println(f.getAbsolutePath());
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// read line by line
				if (line.contains("Mode: ")) {
					mode = Integer.parseInt(line.substring(6));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mode;
	}

	public static String[] getAllInfo(File f) throws Exception {
		String[] output = new String[4];
		String generalInfo = "";
		String sampleInfo = "";
		String timingInfo = "";
		String hitObjectsInfo = "";
		if (f == null || !(f.exists())) {
			// error reading file
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
			String line;
			int sectionID = 0;
			while ((line = br.readLine()) != null) {
				// read line by line
				switch (sectionID) {
				case 0:
					// General stuff

					if (line.contains("Storyboard Sound Samples")) {
						sectionID = 1;
						sampleInfo += line + nl;
					} else if (line.contains("Mode :")) {
						int mode = Integer.parseInt(line.substring(6));
						if (mode != SUPPORTED_PLAY_MODE) {
							String errMsg = "The currently supported mode is mania";
							JOptionPane.showMessageDialog(null, errMsg);
							System.exit(-1);
						}
					}
					generalInfo += line + nl;
					break;
				case 1:
					// Samples
					if (line.equals("[TimingPoints]")) {
						sectionID = 2;
						timingInfo += line + nl;
					} else {
						sampleInfo += line + nl;
					}
					break;

				case 2:
					// timing points
					if (line.contains("[HitObjects]")) {
						hitObjectsInfo += line + nl;
						sectionID = 3;
					}
					timingInfo += line + nl;
					break;

				case 3:
					// Hit Objects
					hitObjectsInfo += line + nl;
					break;
				}
			}
		}
		output[0] = generalInfo;
		output[1] = sampleInfo;
		output[2] = timingInfo;
		output[3] = hitObjectsInfo;
		return output;
	}

	public static File getOsuFile(String path) {
		File f = null;
		FileFilter filter = new FileNameExtensionFilter("OSU file", "osu");
		final JFileChooser jFileChooser1 = new JFileChooser(path);
		jFileChooser1.addChoosableFileFilter(filter);
		jFileChooser1.setFileFilter(filter);
		// Open details
		Action details = jFileChooser1.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		int returnVal = jFileChooser1.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = jFileChooser1.getSelectedFile();
		}
		return f;
	}

	public static ArrayList<Timing> getTimingPoints(File f) throws Exception {
		ArrayList<Timing> output = new ArrayList<Timing>();
		if (f == null || !(f.exists())) {
			// error reading file
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
			String line;
			int sectionID = 0;
			while ((line = br.readLine()) != null) {
				// read line by line
				switch (sectionID) {
				case 0:
					// General stuff

					if (line.equals("[TimingPoints]")) {
						sectionID = 1;
					}
					break;
				case 1:
					// timing points
					if (line.contains("[HitObjects]") || line.contains("[Colours]")) {
						sectionID = 2;
					} else if (!line.equals("")) {
						String[] parts = line.split(",");
						if (parts[0].contains(".")) {
							parts[0] = parts[0].substring(0, parts[0].indexOf('.'));
						}
						long offset = Long.parseLong(parts[0]);
						double mspb = Double.parseDouble(parts[1]);
						int meter = Integer.parseInt(parts[2]);
						SampleSet sampleSet = SampleSet.createSampleSet(Integer.parseInt(parts[3]));
						int setID = Integer.parseInt(parts[4]);
						int volume = Integer.parseInt(parts[5]);
						int isInherited = Integer.parseInt(parts[6]);
						int isKiai = Integer.parseInt(parts[7]);
						Timing timing = new Timing(offset, mspb, meter, sampleSet, setID, volume, isInherited, isKiai);
						output.add(timing);
					}

					break;

				}
			}
		}

		return output;
	}

	public static List<HitObject> getChordByTime(List<HitObject> hitObjects, long startTime) {
		List<HitObject> output = new ArrayList<HitObject>();
		for (HitObject ho : hitObjects) {
			if (ho.getStartTime() == startTime) {
				output.add(ho.clone());
			}
		}
		output = sortChordByHSType(output);
		return output;
	}

	public static List<HitObject> sortChordByHSType(List<HitObject> hitObjects) {
		List<HitObject> output = new ArrayList<HitObject>();
		List<HitObject> wavHS = new ArrayList<HitObject>();
		List<HitObject> defaultHS = new ArrayList<HitObject>();
		for (HitObject ho : hitObjects) {
			if (ho.hasWAV_HS()) {
				wavHS.add(ho.clone());
			} else {
				if (ho.hasDefault_HS()) {
					output.add(ho.clone());
				} else {
					defaultHS.add(ho);
				}

			}
		}
		for (HitObject wav : wavHS) {
			output.add(wav);
		}
		for (HitObject d : defaultHS) {
			output.add(d);
		}
		return output;
	}

	public static int getChordSizeForTime(ArrayList<HitObject> hitObjects, long startTime) {
		int size = 0;
		for (HitObject ho : hitObjects) {
			if (ho.getStartTime() == startTime) {
				size++;
			}
		}
		return size;
	}

	public static int getDefaultHSChordSizeForTime(List<HitObject> hitObjects, long startTime) {
		int size = 0;
		for (HitObject ho : hitObjects) {
			if (ho.getStartTime() == startTime && ho.hasDefault_HS()) {
				size++;
			}
		}
		return size;
	}

	public static void exportBeatmap(File file, String outputText) {
		BufferedWriter writer = null;

		try {
			// create a temporary file
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			writer.write(outputText);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.flush();
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public static ArrayList<HitObject> getListOfHitObjects(File f, boolean ignoreHitNormal) throws Exception {
		ArrayList<HitObject> output = new ArrayList<>();
		ArrayList<Timing> timingPoints = getTimingPoints(f);
		if (f == null || !(f.exists())) {
			// error reading file
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
			String line;
			int sectionID = 0;
			while ((line = br.readLine()) != null) {
				// read line by line
				switch (sectionID) {
				case 0:
					// General stuff

					if (line.contains("[HitObjects]")) {
						sectionID = 1;
					} else if (line.contains("Mode :")) {
						int mode = Integer.parseInt(line.substring(6));
						if (mode != SUPPORTED_PLAY_MODE) {
							String errMsg = "The currently supported mode is mania";
							JOptionPane.showMessageDialog(null, errMsg);
							System.exit(-1);
						}
					}

					break;
				case 1:
					// Hit OBject
					if (line != null && line.contains(",")) {
						String[] parts = line.split(Pattern.quote(","));
						int x = Integer.parseInt(parts[0]);
						long t = Long.parseLong(parts[2]);
						int type = Integer.parseInt(parts[3]);
						HitsoundType WFC = HitsoundType.createHitsoundType(Integer.parseInt(parts[4]));
						int volume;
						String wav;
						String part5;
						if (HitObject.isLN(type)) {
							int firstColonIndex = parts[5].indexOf(':');
							part5 = parts[5].substring(firstColonIndex + 1, parts[5].length());
							// change LN to short note
						} else {
							// short note
							part5 = parts[5];
						}
						volume = getVolumeFromFullHitSoundString(part5);
						wav = getWavNameFromFullHitSoundString(part5);
						SampleSet sampleSet = SampleSet.createSampleSet(Integer.parseInt(part5.substring(0, 1)));
						Addition addition = Addition.createAddition(Integer.parseInt(part5.substring(2, 3)));

						if (!wav.isEmpty()) {
							HitObject hitObject = new HitObject(x, t, wav, volume, WFC, 0, addition, sampleSet);
							hitObject.applyTimingPoint(timingPoints);
							output.add(hitObject);
						} else if (WFC != HitsoundType.HITNORMAL || !ignoreHitNormal) {

							for (HitsoundType hitsoundType : WFC.split()) {
								HitObject hitObject = new HitObject(x, t, wav, volume, hitsoundType, 0, addition,
										sampleSet);
								hitObject.applyTimingPoint(timingPoints);
								output.add(hitObject);
							}
						}
					}
					break;
				}
			}
		}
		return output;
	}

	private static int getVolumeFromFullHitSoundString(String hs) {
		int vol = 0;
		try {

			for (int i = 0; i < 3; i++) {
				int index = hs.indexOf(':');
				hs = hs.substring(index + 1, hs.length());
			}
			vol = Integer.parseInt(hs.substring(0, hs.indexOf(':')));
		} catch (Exception e) {
			System.out.println(hs + " caused an exception");
		}
		return vol;
	}

	private static String getWavNameFromFullHitSoundString(String hs) {
		String output = "";
		for (int i = 0; i < 3; i++) {
			int index = hs.indexOf(':');
			hs = hs.substring(index + 1, hs.length());
		}
		output = hs.substring(hs.indexOf(':') + 1, hs.length());
		return output;
	}

}
