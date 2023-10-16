package detective;

import detective.hitsound.HitsoundDetectiveThread;
import detective.mistake.Mistake;
import detective.mistake.MistakeType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import osu.beatmap.Beatmap;
import osu.beatmap.general.Mode;
import osu.beatmap.hitobject.GlobalSFX;
import server.BeatmapDownloader;
import server.model.Mod;
import server.model.ModResponse;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class HitsoundDetective {

    private final File folder;
    private final File[] osuFiles;
    private File sourceFile;
    private ModResponse res;
    @Getter
    private Set<String> missingHitsounds;
    @Getter
    private Set<String> unusedHitsounds;

    public HitsoundDetective(File folder) {
        this.folder = folder;
        osuFiles = getOsuFiles(folder);
        Beatmap beatmap;
        try {
            beatmap = new Beatmap(getHitsoundDiff());
            res = new ModResponse(beatmap);
        } catch (ParseException | IOException e) {
            log.error("failed to open HS diff!", e);
        }

    }

    public static File getFolder(String setID) {
        File downloadFolder = new File(BeatmapDownloader.downloadPath);
        System.out.println(downloadFolder);
        if (!downloadFolder.exists()) {
            System.out.println("created folder: " + downloadFolder.mkdirs());
        }
        System.out.println("folder exists: " + downloadFolder.exists());
        File[] files = downloadFolder.listFiles((dir, name) -> {
            File f = new File(dir, name);
            return f.isDirectory() && f.getName().startsWith(setID);
        });
        if (files.length > 0) {
            return files[0];
        } else {
            return null;
        }
    }

    public static File[] getOsuFiles(File folder) {
        return folder.listFiles((dir, name) -> {
            File f = new File(dir, name);
            boolean isMania = false;
            if (name.toLowerCase().endsWith(".osu")) {
                try {
                    Beatmap beatmap = new Beatmap(f);
                    isMania = beatmap.getGeneralSection().getMode().equals(Mode.MANIA);
                } catch (Exception e) {
                    log.error("failed to open osu files", e);
                }
            }
            return (isMania);
        });
    }

    public ModResponse mod() {
        Set<String> usedHitsound = new HashSet<>();
        for (File file : osuFiles) {
            HitsoundDetectiveThread t = new HitsoundDetectiveThread(sourceFile, file);
            Mod mod = new Mod(t.getName());
            mod.setNoteCount(t.getNoteCount());
            t.run();
            mod.addMistake(t.getMistakes());
            res.addTab(mod);
            usedHitsound.addAll(t.getUsedHitsounds());
        }

        Mod all = new Mod("All");
        all.setNoteCount(-2);
        File[] hitsounds = folder
                .listFiles((dir, name) ->
                        name.toLowerCase().endsWith(".wav")
                                || name.toLowerCase().endsWith(".ogg")
                                || name.toLowerCase().endsWith(".mp3"));
        Set<String> physicalHS = new HashSet<>();
        Arrays.stream(hitsounds).forEach(hs -> {
            physicalHS.add(hs.getName());
        });

        unusedHitsounds = new HashSet<>(physicalHS);
        unusedHitsounds.removeAll(usedHitsound);
        unusedHitsounds.forEach(hs -> {
            if (!GlobalSFX.isGlobalSFX(hs)) {
                all.addMistake(new Mistake(MistakeType.UnusedHitsound, hs));
            }
        });

        missingHitsounds = new HashSet<>(usedHitsound);
        missingHitsounds.removeAll(physicalHS);
        missingHitsounds.forEach(hs -> all.addMistake(new Mistake(MistakeType.MissingHitsound, hs)));

        res.addTab(all);
        return res;
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
                double ratio1 = count1 * 1.0 / b1.getHitObjectSection().getHitObjects().size();
                double ratio2 = count2 * 1.0 / b2.getHitObjectSection().getHitObjects().size();
                return ratio1 > ratio2 ? 1 : -1;

            } catch (Exception e) {
                log.error("failed to read HS diff", e);
            }
            return 0;

        });
        if (optional.isPresent()) {
            sourceFile = optional.get();
            log.info("Hitsound diff is {}", sourceFile);
        }
        return sourceFile;
    }

}
