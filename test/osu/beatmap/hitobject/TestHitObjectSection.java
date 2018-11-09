package osu.beatmap.hitobject;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.junit.Test;

import osu.beatmap.Beatmap;

public class TestHitObjectSection {
	
	@Test
	public void TestReadBeatmapFromFile() {
		File f = new File("testBeatmap.osu");
		String text = "";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
			String line;
			// read line by line
			while ((line = br.readLine()) != null) {
				text += line + System.lineSeparator();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String split1 = text.split(Pattern.quote("[HitObjects]"))[1];
		
		Beatmap b = new Beatmap(f);
		assertEquals("[HitObjects]" + split1, b.getHitObjectSection().toString() + System.lineSeparator());
	}
}
