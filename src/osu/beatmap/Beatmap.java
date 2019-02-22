package osu.beatmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import osu.beatmap.difficulty.DifficultySection;
import osu.beatmap.editor.EditorSection;
import osu.beatmap.event.EventSection;
import osu.beatmap.general.GeneralSection;
import osu.beatmap.hitobject.HitObjectSection;
import osu.beatmap.metadata.MetadataSection;
import osu.beatmap.timing.TimingSection;
import util.BeatmapUtils;

public final class Beatmap {

	// Instance Variables
	private int OsuVersion = 14;

	private List<Section> sections = new ArrayList<>();

	private static final String nl = BeatmapUtils.nl;

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

		String text = "";
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			String line;
			// read line by line
			while ((line = br.readLine()) != null) {
				text += line + System.lineSeparator();

			}
			if (text.contains("osu file format")) {
				OsuVersion = Integer.parseInt(text.split(System.lineSeparator())[0].split("osu file format v")[1]);
			}

			// separate to sections
			for (int i = 0; i < sections.size(); i++) {
				Section section1 = sections.get(i);
				Section section2 = null;
				if (i != sections.size() - 1) {
					section2 = sections.get(i + 1);
				}
				String sectionText = getSectionText(text, section1, section2);
				sections.get(i).init(sectionText.split(System.lineSeparator()));
			}
			
			// Set up HitObject hs based on timing points
			getHitObjectSection().addTimingHitsound(getTimingSection());

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

		String output = "osu file format v" + OsuVersion + nl;
		for (Section section : sections) {
			output += nl + section;
		}
		return output;
	}
}
