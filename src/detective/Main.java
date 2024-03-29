package detective;

import detective.hitsound.HitsoundDetectiveThread;
import detective.image.ImageDetective;
import detective.mistake.Mistake;
import lombok.extern.slf4j.Slf4j;
import osu.beatmap.Beatmap;
import osu.beatmap.hitobject.GlobalSFX;
import util.BeatmapUtils;

import java.io.*;
import java.text.ParseException;
import java.util.*;

@Slf4j
public class Main {
    private static final String propertyName = "Hitsound Detective config.properties";
    private static final Set<String> imageMistakes = new HashSet<>();
    private static final String programStartPath = System.getProperty("user.dir");
    private static final List<HitsoundDetectiveThread> list = new ArrayList<>();
    private static String OsuPath = "C:\\Program Files (x86)\\osu!\\Songs";
    private static boolean textOutput = true;

    public static void main(String[] args) {
        readFromProperty(programStartPath);
        File osuDefaultPath = new File(OsuPath);
        if (!osuDefaultPath.exists()) {
            OsuPath = programStartPath;
        }
        File sourceFile = BeatmapUtils.getOsuFile(OsuPath);
        OsuPath = sourceFile.getParentFile().getAbsolutePath();

        writeToProperty(programStartPath);

        File[] fileTargets = HitsoundDetective.getOsuFiles(new File(OsuPath));
        Set<String> images = new HashSet<>();
        for (File f : fileTargets) {
            Beatmap b;
            try {
                b = new Beatmap(f);
                images.add(b.getEventSection().getBgImage());
                HitsoundDetectiveThread hd = new HitsoundDetectiveThread(sourceFile, f);
                list.add(hd);
                hd.run();
            } catch (ParseException | IOException e) {
                log.error("error open beatmap", e);
            }
        }
        log.info("after reading all beatmaps");
        processResult();
        for (String image : images) {
            File f = new File(sourceFile.getParent() + "\\" + image);
            Mistake m = ImageDetective.check(f);
            if (m != null) {
                imageMistakes.add(m.getDescription());
            }
        }

    }

    public static void processResult() {
        ResultsWindow frame = new ResultsWindow(OsuPath);
        frame.setVisible(true);

        Set<String> usedHitsound = new HashSet<>();
        // get every used hitsound
        Collections.sort(list);
        for (HitsoundDetectiveThread hd : list) {
            frame.addTabForSpecificDifficulty(hd.getName(), hd.getMistakes());
            usedHitsound.addAll(hd.getUsedHitsounds());
        }

        // get actual Hitsound
        FilenameFilter hsFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return lowercaseName.endsWith(".wav") || lowercaseName.endsWith(".ogg");
        };
        Set<String> physicalHS = new HashSet<>();
        File[] wavFiles = new File(OsuPath).listFiles(hsFilter);

        if (wavFiles != null) {
            for (File wav : wavFiles) {
                physicalHS.add(wav.getName());
            }
        }

        // Find un-used hitsound
        TreeSet<String> unusedHitsounds = new TreeSet<>(physicalHS);
        unusedHitsounds.removeAll(usedHitsound);
        unusedHitsounds.removeIf(GlobalSFX::isGlobalSFX);
        frame.addTabForAllDifficulties("Unused hitsound", unusedHitsounds);
        if (textOutput) {
            log.info("printing unused hs");
            try {
                writeListToFile(unusedHitsounds, "Unused.txt");
            } catch (IOException e) {
                log.error("failed to save unused HS to disk", e);
            }
        }

        // Find missing hitsound
        Set<String> missingHitsounds = new HashSet<>(usedHitsound);

        log.info("used {}", usedHitsound);
        log.info("in folder {}", physicalHS);
        missingHitsounds.removeAll(physicalHS);
        frame.addTabForAllDifficulties("Missing hitsound", missingHitsounds);
    }

    private static void readFromProperty(String path) {
        Properties prop = new Properties();
        InputStream input;

        try {
            String propertyPath = path + "\\" + propertyName;
            File f = new File(propertyPath);
            if (f.exists() && f.isFile()) {
                input = new FileInputStream(propertyPath);
                prop.load(input);
                if (prop.getProperty("Path") != null) {
                    OsuPath = prop.getProperty("Path");
                }
                if (prop.getProperty("TextOutput") != null) {
                    textOutput = Boolean.parseBoolean(prop.getProperty("TextOutput"));
                }

                input.close();
            }

        } catch (IOException e) {
            log.error("failing to read properties", e);
        }

    }

    private static void writeToProperty(String path) {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            String propertyPath = path + "\\" + propertyName;
            File f = new File(propertyPath);
            if (!f.exists()) {
                f.createNewFile();
            }
            FileInputStream input = new FileInputStream(propertyPath);
            prop.load(input);
            // System.out.println(OsuPath);
            prop.setProperty("Path", OsuPath);
            prop.setProperty("TextOutput", "" + textOutput);
            input.close();
            // save properties to project root folder
            output = new FileOutputStream(propertyPath);
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            log.error("error writing to properties", e);
        }
    }

    private static void writeListToFile(Set<String> set, String filename) throws IOException {
        FileWriter fw = new FileWriter(programStartPath + "\\" + filename);
        System.out.println(programStartPath + "\\" + filename);
        for (String s : set) {
            fw.write(s);
            fw.write("\n");
        }
        fw.close();
    }
}
