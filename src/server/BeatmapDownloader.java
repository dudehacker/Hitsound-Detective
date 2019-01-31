package server;

import detective.TimedMistake;
import detective.mistake.Mistake;
import detective.mistake.MistakeType;
import server.model.Mod;
import server.model.ModResponse;

public class BeatmapDownloader {
	
	public static ModResponse modMap(String url){
		
		if (url.contains("dead")){
			return null;
		}
		
		ModResponse res = new ModResponse(url);
		
		//TODO download beatmap
		
		//TODO run hitsound detective
		
		//sample response
		res.setArtist("Unknown Artist");
		res.setTitle("Unknown Title");
		res.setMapper("Unknown Mapper");
		Mod all = new Mod("All");
		all.addMistake(new Mistake(MistakeType.MissingImage));
		all.addMistake(new Mistake(MistakeType.BadResolutionImage));
		res.addTab(all);
		
		Mod ez = new Mod("EZ");
		ez.addMistake(new TimedMistake(230, MistakeType.MutedHO));
		ez.addMistake(new TimedMistake(450, MistakeType.Inconsistency));
		ez.addMistake(new TimedMistake(870, MistakeType.Inconsistency));
		ez.addMistake(new TimedMistake(1020, MistakeType.SBwhenNoNote));
		res.addTab(ez);
		
		Mod nm = new Mod("NM");
		nm.addMistake(new TimedMistake(100, MistakeType.UnusedGreenTiming));
		nm.addMistake(new TimedMistake(200, MistakeType.DuplicateHitsound));
		nm.addMistake(new TimedMistake(480, MistakeType.Inconsistency));
		res.addTab(nm);
		
		return res;
	}

}
