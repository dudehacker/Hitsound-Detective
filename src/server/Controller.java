package server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.model.ModResponse;
import server.model.exception.DeletedBeatmapException;
import server.model.exception.InvalidUrlException;


@RestController
public class Controller {
	
	@CrossOrigin
    @RequestMapping("/mod")
    public ModResponse greeting(@RequestParam(value="url") String url) {
    	ModResponse res = BeatmapDownloader.modMap(url);
    	if (!url.contains("https://osu")){  //TODO use regex
    		throw new InvalidUrlException(url);
    	} else if (res == null){
    		throw new DeletedBeatmapException(url);
    	}
    	return res;
    }
    
}