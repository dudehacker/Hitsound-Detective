package server.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  public void init();

  public void save(MultipartFile file,String folder);
  
  public void deleteFolder(String folder);
  
  public File getFolder(String folder);
}