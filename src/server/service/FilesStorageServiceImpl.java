package server.service;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

	private final Path root = Paths.get("uploads");

	@Override
	public void init() {
		try {
			File uploadFolder = root.toFile();
			if (!uploadFolder.exists()) {
				uploadFolder.mkdir();
			}
		} catch (Exception e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}
	
	@Override
	public void save(MultipartFile file, String folder) {
		try {
			File directory = new File(Paths.get(this.root.toString(),folder).toString());
			if (!directory.exists()) {
				directory.mkdir();
			}
			Path target = Paths.get(this.root.toString(),file.getOriginalFilename());
			Files.copy(file.getInputStream(), target,StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public File getFolder(String folder) {
		return new File(Paths.get(this.root.toString(),folder).toString());
	}
	
	@Override
	public void deleteFolder(String folder) {
		FileSystemUtils.deleteRecursively(getFolder(folder));
		
	}

}