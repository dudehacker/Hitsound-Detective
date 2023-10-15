package detective;

import detective.hitsound.HitsoundDetectiveThread;
import detective.image.ImageDetective;
import detective.mistake.Mistake;
import lombok.extern.slf4j.Slf4j;
import osu.beatmap.Beatmap;
import util.BeatmapUtils;

import java.io.*;
import java.text.ParseException;
import java.util.*;

@Slf4j
public class Main {
    private static final String propertyName = "Hitsound Detective config.properties";
    private static String OsuPath = "C:\\Program Files (x86)\\osu!\\Songs";
    private static String programStartPath = System.getProperty("user.dir");
    private static boolean textOutput = true;
    private static List<HitsoundDetectiveThread> list = new ArrayList<>();
    private static Set<String> imageMistakes = new HashSet<>();

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
                e.printStackTrace();
            }
        }
        System.out.println("after reading all beatmaps");
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
            for (String s : hd.getUsedHS()) {
                usedHitsound.add(s);
            }
        }

        // get actual Hitsound
        FilenameFilter hsFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".wav") || lowercaseName.endsWith(".ogg")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        Set<String> physicalHS = new HashSet<>();
        File[] wavFiles = new File(OsuPath).listFiles(hsFilter);


        for (File wav : wavFiles) {
            physicalHS.add(wav.getName());
        }

        // Find un-used hitsound
        TreeSet<String> unusedHitsounds = new TreeSet<>(physicalHS);
        unusedHitsounds.removeAll(usedHitsound);
        unusedHitsounds.remove("combobreak.wav");
        frame.addTabForAllDifficulties("Unused hitsound", unusedHitsounds);
        if (textOutput) {
            System.out.println("printing unused hs");
            try {
                writeListToFile(unusedHitsounds, "Unused.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Find missing hitsound
        Set<String> missingHitsounds = new HashSet<>(usedHitsound);
        System.out.println("used\n" + usedHitsound.toString());
        System.out.println("exist\n" + physicalHS.toString());
        missingHitsounds.removeAll(physicalHS);
        frame.addTabForAllDifficulties("Missing hitsound", missingHitsounds);

        // frame.addTabForAllDifficulties("Bad Images", imageMistakes);
    }

    private static void readFromProperty(String path) {
        Properties prop = new Properties();
        InputStream input = null;

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
            e.printStackTrace();
        }
    }

    private static void writeListToFile(Set<String> set, String filename) throws IOException {
        FileWriter fw = new FileWriter(programStartPath + "\\" + filename);
        System.out.println(programStartPath + "\\" + filename);
        Iterator<String> ite = set.iterator();
        while (ite.hasNext()) {
            String s = ite.next();
            fw.write(s);
            fw.write("\n");
        }
        fw.close();
    }
}
