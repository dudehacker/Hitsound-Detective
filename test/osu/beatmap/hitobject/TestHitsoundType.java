package osu.beatmap.hitobject;
import org.junit.Test;

import osu.beatmap.hitobject.HitsoundType;

import static org.junit.Assert.*;

public class TestHitsoundType {
	
	@Test
	public void Test_Split_Whistle() {
		HitsoundType type = HitsoundType.HITWHISTLE;
		HitsoundType[] results = type.split();
		assertEquals(HitsoundType.HITWHISTLE, results[0]);
	}

	@Test
	public void Test_Split_Hit_Whistle_Finish_Clap() {
		HitsoundType type = HitsoundType.HITWHISTLE_FINISH_CLAP;
		HitsoundType[] results = type.split();
		assertEquals(HitsoundType.HITWHISTLE, results[0]);
		assertEquals(HitsoundType.HITFINISH, results[1]);
		assertEquals(HitsoundType.HITCLAP, results[2]);
	}
	
	
	@Test
	public void Test_Split_Hit_Whistle_Finish() {
		HitsoundType type = HitsoundType.HITWHISTLE_FINISH;
		HitsoundType[] results = type.split();
		assertEquals(HitsoundType.HITWHISTLE, results[0]);
		assertEquals(HitsoundType.HITFINISH, results[1]);
	}
	
	
	@Test
	public void Test_Split_Hit_Finish_Clap() {
		HitsoundType type = HitsoundType.HITFINISH_CLAP;
		HitsoundType[] results = type.split();
		assertEquals(HitsoundType.HITFINISH, results[0]);
		assertEquals(HitsoundType.HITCLAP, results[1]);
	}
}
