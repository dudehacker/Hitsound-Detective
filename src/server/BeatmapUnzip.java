package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class BeatmapUnzip {
	
	    public static void unzip(String fileZip) {
	        File destDir = new File(new File(fileZip).getParent() + "\\" + new File(fileZip).getName().replace(".osz", ""));
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
