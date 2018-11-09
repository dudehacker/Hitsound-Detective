package osu.beatmap.difficulty;

import osu.beatmap.Section;
import util.BeatmapUtils;

public final class DifficultySection extends Section{

	private double HP = 8;
	private int keyCount = 7;
	private double OD = 8;
	private int approchRate = 5;
	private double sliderMultiplier = 1.4;
	private double sliderTickRate = 1;
	
	private static final String nl = BeatmapUtils.nl;
	
	public DifficultySection() {
		super("[Difficulty]");
	}
	
	public void init(String[] lines) {
		HP = Double.parseDouble(lines[0].split(":")[1].trim());
		keyCount = Integer.parseInt(lines[1].split(":")[1].trim());
		OD = Double.parseDouble(lines[2].split(":")[1].trim());
		approchRate = Integer.parseInt(lines[3].split(":")[1].trim());
		sliderMultiplier = Double.parseDouble(lines[4].split(":")[1].trim());
		sliderTickRate = Double.parseDouble(lines[5].split(":")[1].trim());
	}
	
	public final String toString() {
		return  getHeader() + nl
				+ "HPDrainRate:" + HP + nl
				+ "CircleSize:" + keyCount + nl
				+ "OverallDifficulty:" + OD + nl
				+ "ApproachRate:" + approchRate + nl
				+ "SliderMultiplier:" + BeatmapUtils.doubleToIntString(sliderMultiplier) + nl
				+ "SliderTickRate:" + BeatmapUtils.doubleToIntString(sliderTickRate) + nl;
	}
}
