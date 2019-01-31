package osu.beatmap.hitobject;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestHitObject {
	
	@Test
	public void testIsLN(){
		assertEquals(true, HitObject.isLN(128));
		assertEquals(true, HitObject.isLN(132));
		assertEquals(false, HitObject.isLN(1));
		assertEquals(false, HitObject.isLN(5));
	}
}
