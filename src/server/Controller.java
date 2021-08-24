package server;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import server.model.ModResponse;

@RestController
@CrossOrigin("http://localhost:3000") // react front end
public class Controller {

	@RequestMapping("/download")
	public ModResponse mod(@RequestParam(value = "url") String url) {
		return BeatmapDownloader.modUrl(url);
	}

	@CrossOrigin()
	@PostMapping("/upload")
	public ModResponse modLocal(@RequestParam("files") MultipartFile[] files, 
			@RequestParam("folder") String folder) {
//		String message = "";
//		try {
//			List<String> fileNames = new ArrayList<>();
//
//			Arrays.asList(files).stream().forEach(file -> {
//				storageService.save(file);
//				fileNames.add(file.getOriginalFilename());
//			});
//
//			message = "Uploaded the files successfully: " + fileNames;
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//		} catch (Exception e) {
//			message = "Fail to upload files!";
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//		}
		System.out.println(folder);
		Arrays.asList(files).stream().forEach(file -> {
			System.out.println(file);
		});
		return null;
	}

}