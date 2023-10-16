package osu.beatmap;

import osu.beatmap.event.Sample;
import osu.beatmap.hitobject.HitObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Chord {
    private ArrayList<HitObject> list_HO;
    private ArrayList<Sample> list_SB;

    private long startTime;

    public Chord() {
        list_HO = new ArrayList<HitObject>();
        list_SB = new ArrayList<Sample>();
        startTime = -1;
    }

    public long getStartTime() {
        if (list_HO.size() > 0)
            return list_HO.get(0).getStartTime();
        else if (list_SB.size() > 0)
            return list_SB.get(0).getStartTime();
        else
            return -1;
    }

    public boolean containsDuplicateHitsound() {
        Set<String> set = new HashSet<>();
        for (Sample sample : list_SB) {
            if (!set.add(sample.gethitSound())) {
                return true;
            }
        }
        for (HitObject hitObject : list_HO) {

            if (hitObject.hasHitsound()) {
                for (String hs : hitObject.toHitsoundString()) {
                    if (!set.add(hs)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * @return Set of unique hitsounds
     */
    public Set<String> getHitsounds() {
        Set<String> output = new HashSet<>();

        for (Sample sample : list_SB) {
            output.add(sample.gethitSound());
        }

        for (HitObject hitObject : list_HO) {
            if (hitObject.hasHitsound()) {
                output.addAll(hitObject.toHitsoundString());
            }
        }

        return output;
    }

    public boolean SbHasSoundWhenHoIsEmpty() {
        if (list_HO.isEmpty()) {
            if (!list_SB.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldMoveSbToHo() {
        int emptyHo = list_HO.stream().filter(x -> !x.hasHitsound()).collect(Collectors.toSet()).size();
        int sbCount = list_SB.size();
        if (emptyHo > 0 && sbCount > 0) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return getHitsounds().hashCode();
    }

    public boolean containsHitsounds(Chord chord) {
        if (chord == null || chord.getHitsounds().size() == 0) {
            if (getHitsounds().size() > 0) {
                return false;
            }
            return true;
        }
        return this.getHitsounds().containsAll(chord.getHitsounds());
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Chord) {
            return getHitsounds().equals(((Chord) other).getHitsounds());
        }

        return false;
    }

    public void addALLSB(ArrayList<Sample> SB) {
        for (Sample s : SB) {
            list_SB.add(s.clone());
        }
    }

    public void add(HitObject ho) {
        if (startTime != -1 && ho.getStartTime() != startTime) {
            throw new IllegalArgumentException(ho.toString());
        }
        list_HO.add(ho);
    }

    public void add(Sample s) {
        list_SB.add(s);
    }

    public HitObject getHitObjectByIndex(int i) {
        return list_HO.get(i);
    }

    public Chord Clone() {
        Chord newChord = new Chord();
        for (HitObject ho : list_HO) {
            newChord.add(ho.clone());
        }
        newChord.startTime = startTime;
        return newChord;
    }

    @Override
    public String toString() {
        String output = "Hit Objects\n";
        for (HitObject ho : list_HO) {
            try {
                output += ho.toSample().toString() + "\n";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        output += "\nSamples\n";
        for (Sample s : list_SB) {
            output += s.toString() + "\n";
        }

        return output;
    }

    public int getSize() {
        return getHitsounds().size();
    }

}
