package osu.beatmap.hitobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestGlobalSFX {
	
	@Test
	public void TestBadNames() {
		assertFalse(GlobalSFX.isGlobalSFX("countasd.wav"));
		assertFalse(GlobalSFX.isGlobalSFX("Count.wav"));
	}
	
	@Test
	public void TestCount() {
		assertTrue(GlobalSFX.isGlobalSFX("count.wav"));
	}
	
	@Test
	public void TestCount1s() {
		assertTrue(GlobalSFX.isGlobalSFX("count1s.wav"));
	}
	
	@Test
	public void TestCount2s() {
		assertTrue(GlobalSFX.isGlobalSFX("count2s.wav"));
	}
	
	@Test
	public void TestCount3s() {
		assertTrue(GlobalSFX.isGlobalSFX("count3s.wav"));
	}
	
	@Test
	public void TestGos() {
		assertTrue(GlobalSFX.isGlobalSFX("gos.wav"));
	}
	
	@Test
	public void TestReady() {
		assertTrue(GlobalSFX.isGlobalSFX("readys.wav"));
	}
	
	@Test
	public void TestFailsound() {
		assertTrue(GlobalSFX.isGlobalSFX("failsound.wav"));
	}
	
	@Test
	public void TestComboBurst() {
		assertTrue(GlobalSFX.isGlobalSFX("comboburst.wav"));
	}
	
	@Test
	public void TestSectionPass() {
		assertTrue(GlobalSFX.isGlobalSFX("sectionpass.wav"));
	}
	
	@Test
	public void TestSectionFail() {
		assertTrue(GlobalSFX.isGlobalSFX("sectionfail.wav"));
	}
	
	@Test
	public void TestSectionApplause() {
		assertTrue(GlobalSFX.isGlobalSFX("applause.wav"));
	}
	
	@Test
	public void TestSectionPauseLoop() {
		assertTrue(GlobalSFX.isGlobalSFX("pause-loop.wav"));
	}
	
	@Test
	public void TestAllFormat() {
		assertTrue(GlobalSFX.isGlobalSFX("count.mp3"));
		assertTrue(GlobalSFX.isGlobalSFX("count.wav"));
		assertTrue(GlobalSFX.isGlobalSFX("count.ogg"));
	}

}
