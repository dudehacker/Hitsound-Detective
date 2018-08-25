package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import osu.Chord;
import osu.HitObject;
import osu.Sample;
import osu.Timing;
import utils.BeatmapUtils;

public class HitsoundDetectiveThread implements Runnable, Comparable<HitsoundDetectiveThread> {

	private File sourceDifficulty;
	private File targetDifficulty;
	private Thread t;
	private int difficultyLevel;

	private List<Mistake> mistakes = new ArrayList<>();
	private Set<String> usedHitsounds = new TreeSet<>();

	public HitsoundDetectiveThread(File source, File target) {
		sourceDifficulty = source;
		targetDifficulty = target;
	}

	@Override
	public int hashCode() {
		return sourceDifficulty.hashCode() + targetDifficulty.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof HitsoundDetectiveThread) {
			HitsoundDetectiveThread other = (HitsoundDetectiveThread) o;
			if (targetDifficulty.equals(other.targetDifficulty)) {
				return true;
			}
		}
		return false;
	}

	private void checkUnusedTimings() throws Exception {
		List<Timing> timings = BeatmapUtils.getTimingPoints(targetDifficulty);
		for (int i = 0; i < timings.size(); i++) {
			if (i != 0) {
				Timing t = timings.get(i);
				Timing previousT = timings.get(i - 1);
				if (t.getOffset() == previousT.getOffset()) {
					mistakes.add(new Mistake(t.getOffset(), MistakeType.SameOffsetTiming));
				} else if (t.isUnused(previousT)) {
					mistakes.add(new Mistake(t.getOffset(), MistakeType.UnusedGreenTiming));
				}

			}
		}

	}

	@Override
	public void run() {
		try {

			checkUnusedTimings();

			ArrayList<HitObject> sourceHO = BeatmapUtils.getListOfHitObjects(sourceDifficulty, false);
			ArrayList<HitObject> targetHO = BeatmapUtils.getListOfHitObjects(targetDifficulty, false);

			difficultyLevel = targetHO.size();

			ArrayList<Sample> sourceSB = BeatmapUtils.getSamples(sourceDifficulty);
			ArrayList<Sample> targetSB = BeatmapUtils.getSamples(targetDifficulty);

			// check for used hitsounds
			for (HitObject ho : targetHO) {
				// check for muted hitsound on note
				if (ho.isMuted()) {
					mistakes.add(new Mistake(ho.getStartTime(), MistakeType.MutedHO));
				} else {
					for (Sample s : ho.toSample()) {
						usedHitsounds.add(s.gethitSound());
					}
				}
			}

			for (Sample s : targetSB) {
				if (s.isMuted()) {
					mistakes.add(new Mistake(s.getStartTime(), MistakeType.MutedSB));
				} else {
					usedHitsounds.add(s.gethitSound());
				}
			}

			Map<Long, Chord> sourceChords = convertToChordMapWithHitsound(sourceHO, sourceSB);
			Map<Long, Chord> targetChords = convertToChordMapWithHitsound(targetHO, targetSB);
			for (Map.Entry<Long, Chord> entry : targetChords.entrySet()) {
				Chord chord = entry.getValue();
				if (chord.SbHasSoundWhenHoIsEmpty()) {
					mistakes.add(new Mistake(chord.getStartTime(), MistakeType.SBwhenNoNote));
				}

				if (!sourceDifficulty.equals(targetDifficulty)) {
					int i = -1;
					Chord sourceChord = null;
					while (i < 2 && sourceChord == null) {
						sourceChord = sourceChords.get(chord.getStartTime()+i);
						i++;
					}

					if (!chord.containsHitsounds(sourceChord)) {
						mistakes.add(new Mistake(chord.getStartTime(), MistakeType.Inconsistency));
					}
				}
			}

			System.out.println(t.getName() + " has mistake count = " + mistakes.size());
			Collections.sort(mistakes);
			Main.threadFinished();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<Long, Chord> convertToChordMapWithHitsound(ArrayList<HitObject> hitObjects,
			ArrayList<Sample> SB) {
		Map<Long, Chord> output = new TreeMap<Long, Chord>();
		for (Sample sample : SB) {
			Chord chord = null;
			if (!output.containsKey(sample.getStartTime())) {
				chord = new Chord();
				output.put(sample.getStartTime(), chord);
			} else {
				chord = output.get(sample.getStartTime());
			}
			chord.add(sample);
		}

		for (HitObject ho : hitObjects) {
			Chord chord = null;
			if (!output.containsKey(ho.getStartTime())) {
				chord = new Chord();
				output.put(ho.getStartTime(), chord);
			} else {
				chord = output.get(ho.getStartTime());
			}
			chord.add(ho);
		}
		return output;
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "Thread " + targetDifficulty.getName());
			t.setPriority(10);
			t.start();
		}
	}

	public List<Mistake> getMistakes() {
		return mistakes;
	}

	public Set<String> getUsedHS() {
		return usedHitsounds;
	}

	public String getName() {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(targetDifficulty), "UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// read line by line
				if (line.contains("Version:")) {
					return line.substring(line.indexOf(':') + 1, line.length());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int compareTo(HitsoundDetectiveThread other) {
		return this.difficultyLevel - other.difficultyLevel;
	}
}
