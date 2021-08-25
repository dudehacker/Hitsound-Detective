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

@RestController
@CrossOrigin("http://localhost:3000") // react front end
public class Controller {

	@Autowired
	FilesStorageService storageService;

	@RequestMapping("/download")
	public ModResponse mod(@RequestParam(value = "url") String url) {
		return BeatmapDownloader.modUrl(url);
	}

	@CrossOrigin()
	@PostMapping("/upload")
	public ModResponse modLocal(@RequestParam("files") MultipartFile[] files, @RequestParam("folder") String folder) {
		// String message = "";
		System.out.println(folder);
		try {
			storageService.deleteFolder(folder);
			Arrays.asList(files).stream().forEach(file -> {
				storageService.save(file,folder);
			});

		} catch (Exception e) {
			System.err.println(e);
		}
		
		return BeatmapDownloader.modLocal(storageService.getFolder(folder));
	}

}