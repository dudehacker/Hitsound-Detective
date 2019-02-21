package server;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.*;

import server.model.osu.api.BeatmapInfoJSON;

public class TestBeatmapDownloader {
	
	String beatmapSet = "914328";
	String beatmapID = "1940248";
	
	@Test
	public void testDownload(){
		String osz = BeatmapDownloader.downloadBeatmap(beatmapSet);
		BeatmapUnzip.unzip(osz);
	}
	
	@Ignore
	@Test
	public void test_delete_folder(){
		BeatmapDownloader.deleteBeatmapFolder(beatmapSet);
	}
	
	@Ignore
	@Test
	public void test_isOutdated(){
		BeatmapInfoJSON beatmap = OsuAPI.getBeatmapInfoFromBeatmapID(beatmapID).get(0);
		String beatmapSet = beatmap.getBeatmapset_id();
		Date updated = beatmap.getLast_update();
		boolean outdated = BeatmapDownloader.isLocalOutdated(beatmapSet,updated);
		System.out.println(outdated);
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
