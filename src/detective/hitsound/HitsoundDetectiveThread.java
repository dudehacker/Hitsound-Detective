package detective.hitsound;

import detective.mistake.MistakeType;
import detective.mistake.TimedMistake;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import osu.beatmap.Beatmap;
import osu.beatmap.Chord;
import osu.beatmap.event.Sample;
import osu.beatmap.general.GeneralSection;
import osu.beatmap.hitobject.HitObject;
import osu.beatmap.metadata.MetadataSection;
import osu.beatmap.timing.Timing;
import util.BeatmapUtils;

import java.io.File;
import java.util.*;

@Slf4j
@Data
public class HitsoundDetectiveThread implements Comparable<HitsoundDetectiveThread> {

    private Beatmap hitsoundDifficulty;
    private Beatmap targetDifficulty;

    private List<TimedMistake> mistakes = new ArrayList<>();
    private Set<String> usedHitsounds = new TreeSet<>();

    public HitsoundDetectiveThread(File source, File target) {
        try {
            hitsoundDifficulty = new Beatmap(source);
            targetDifficulty = new Beatmap(target);
        } catch (Exception e) {
            log.error("failed to start a thread", e);
        }
    }

    @Override
    public int hashCode() {
        return hitsoundDifficulty.hashCode() + targetDifficulty.hashCode();
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

    private void checkUnusedTimings() {
        List<Timing> timings = targetDifficulty.getTimingSection().getTimingPoints();
        for (int i = 0; i < timings.size(); i++) {
            if (i != 0) {
                Timing t = timings.get(i);
                Timing previousT = timings.get(i - 1);
                if (t.getOffset() == previousT.getOffset() && t.isInherited() == previousT.isInherited()) {
                    mistakes.add(new TimedMistake(t.getOffset(), MistakeType.SameOffsetTiming));
                } else if (t.isUnused(previousT)) {
                    mistakes.add(new TimedMistake(t.getOffset(), MistakeType.UnusedGreenTiming));
                }

            }
        }

    }

    public void run() {
        try {

            checkUnusedTimings();

            List<HitObject> sourceHO = hitsoundDifficulty.getHitObjectSection().getHitObjects();
            List<HitObject> targetHO = targetDifficulty.getHitObjectSection().getHitObjects();
            String audioFile = targetDifficulty.getGeneralSection().getProperty(GeneralSection.audioFileName).toString();
            usedHitsounds.add(audioFile);

            List<Sample> sourceSB = hitsoundDifficulty.getEventSection().getSamples();
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

            Map<Long, Chord> targetChords = BeatmapUtils.convertToChordMapWithHitsound(targetHO, targetSB);
            Map<Long, Chord> sourceChords = BeatmapUtils.convertToChordMapWithHitsound(sourceHO, sourceSB);

            for (Map.Entry<Long, Chord> entry : targetChords.entrySet()) {
                Chord chord = entry.getValue();
                if (chord.SbHasSoundWhenHoIsEmpty() || chord.shouldMoveSbToHo()) {
                    mistakes.add(new TimedMistake(chord.getStartTime(), MistakeType.SBwhenNoNote));
                }

                if (chord.containsDuplicateHitsound()) {
                    mistakes.add(new TimedMistake(chord.getStartTime(), MistakeType.DuplicateHitsound));
                }

                String sourceDiffName = (String) hitsoundDifficulty.getMetadataSection().getProperty(MetadataSection.versionKey);
                String targetDiffName = (String) targetDifficulty.getMetadataSection().getProperty(MetadataSection.versionKey);
                if (!sourceDiffName.equals(targetDiffName)) {

                    int i = -1;
                    Chord sourceChord = null;
                    while (i < 2 && sourceChord == null) {
                        sourceChord = sourceChords.get(chord.getStartTime() + i);
                        i++;
                    }

                    if (!chord.containsHitsounds(sourceChord)) {
                        mistakes.add(new TimedMistake(chord.getStartTime(), MistakeType.Inconsistency));
                    }
                }
            }

            System.out.println(getName() + " has mistake count = " + mistakes.size());
            Collections.sort(mistakes);
        } catch (Exception e) {
            log.error("failed to run check", e);
        }
    }

    public String getName() {
        return targetDifficulty.getMetadataSection().getProperty(MetadataSection.versionKey).toString();
    }

    @Override
    public int compareTo(HitsoundDetectiveThread other) {
        return this.targetDifficulty.getHitObjectSection().getHitObjects().size() - other.targetDifficulty.getHitObjectSection().getHitObjects().size();
    }

    public int getNoteCount() {
        return targetDifficulty.getHitObjectSection().getHitObjects().size();
    }

}
