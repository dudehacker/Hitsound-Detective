package osu.beatmap.hitobject;

import java.util.ArrayList;
import java.util.List;

import osu.beatmap.Section;
import osu.beatmap.timing.TimingSection;
import util.BeatmapUtils;

public final class HitObjectSection extends Section {

	private List<HitObject> hitObjects;

	public HitObjectSection() {
		super("[HitObjects]");
		hitObjects = new ArrayList<>();
	}

	public void init(String[] lines) {
		for (String line : lines) {
			try {
				HitObject hitObject = new HitObject(line);
				hitObjects.add(hitObject);
			} catch (Exception e) {
				System.err.println(line);
				e.printStackTrace();
			}
		}
	}

	public final String toString() {
		return BeatmapUtils.nl + getHeader() + BeatmapUtils.nl + BeatmapUtils.convertListToString(hitObjects);
	}

	public void addTimingHitsound(TimingSection timingSection) {
		for (HitObject hitObject : hitObjects) {
			hitObject.applyTimingPoint(timingSection.getTimingPoints());
		}
	}
}
