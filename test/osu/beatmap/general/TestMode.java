package osu.beatmap.general;

import static org.junit.Assert.*;
import org.junit.Test;

import osu.beatmap.general.Mode;

public class TestMode {

	@Test
	public void TestModeSTD() {
		assertEquals("0",Mode.STD.toString());
	}
	
	@Test
	public void TestModeMania() {
		assertEquals("3",Mode.MANIA.toString());
	}
	
	@Test
	public void TestModeTaiko() {
		assertEquals("1",Mode.TAIKO.toString());
	}
	
	@Test
	public void TestModeCtb() {
		assertEquals("2",Mode.CTB.toString());
	}
}
