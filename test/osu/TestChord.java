package osu;

import org.junit.Test;
import static org.junit.Assert.*;


public class TestChord {
	@Test 
	public void Test_GetHashCode() {
		Chord chord1 = new Chord();
		chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM));
		chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITCLAP, 0, Addition.AUTO, SampleSet.NORMAL));
		
		Chord chord2= new Chord();
		chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH_CLAP, 0, Addition.AUTO, SampleSet.NORMAL));
		
		assertEquals(chord1.hashCode() , chord2.hashCode());
	}
	
	
	@Test
	public void Test_Equals() {
		Chord chord1 = new Chord();
		chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITCLAP, 0, Addition.AUTO, SampleSet.NORMAL));
		chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM));

		
		Chord chord2= new Chord();
		chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH_CLAP, 0, Addition.AUTO, SampleSet.NORMAL));
		
		assertTrue(chord1.equals(chord2));
	}
	
	@Test
	public void Test_Equals_With_SB() {
		Chord chord1 = new Chord();
		chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.AUTO, SampleSet.NORMAL));
		chord1.add(new HitObject(0, 0, "kick.wav", 80, HitsoundType.HITNORMAL, 0, Addition.AUTO, SampleSet.AUTO));
		
		
		Chord chord2= new Chord();
		chord2.add(new Sample(0,"kick.wav",100));
		chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.AUTO, SampleSet.NORMAL));

		
		assertTrue(chord1.equals(chord2));
	}
	
	@Test
	public void Test_Equals_False() {
		Chord chord1 = new Chord();
		chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM));
		
		Chord chord2= new Chord();
		chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH_CLAP, 0, Addition.AUTO, SampleSet.NORMAL));
		
		assertFalse(chord1.equals(chord2));
	}
	
	@Test
	public void Test_Equals_False2() {
		Chord chord1 = new Chord();
		chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM));
		chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITCLAP, 0, Addition.AUTO, SampleSet.NORMAL));
		chord1.add(new HitObject(0, 0, null, 0, HitsoundType.HITCLAP, 0, Addition.AUTO, SampleSet.DRUM));
		
		Chord chord2= new Chord();
		chord2.add(new HitObject(0, 0, null, 0, HitsoundType.HITFINISH_CLAP, 0, Addition.AUTO, SampleSet.NORMAL));
		
		assertFalse(chord1.equals(chord2));
	}
}
