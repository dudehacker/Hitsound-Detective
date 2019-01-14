package detective.hitsound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import detective.Main;
import detective.TimedMistake;
import detective.MistakeType;
import osu.beatmap.Beatmap;
import osu.beatmap.Chord;
import osu.beatmap.event.Sample;
import osu.beatmap.hitobject.HitObject;
import osu.beatmap.timing.Timing;
import util.BeatmapUtils;

public class HitsoundDetectiveThread implements Runnable, Comparable<HitsoundDetectiveThread> {

	private Beatmap sourceDifficulty;
	private Beatmap targetDifficulty;
	private File targetFile;
	private Thread t;

	private List<TimedMistake> mistakes = new ArrayList<>();
	private Set<String> usedHitsounds = new TreeSet<>();

	public HitsoundDetectiveThread(File source, File target) {
		targetFile = target;
		try {
			sourceDifficulty = new Beatmap(source);
			targetDifficulty = new Beatmap(target);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		List<Timing> timings = targetDifficulty.getTimingSection().getTimingPoints();
		for (int i = 0; i < timings.size(); i++) {
			if (i != 0) {
				Timing t = timings.get(i);
				Timing previousT = timings.get(i - 1);
				if (t.getOffset() == previousT.getOffset() && t.isInherited() == previousT.isInherited() ) {
					mistakes.add(new TimedMistake(t.getOffset(), MistakeType.SameOffsetTiming));
				} else if (t.isUnused(previousT)) {
					mistakes.add(new TimedMistake(t.getOffset(), MistakeType.UnusedGreenTiming));
				}

			}
		}

	}

	@Override
	public void run() {
		try {

			checkUnusedTimings();
			
			List<HitObject> sourceHO = sourceDifficulty.getHitObjectSection().getHitObjects();
			List<HitObject> targetHO = targetDifficulty.getHitObjectSection().getHitObjects();


			List<Sample> sourceSB = sourceDifficulty.getEventSection().getSamples();
			List<Sample> targetSB = targetDifficulty.getEventSection().getSamples();

			// check for used hitsounds
			for (HitObject ho : targetHO) {
				// check for muted hitsound on note
				if (ho.isMuted()) {
					mistakes.add(new TimedMistake(ho.getStartTime(), MistakeType.MutedHO));
				} else {
					for (Sample s : ho.toSample()) {
						usedHitsounds.add(s.gethitSound());
					}
				}
			}

			for (Sample s : targetSB) {
				if (s.isMuted()) {
					mistakes.add(new TimedMistake(s.getStartTime(), MistakeType.MutedSB));
				} else {
					usedHitsounds.add(s.gethitSound());
				}
			}

			Map<Long, Chord> sourceChords = BeatmapUtils.convertToChordMapWithHitsound(sourceHO, sourceSB);
			Map<Long, Chord> targetChords = BeatmapUtils.convertToChordMapWithHitsound(targetHO, targetSB);
			for (Map.Entry<Long, Chord> entry : targetChords.entrySet()) {
				Chord chord = entry.getValue();
				if (chord.SbHasSoundWhenHoIsEmpty()) {
					mistakes.add(new TimedMistake(chord.getStartTime(), MistakeType.SBwhenNoNote));
				}

				if (!sourceDifficulty.equals(targetDifficulty)) {
					int i = -1;
					Chord sourceChord = null;
					while (i < 2 && sourceChord == null) {
						sourceChord = sourceChords.get(chord.getStartTime()+i);
						i++;
					}

					if (!chord.containsHitsounds(sourceChord)) {
						mistakes.add(new TimedMistake(chord.getStartTime(), MistakeType.Inconsistency));
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
	


	public void start() {
		if (t == null) {
			t = new Thread(this, "Thread " + targetFile.getName());
			t.setPriority(10);
			t.start();
		}
	}

	public List<TimedMistake> getMistakes() {
		return mistakes;
	}

	public Set<String> getUsedHS() {
		return usedHitsounds;
	}

	public String getName() {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(targetFile), "UTF-8"))) {
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
		return this.targetDifficulty.getHitObjectSection().getHitObjects().size() - other.targetDifficulty.getHitObjectSection().getHitObjects().size();
	}
}
