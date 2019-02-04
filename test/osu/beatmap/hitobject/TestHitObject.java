package osu.beatmap.hitobject;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestHitObject {
	
	@Test
	public void testIsLN(){
		assertEquals(true, HitObject.isLN(128));
		assertEquals(true, HitObject.isLN(132));
		assertEquals(false, HitObject.isLN(1));
		assertEquals(false, HitObject.isLN(5));
	}
	
	@Test 
	public void Test_Muted_With_HitNormal() {
		HitObject ho = new HitObject(0, 0, null, 0, HitsoundType.HITNORMAL, 0, Addition.NORMAL, SampleSet.DRUM);
		assertFalse(ho.isMuted());
	}
	
	@Test 
	public void Test_Muted_With_WAV_Muted() {
		HitObject ho = new HitObject(0, 0, "kick.wav", 0, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM);
		assertTrue(ho.isMuted());
	}
	
	@Test 
	public void Test_Muted_With_WAV_Unmuted() {
		HitObject ho = new HitObject(0, 0, "kick.wav", 50, HitsoundType.HITFINISH, 0, Addition.NORMAL, SampleSet.DRUM);
		assertFalse(ho.isMuted());
	}
}
