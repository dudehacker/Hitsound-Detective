package osu.beatmap;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

import osu.beatmap.difficulty.DifficultySection;
import osu.beatmap.general.Mode;
import osu.beatmap.hitobject.SampleSet;
import osu.beatmap.metadata.MetadataSection;
import util.BeatmapUtils;

public abstract class Section {
	private String header;
	private Map<Key, Object> map;

	private static final String nl = BeatmapUtils.nl;
	protected static final char separator = ':';

	public Section(String header) {
		this.header = header;
		map = new LinkedHashMap<>();
	}

	protected void addProperty(Key key, Object property) {
		map.put(key, property);
	}

	public final String getHeader() {
		return header;
	}

	protected Object getProperty(Key key) {
		return map.get(key);
	}

	/**
	 * Default initialization, treats every value as a string
	 * 
	 * @param lines
	 *            that contain the key value pair
	 * @throws Exception
	 */
	public void init(String[] lines) throws ParseException {

		for (String line : lines) {
			try {
				for (Key key : map.keySet()) {
					if (line.contains(key.getName())) {
						String rawValue = getValue(line);
						if (rawValue.equals("") || key.getType().equals(String.class)) {
							map.put(key, rawValue);
						} else if (key.getType().equals(Integer.class)) {
							map.put(key, Integer.parseInt(rawValue));
						} else if (key.getType().equals(Double.class)) {
							map.put(key, Double.parseDouble(rawValue));
						} else if (key.getType().equals(SampleSet.class)) {
							map.put(key, SampleSet.createSampleSet(rawValue).toUpperString());
						} else if (key.getType().equals(Mode.class)) {
							map.put(key, Mode.createMode(rawValue));
						} else {
							throw new ParseException("Unsupported data type found: " + key.getType(), 0);
						}

					}
				}

			} catch (Exception e) {
				System.out.println(line);
				e.printStackTrace();
			}
		}
	}

	private String getValue(String line) {
		if (line.trim().charAt(line.length() - 1) != separator) {
			String s = line.substring(line.indexOf(separator) + 1, line.length());
			return s.trim();
		}
		return "";
	}

	@Override
	public String toString() {
		String output = "";
		String space = " ";
		if (this instanceof MetadataSection || this instanceof DifficultySection) {
			space = "";
		}
		output += header + nl;
		for (Key key : map.keySet()) {
			if (key.getType().equals(Double.class)) {
				output += key.getName() + separator + space + BeatmapUtils.doubleToIntString((Double) map.get(key))
						+ nl;
			} else {
				output += key.getName() + separator + space + map.get(key) + nl;
			}

		}
		return output;
	}

}
