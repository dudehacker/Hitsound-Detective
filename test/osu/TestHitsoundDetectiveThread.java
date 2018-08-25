package osu;

import org.junit.Test;

import main.HitsoundDetectiveThread;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;


public class TestHitsoundDetectiveThread {
	
	@Test
	public void Test_ConvertToChordMap() {
		ArrayList<HitObject> hitObjects = new ArrayList<>();
		HitObject ho1 = new HitObject(0, 0, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM);
		hitObjects.add(ho1);
		HitObject ho2 = new HitObject(0, 100, null, 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM);
		hitObjects.add(ho2);
		
		ArrayList<Sample> samples = new ArrayList<>();
		Sample s1 = new Sample(0,"kick.wav",100);
		samples.add(s1);
		Sample s2 = new Sample(50,"kick.wav",100);
		samples.add(s2);
		Sample s3 = new Sample(100,"kick.wav",100);
		samples.add(s3);
		
		Map<Long, Chord> chordMap = HitsoundDetectiveThread.convertToChordMapWithHitsound(hitObjects, samples);
		
		Chord expected1 = new Chord();
		expected1.add(ho1);
		expected1.add(s1);
		Chord actual1 = chordMap.get(expected1.getStartTime());
		assertEquals( expected1,  actual1);
		
		Chord expected2 = new Chord();
		expected2.add(s2);
		Chord actual2 = chordMap.get(expected2.getStartTime());
		assertEquals( expected2,  actual2);
		
		Chord expected3 = new Chord();
		expected3.add(s3);
		expected3.add(ho2);
		Chord actual3 = chordMap.get(expected3.getStartTime());
		assertEquals( expected3,  actual3);
		
	}
	
	@Test 
	public void Test_Equals() {
		File f1 = new File("zxc");
		File f2 = new File("qwe");
		HitsoundDetectiveThread t1 = new HitsoundDetectiveThread(f1,f2);
		HitsoundDetectiveThread t2 = new HitsoundDetectiveThread(f1,f2);
		
		assertEquals(t1,t2);
	}
	
	@Test 
	public void Test_hashCode() {
		File f1 = new File("zxc");
		File f2 = new File("qwe");
		File f3 = new File("qwwwe");
		HitsoundDetectiveThread t1 = new HitsoundDetectiveThread(f1,f3);
		HitsoundDetectiveThread t2 = new HitsoundDetectiveThread(f2,f3);
		
		assertNotEquals(t1.hashCode(),t2.hashCode());
	}
	
}
