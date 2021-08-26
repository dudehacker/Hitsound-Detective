package server;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import server.model.ModResponse;
import server.service.FilesStorageService;
import server.service.ModService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://hitsound-detective-frontend.herokuapp.com"}) // react front end
public class Controller {

	@Autowired
	FilesStorageService storageService;
	
	@Autowired
	ModService modService;

	@RequestMapping("/download")
	public ModResponse mod(@RequestParam(value = "url") String url) {
		return BeatmapDownloader.modUrl(url);
	}

	@PostMapping("/upload")
	public ModResponse modLocal(@RequestParam("files") MultipartFile[] files, @RequestParam("folder") String folder) {
		// String message = "";
		System.out.println(folder);
		try {
			Arrays.asList(files).stream().forEach(file -> {
				storageService.save(file,folder);
			});

		} catch (Exception e) {
			System.err.println(e);
		}
		ModResponse results =  modService.mod(folder);
		storageService.deleteFolder(folder);
		return results;
	}

}