package server;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestBeatmapDownloader {
	
	@Test
	public void test_download_beatmap(){
		String beatmapSet = "914328";
		BeatmapDownloader.downloadBeatmap(beatmapSet);
	}
	
	@Test
	public void test_beatmap_url_matcher(){
		String url = "https://osu.ppy.sh/beatmapsets/914328#mania/1940248";
		assertEquals(true,BeatmapDownloader.checkUrl(url));
		String url_bad = "https://osuwppyqsh/beatmapsets/";
		assertEquals(false, BeatmapDownloader.checkUrl(url_bad));
		String url_bad2 = "https://osu.ppy.sh/beatmapsets/914123328#mania/123";
		assertEquals(false, BeatmapDownloader.checkUrl(url_bad2));
		String url_old = "https://osu.ppy.sh/b/1940248?m=3";
		assertEquals(true, BeatmapDownloader.checkUrl(url_old));
	}
}
