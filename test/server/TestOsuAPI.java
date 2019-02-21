package server;

import java.util.List;

import org.junit.Test;

import server.model.osu.api.BeatmapInfoJSON;

public class TestOsuAPI {
	
	@Test
	public void testBeatmapInfoFromSetID(){
		String setId = "914328";
		List<BeatmapInfoJSON> beatmaps = OsuAPI.getBeatmapInfoFromSetID(setId);
		System.out.println(beatmaps.get(0).getLast_update());
	}
	
	@Test
	public void testBeatmapInfoFromBeatmapID(){
		String beatmapId = "1940248";
		List<BeatmapInfoJSON> beatmaps = OsuAPI.getBeatmapInfoFromBeatmapID(beatmapId);
		System.out.println(beatmaps.get(0).getLast_update());
	}
}
