package server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.model.ModResponse;


@RestController
public class Controller {
	
	@CrossOrigin
    @RequestMapping("/mod")
    public ModResponse greeting(@RequestParam(value="url") String url) {
		ModResponse res = BeatmapDownloader.modMap(url);
    	
    	return res;
    }
    
}