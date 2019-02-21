package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class BeatmapUnzip {
	
	    public static File unzip(String fileZip) {
	    	File zipFile = new File(fileZip);
	        File destDir = new File(zipFile.getParent() + "\\" + zipFile.getName().replace(".osz", ""));
	        destDir.mkdirs();
	        byte[] buffer = new byte[1024];
	        
			try (ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));){
				ZipEntry zipEntry = zis.getNextEntry();
		        while (zipEntry != null) {
		            File newFile = newFile(destDir, zipEntry);
		            FileOutputStream fos = new FileOutputStream(newFile);
		            int len;
		            while ((len = zis.read(buffer)) > 0) {
		                fos.write(buffer, 0, len);
		            }
		            fos.close();
		            zipEntry = zis.getNextEntry();
		        }
		        
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				Files.delete(zipFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return destDir;

	    }
	     
	    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
	        File destFile = new File(destinationDir, zipEntry.getName());
	         
	        String destDirPath = destinationDir.getCanonicalPath();
	        String destFilePath = destFile.getCanonicalPath();
	         
	        if (!destFilePath.startsWith(destDirPath + File.separator)) {
	            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
	        }
	         
	        return destFile;
	    }
}
