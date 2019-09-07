package detective;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import detective.hitsound.HitsoundDetectiveThread;
import detective.mistake.Mistake;
import detective.mistake.MistakeType;
import osu.beatmap.Beatmap;
import osu.beatmap.general.Mode;
import server.BeatmapDownloader;
import server.model.Mod;
import server.model.ModResponse;

public class HitsoundDetective {

	private File folder;
	private File sourceFile;
	private File[] osuFiles;
	private ModResponse res;
	private Set<String> missingHitsounds;
	private Set<String> unusedHitsounds;
	private Set<String> wrongFormatHitSounds;

	public HitsoundDetective(File folder) {
		this.folder = folder;
		osuFiles = getOsuFiles(folder);
		Beatmap beatmap;
		try {
			beatmap = new Beatmap(getHitsoundDiff());
			res = new ModResponse(beatmap);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}

	}

	public ModResponse mod() {
		Set<String> usedHitsound = new HashSet<>();
		for (File file : osuFiles) {
			HitsoundDetectiveThread t = new HitsoundDetectiveThread(sourceFile, file);
			Mod mod = new Mod(t.getName());
			if (sourceFile.equals(file)){
				mod.setNoteCount(1);
			} else {
				mod.setNoteCount(t.getNoteCount());
			}
			t.run();
			mod.addMistake(t.getMistakes());
			res.addTab(mod);
			for (String s : t.getUsedHS()) {
				usedHitsound.add(s);
			}
		}

		Mod all = new Mod("All");
		all.setNoteCount(-2);
		wrongFormatHitSounds = new HashSet<>();
		File[] hitsounds = folder
				.listFiles((dir, name) -> name.toLowerCase().endsWith(".wav") || name.toLowerCase().endsWith(".ogg"));
		Set<String> physicalHS = new HashSet<>();
		Arrays.stream(hitsounds).forEach(hs -> {
			physicalHS.add(hs.getName());
//			if (!hs.getName().endsWith(".wav")) {
//				wrongFormatHitSounds.add(hs.getName());
//			}
		});
//		wrongFormatHitSounds.forEach(hs -> all.addMistake(new Mistake(MistakeType.WrongFormatHitsound, hs)));

		unusedHitsounds = new HashSet<>(physicalHS);
		unusedHitsounds.removeAll(usedHitsound);
		unusedHitsounds.forEach(hs -> all.addMistake(new Mistake(MistakeType.UnusedHitsound, hs)));

		missingHitsounds = new HashSet<>(usedHitsound);
		missingHitsounds.removeAll(physicalHS);
		missingHitsounds.forEach(hs -> all.addMistake(new Mistake(MistakeType.MissingHitsound, hs)));

		res.addTab(all);
		return res;
	}
	
	public static File getFolder(String setID){
		File downloadFolder =  new File(BeatmapDownloader.downloadPath);
		System.out.println(downloadFolder);
		if (!downloadFolder.exists()){
			System.out.println("created folder: " +downloadFolder.mkdirs());
		}
		System.out.println("folder exists: " +downloadFolder.exists());
		File[] files = downloadFolder.listFiles((dir,name)-> {
			File f = new File(dir,name);
			return f.isDirectory() && f.getName().startsWith(setID);
		});
		if (files.length > 0){
			return files[0];
		} else {
			return null;
		}
	}

	public static File[] getOsuFiles(File folder) {
		return folder.listFiles((dir, name) -> {
			File f = new File(dir,name);
			boolean isMania = false;
			if (name.toLowerCase().endsWith(".osu")) {
				try {
					Beatmap beatmap = new Beatmap(f);
					isMania = beatmap.getGeneralSection().getMode().equals(Mode.MANIA);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return (isMania);
		});
	}

	public File getHitsoundDiff() {
		Optional<File> optional = Arrays.stream(getOsuFiles(folder)).max((f1, f2) -> {
			Beatmap b1;
			Beatmap b2;
			try {
				b1 = new Beatmap(f1);
				b2 = new Beatmap(f2);
				int count1 = b1.getHitObjectSection().getHsCount();
				int count2 = b2.getHitObjectSection().getHsCount();
				if (count1 == count2) {
					double ratio1 = count1 * 1000.0 / b1.getHitObjectSection().getHitObjects().size();
					double ratio2 = count2 * 1000.0 / b2.getHitObjectSection().getHitObjects().size();
					return (int) (ratio1 - ratio2);
				} else {
					return count1 - count2;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;

		});
		if (optional.isPresent()) {
			sourceFile = optional.get();
			System.out.println("Hitsound diff is " + sourceFile);
		}
		return sourceFile;
	}

	public Set<String> getMissingHitsounds() {
		return missingHitsounds;
	}

	public Set<String> getUnusedHitsounds() {
		return unusedHitsounds;
	}

	public Set<String> getWrongFormatHitSounds() {
		return wrongFormatHitSounds;
	}

}
