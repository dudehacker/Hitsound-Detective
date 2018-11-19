package osu.beatmap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import util.BeatmapUtils;

public abstract class Section {
	private String header;
	protected Map<String, Property> properties;
	protected Set<String> keys;
	
	private static final String nl = BeatmapUtils.nl;
	
	public Section(String header) {
		this.header = header;
		properties = new TreeMap<>();
		keys = new HashSet<>();
	}
	
	public final String getHeader() {
		return header;
	}
	
	public void init(String[] lines) {
		for (String line: lines) {
			for (String key: keys) {
				if (line.contains(key)) {
					properties.put(key, new Property(line));
				}
			}
		}
	}
	
	@Override
	public String toString() {
		String output = "";
		output += header + nl;
		for (String key : keys) {
			output += properties.get(key);
		}
		return output;
	}
	
}
