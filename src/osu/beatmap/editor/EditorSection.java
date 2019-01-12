package osu.beatmap.editor;

import osu.beatmap.Key;
import osu.beatmap.Section;

public final class EditorSection extends Section{
	
	private static final Key distanceSpacing = new Key("DistanceSpacing",Double.class);
	private static final Key beatDivisor = new Key("BeatDivisor",Integer.class);
	private static final Key gridSize = new Key("GridSize",Integer.class);
	private static final Key timelineZoom = new Key("TimelineZoom",Double.class);
	private static final Key boomarks = new Key("Bookmarks",String.class);
	
	public EditorSection() {
		super("[Editor]");
		addProperty(boomarks, "");
		addProperty(distanceSpacing, 1.2);
		addProperty(beatDivisor, 4);
		addProperty(gridSize, 4);
		addProperty(timelineZoom, 1.0);
		
	}
	
}
