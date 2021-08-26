package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import detective.HitsoundDetective;
import server.model.ModResponse;

@Service
public class LocalFileModServiceImpl implements ModService{
	
	@Autowired
	FilesStorageService storageService;

	@Override
	public ModResponse mod(String folder) {
		HitsoundDetective hd = new HitsoundDetective(storageService.getFolder(folder));
		return hd.mod();
	}

}
