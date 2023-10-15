package detective;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import osu.beatmap.Beatmap;
import server.BeatmapDownloader;
import server.model.ModResponse;

public class TestHitsoundDetective {
	static File folder = new File(BeatmapDownloader.downloadPath + "\\914328 ClariS - SHIORI (TV Size)");

	@Test
	public void Test_Get_Folder(){
		String set = "914328";
		assertEquals(folder.getAbsolutePath(),HitsoundDetective.getFolder(set).getAbsolutePath());
	}
	
	@Test
	public void Test_Mod(){
		HitsoundDetective hd = new HitsoundDetective(folder);
		ModResponse res = hd.mod();
		System.out.println(res);
	}
	
	@Test
	public void Test_Get_Osu_Files() {
		File[] files = HitsoundDetective.getOsuFiles(folder);
		assertEquals(files.length, 5);
		Arrays.stream(files).forEach(file -> {
			try {
				int count = new Beatmap(file).getHitObjectSection().getHsCount();
				System.out.println(file);
				System.out.println("Hitsound count: "+count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void Test_Get_Hitsound_Diff() {
		HitsoundDetective hd = new HitsoundDetective(folder);
		assertNotNull(hd.getHitsoundDiff());
	}
}
