package osu.beatmap.difficulty;

import osu.beatmap.Key;
import osu.beatmap.Section;

public final class DifficultySection extends Section{
	
	private static final Key HP = new Key("HPDrainRate",Double.class);
	private static final Key keyCount = new Key("CircleSize",Integer.class);
	private static final Key OD = new Key("OverallDifficulty",Double.class);
	private static final Key approachRate = new Key("ApproachRate",Double.class);
	private static final Key sliderMultiplier = new Key("SliderMultiplier",Double.class);
	private static final Key sliderTickRate = new Key("SliderTickRate",Double.class);
	
	
	public DifficultySection() {
		super("[Difficulty]");
		addProperty(HP, 8.0);
		addProperty(keyCount, 7);
		addProperty(OD, 8.0);
		addProperty(approachRate, 5.0);
		addProperty(sliderMultiplier, 1.4);
		addProperty(sliderTickRate, 1.0);
	}
	
}
