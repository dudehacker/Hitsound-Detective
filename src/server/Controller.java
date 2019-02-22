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
    public ModResponse mod(@RequestParam(value="url") String url) {
    	return BeatmapDownloader.modMap(url);
    }
    
}