package osu.beatmap;

import lombok.extern.slf4j.Slf4j;
import osu.beatmap.difficulty.DifficultySection;
import osu.beatmap.editor.EditorSection;
import osu.beatmap.event.EventSection;
import osu.beatmap.general.GeneralSection;
import osu.beatmap.hitobject.HitObjectSection;
import osu.beatmap.metadata.MetadataSection;
import osu.beatmap.timing.TimingSection;
import util.BeatmapUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public final class Beatmap {

    private static final String nl = BeatmapUtils.nl;
    private final List<Section> sections = new ArrayList<>();
    // Instance Variables
    private int OsuVersion = 14;

    public Beatmap() {
        sections.add(new GeneralSection());
        sections.add(new EditorSection());
        sections.add(new MetadataSection());
        sections.add(new DifficultySection());
        sections.add(new EventSection());
        sections.add(new TimingSection());
        sections.add(new HitObjectSection());
    }

    public Beatmap(File file) throws ParseException, IOException {

        this();
        if (!file.exists() || !file.getAbsolutePath().endsWith(".osu")) {
            throw new IllegalArgumentException("Can't open file " + file.getAbsolutePath());
        }

        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            // read line by line
            while ((line = br.readLine()) != null) {
                if (!"[Colours]".equals(line) && !line.contains("Combo")) {
                    text.append(line).append(System.lineSeparator());
                }
            }
            if (text.toString().contains("osu file format")) {
                OsuVersion = Integer.parseInt(text.toString().split(System.lineSeparator())[0].split("osu file format v")[1]);
            }

            // separate to sections
            for (int i = 0; i < sections.size(); i++) {
                Section section1 = sections.get(i);
                Section section2 = null;
                if (i != sections.size() - 1) {
                    section2 = sections.get(i + 1);
                }
                String sectionText = getSectionText(text.toString(), section1, section2);
                sections.get(i).init(sectionText.split(System.lineSeparator()));
            }

            // Set up HitObject hs based on timing points
            getHitObjectSection().addTimingHitsound(getTimingSection());

        } catch (Exception e) {
            log.error("failed to parse file", e);
        }
    }

    public GeneralSection getGeneralSection() {
        return (GeneralSection) sections.get(0);
    }

    public EditorSection getEditorSection() {
        return (EditorSection) sections.get(1);
    }

    public MetadataSection getMetadataSection() {
        return (MetadataSection) sections.get(2);
    }

    public DifficultySection getDifficultySection() {
        return (DifficultySection) sections.get(3);
    }

    public EventSection getEventSection() {
        return (EventSection) sections.get(4);
    }

    public TimingSection getTimingSection() {
        return (TimingSection) sections.get(5);
    }

    public HitObjectSection getHitObjectSection() {
        return (HitObjectSection) sections.get(6);
    }

    private String getSectionText(String text, Section section1, Section section2) {
        String split1 = text.split(Pattern.quote(section1.getHeader() + System.lineSeparator()))[1];
        if (section2 == null) {
            return split1;
        }
        String split2 = split1.split(Pattern.quote(section2.getHeader()))[0];
        return split2;
    }

    public Beatmap clearHitsounds() {
        getEventSection().getSamples().clear();
        getHitObjectSection().clearHitsound();
        return this;
    }

    @Override
    public String toString() {

        StringBuilder output = new StringBuilder("osu file format v" + OsuVersion + nl);
        for (Section section : sections) {
            output.append(nl).append(section);
        }
        return output.toString();
    }
}
