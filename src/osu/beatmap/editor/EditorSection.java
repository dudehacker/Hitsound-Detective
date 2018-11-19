package osu.beatmap.editor;

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
		for (String line : lines) {
			if (line.contains("DistanceSpacing")) {
				distanceSpacing = Double.parseDouble(line.split(":")[1].trim());
			}
			if (line.contains("BeatDivisor")) {
				beatDivisor = Integer.parseInt(line.split(":")[1].trim());
			}
			if (line.contains("GridSize")) {
				gridSize = Integer.parseInt(line.split(":")[1].trim());
			}
			if (line.contains("TimelineZoom")) {
				timelineZoom = Double.parseDouble(line.split(":")[1].trim());
			}
			if (line.contains("Bookmarks")) {
				boomarks = line;
			}
		}
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
