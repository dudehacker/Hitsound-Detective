package osu.beatmap.editor;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

import osu.beatmap.Section;
import util.BeatmapUtils;

public final class EditorSection extends Section{
	
	private double distanceSpacing = 1.2;
	private int beatDivisor = 4;
	private int gridSize = 4;
	private double timelineZoom = 1;
	private String boomarks;
	private static final String nl = BeatmapUtils.nl;
	
	public EditorSection() {
		super("[Editor]");
	}
	
	public void init(String[] lines) {
		boomarks = lines[0];
		distanceSpacing = Double.parseDouble(lines[1].split(":")[1].trim());
		beatDivisor = Integer.parseInt(lines[2].split(":")[1].trim());
		gridSize = Integer.parseInt(lines[3].split(":")[1].trim());
		timelineZoom = Double.parseDouble(lines[4].split(":")[1].trim());
	}

	@Override
	public final String toString() {
		return  getHeader() + nl
				+ boomarks + nl
				+ "DistanceSpacing: " + BeatmapUtils.doubleToIntString(distanceSpacing) + nl
				+ "BeatDivisor: " + beatDivisor + nl
				+ "GridSize: " + gridSize + nl
				+ "TimelineZoom: " + BeatmapUtils.doubleToIntString(timelineZoom) + nl ;
	}
}
