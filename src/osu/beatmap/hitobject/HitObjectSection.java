package osu.beatmap.hitobject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import osu.beatmap.Section;
import util.BeatmapUtils;

public final class HitObjectSection extends Section {

	private List<HitObject> hitObjects;

	public HitObjectSection() {
		super("[HitObjects]");
		hitObjects = new ArrayList<>();
	}

	public void init(String[] lines) {
		for (String line : lines) {
			if (line != null && line.contains(",")) {
				String[] parts = line.split(Pattern.quote(","));
				int x = Integer.parseInt(parts[0]);
				long t = Long.parseLong(parts[2]);
				int type = Integer.parseInt(parts[3]);
				HitsoundType WFC = HitsoundType.createHitsoundType(Integer.parseInt(parts[4]));
				int volume;
				String wav;
				String part5;
				if (type == 128) {
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
					hitObject.setType(type);
//					hitObject.applyTimingPoint(timingPoints);
					hitObjects.add(hitObject);
				} else {
					for (HitsoundType hitsoundType : WFC.split()) {
						HitObject hitObject = new HitObject(x, t, wav, volume, hitsoundType, 0, addition, sampleSet);
//						hitObject.applyTimingPoint(timingPoints);
						hitObjects.add(hitObject);
					}
				}
			}
		}
	}

	public final String toString() {
		return getHeader() + BeatmapUtils.nl + BeatmapUtils.convertListToString(hitObjects);
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
