package osu.beatmap.event;

import java.util.ArrayList;
import java.util.List;

import osu.beatmap.Section;
import util.BeatmapUtils;

public final class EventSection extends Section{
	private String storyboard = "";
	private List<Sample> samples;
	private String backgroundImage;
	private static final String nl = BeatmapUtils.nl;
	
	public List<Sample> getSamples() {
		return samples;
	}

	public EventSection() {
		super("[Events]");
		samples = new ArrayList<>();
	}
	
	public void init(String[] lines) {
		for (String line : lines) {
			if (line.contains("Sample,")) {
				String[] parts = line.split(",");
				long startTime = Long.parseLong(parts[1]);
				String hs = parts[3];
				hs = hs.substring(1, hs.length() - 1);
				int vol = Integer.parseInt(parts[4]);
				Sample s = new Sample(startTime, hs, vol);
				samples.add(s);
			} else {
				if (backgroundImage == null && isImageFormat(line) ) {
					backgroundImage = line.split(",")[2].replaceAll("\"", "");
				}
				storyboard += line + nl;
			}
		}
		
	}
	
	private boolean isImageFormat(String filename) {
		if (filename.contains(".jpg")) {
			return true;
		}
		if (filename.contains(".png")) {
			return true;
		}
		
		return false;
	}

	public final String toString() {
		return  getHeader() + nl
				+ storyboard
				+ BeatmapUtils.convertListToString(samples);
	}

	public String getBgImage() {
		return backgroundImage;
	}
}
