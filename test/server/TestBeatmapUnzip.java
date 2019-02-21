package server;

import org.junit.Test;

public class TestBeatmapUnzip {

	@Test
	public void test_unzip(){
		String beatmapZip = BeatmapDownloader.downloadPath + "\\914328 ClariS - SHIORI (TV Size).osz";
		System.out.println(beatmapZip);
		BeatmapUnzip.unzip(beatmapZip);
	}
}
