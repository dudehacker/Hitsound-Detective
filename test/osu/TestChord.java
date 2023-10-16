package osu;

import org.junit.Test;
import osu.beatmap.Chord;
import osu.beatmap.event.Sample;
import osu.beatmap.hitobject.Addition;
import osu.beatmap.hitobject.HitObject;
import osu.beatmap.hitobject.HitsoundType;
import osu.beatmap.hitobject.SampleSet;

import static org.junit.Assert.*;


public class TestChord {
    @Test
    public void Test_GetHashCode() {
        Chord chord1 = new Chord();
        chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM));
        chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITCLAP, 0, Addition.AUTO, SampleSet.NORMAL));

        Chord chord2 = new Chord();
        chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH_CLAP, 0, Addition.AUTO, SampleSet.NORMAL));

        assertEquals(chord1.hashCode(), chord2.hashCode());
    }


    @Test
    public void Test_Equals() {
        Chord chord1 = new Chord();
        chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITCLAP, 0, Addition.AUTO, SampleSet.NORMAL));
        chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM));


        Chord chord2 = new Chord();
        chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH_CLAP, 0, Addition.AUTO, SampleSet.NORMAL));

        assertEquals(chord1, chord2);
    }

    @Test
    public void Test_Equals_With_SB() {
        Chord chord1 = new Chord();
        chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.AUTO, SampleSet.NORMAL));
        chord1.add(new HitObject(0, 0, "kick.wav", 80, HitsoundType.HITNORMAL, 0, Addition.AUTO, SampleSet.AUTO));


        Chord chord2 = new Chord();
        chord2.add(new Sample(0, "kick.wav", 100));
        chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.AUTO, SampleSet.NORMAL));


        assertEquals(chord1, chord2);
    }

    @Test
    public void Test_Equals_False() {
        Chord chord1 = new Chord();
        chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM));

        Chord chord2 = new Chord();
        chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH_CLAP, 0, Addition.AUTO, SampleSet.NORMAL));

        assertNotEquals(chord1, chord2);
    }

    @Test
    public void Test_Equals_False2() {
        Chord chord1 = new Chord();
        chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM));
        chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITCLAP, 0, Addition.AUTO, SampleSet.NORMAL));
        chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITCLAP, 0, Addition.AUTO, SampleSet.DRUM));

        Chord chord2 = new Chord();
        chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH_CLAP, 0, Addition.AUTO, SampleSet.NORMAL));

        assertNotEquals(chord1, chord2);
    }

    @Test
    public void shouldMoveToSB() {
        // given
        Chord chord1 = new Chord();
        chord1.add(new HitObject(0, 0, null, 70, HitsoundType.HITFINISH, 0, Addition.AUTO, SampleSet.AUTO));
        chord1.add(new HitObject(111, 0, null, 70, HitsoundType.HITNORMAL, 0, Addition.AUTO, SampleSet.AUTO));
        chord1.add(new Sample(0, "kick.wav", 100));

        // when
        boolean result = chord1.shouldMoveSbToHo();

        // then
        assertTrue(result);
    }

    @Test
    public void SbHasSoundWhenHoIsEmpty() {
        // given
        Chord chord1 = new Chord();
        chord1.add(new Sample(0, "kick.wav", 100));

        // when
        boolean result = chord1.SbHasSoundWhenHoIsEmpty();

        // then
        assertTrue(result);
    }

    @Test
    public void shouldNotFlagMoveToSB() {
        // given
        Chord chord1 = new Chord();
        chord1.add(new HitObject(0, 0, null, 70, HitsoundType.HITFINISH, 0, Addition.AUTO, SampleSet.AUTO));
        chord1.add(new HitObject(111, 0, null, 70, HitsoundType.HITCLAP, 0, Addition.AUTO, SampleSet.AUTO));
        chord1.add(new HitObject(222, 0, null, 70, HitsoundType.HITWHISTLE_FINISH_CLAP, 0, Addition.AUTO, SampleSet.AUTO));
        chord1.add(new Sample(0, "kick.wav", 100));

        // when
        boolean result = chord1.shouldMoveSbToHo();

        // then
        assertFalse(result);
    }

    @Test
    public void shouldNotFlagMoveToSB2() {
        // given
        Chord chord1 = new Chord();
        chord1.add(new Sample(0, "kick.wav", 100));

        // when
        boolean result = chord1.shouldMoveSbToHo();

        // then
        assertFalse(result);
    }

    @Test
    public void shouldNotFlagMoveToSB3() {
        // given
        Chord chord1 = new Chord();

        // when
        boolean result = chord1.shouldMoveSbToHo();

        // then
        assertFalse(result);
    }
}
