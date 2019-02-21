package detective;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import detective.hitsound.HitsoundDetectiveThread;
import osu.beatmap.Beatmap;
import osu.beatmap.general.Mode;

public class HitsoundDetective {

	private File folder;
	private File sourceFile;
	
	
	public HitsoundDetective(File folder){
		this.folder = folder;
	}
	
	public File[] getOsuFiles(){
		return folder.listFiles(
				(dir,name) -> name.toLowerCase().endsWith(".osu")
		);
	}
	
	public void getHitsoundDiff(){
		 Optional<File> optional = Arrays.stream(getOsuFiles()).max((f1,f2) -> {
			Beatmap b1, b2;
			try {
				b1 = new Beatmap(f1);
				b2 = new Beatmap(f2);
				return b1.getHitObjectSection().getHsCount() - b2.getHitObjectSection().getHsCount();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;

		});
		 if (optional.isPresent()){
			 sourceFile = optional.get();
		 } 
	}
	
}
