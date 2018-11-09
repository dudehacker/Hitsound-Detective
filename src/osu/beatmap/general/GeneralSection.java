package osu.beatmap.general;

import osu.beatmap.Section;
import osu.beatmap.hitobject.SampleSet;
import util.BeatmapUtils;

public final class GeneralSection extends Section {
	private String audioFilename;
	private int audioLeadIn;
	private int previewTime;
	private int countdown;
	private SampleSet sampleSet;
	private double stackLeniency;
	private Mode mode;
	private int letterboxInBreaks;
	private int specialStyle;
	private int widecreenStoryboard;

	private static final String nl = BeatmapUtils.nl;

	public GeneralSection() {
		super("[General]");
		audioFilename = "audio.mp3";
		audioLeadIn = 0;
		previewTime = -1;
		countdown = 0;
		sampleSet = SampleSet.SOFT;
		stackLeniency = 0.7;
		mode = Mode.MANIA;
		letterboxInBreaks = 0;
		specialStyle = 0;
		widecreenStoryboard = 0;
	}

	public void init(String[] lines) {
		audioFilename = lines[0].split(":")[1].trim();
		audioLeadIn = Integer.parseInt(lines[1].split(":")[1].trim());
		previewTime = Integer.parseInt(lines[2].split(":")[1].trim());
		countdown = Integer.parseInt(lines[3].split(":")[1].trim());
		sampleSet = SampleSet.createSampleSet(lines[4].split(":")[1].trim());
		stackLeniency = Double.parseDouble(lines[5].split(":")[1].trim());
		mode = Mode.createMode(Integer.parseInt(lines[6].split(":")[1].trim()));
		letterboxInBreaks = Integer.parseInt(lines[7].split(":")[1].trim());
		specialStyle = Integer.parseInt(lines[8].split(":")[1].trim());
		widecreenStoryboard = Integer.parseInt(lines[9].split(":")[1].trim());
	}

	private String getUpperSampleSet() {
		return sampleSet.toString().substring(0, 1).toUpperCase()
				+ sampleSet.toString().substring(1, sampleSet.toString().length());
	}

	@Override
	public String toString() {
		return getHeader() + nl + "AudioFilename: " + audioFilename + nl + "AudioLeadIn: " + audioLeadIn + nl
				+ "PreviewTime: " + previewTime + nl + "Countdown: " + countdown + nl + "SampleSet: "
				+ getUpperSampleSet() + nl + "StackLeniency: " + stackLeniency + nl + "Mode: " + mode.toString() + nl
				+ "LetterboxInBreaks: " + letterboxInBreaks + nl + "SpecialStyle: " + specialStyle + nl
				+ "WidescreenStoryboard: " + widecreenStoryboard + nl;
	}

}
