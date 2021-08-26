package server.model;

import static org.junit.Assert.assertEquals;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

public class TestMod {
	
	@Test
	public void TestModSort(){
		Mod normal = new Mod("Normal");
		normal.setNoteCount(300);
		Mod easy = new Mod("Easy");
		easy.setNoteCount(100);
		Mod hard = new Mod("Hard");
		hard.setNoteCount(500);
		SortedSet<Mod> tabs = new TreeSet<>();;
		tabs.add(normal);
		tabs.add(easy);
		tabs.add(hard);
		assertEquals(tabs.first().getNoteCount(), 100);
		assertEquals(tabs.last().getNoteCount(), 500);
	}
}
