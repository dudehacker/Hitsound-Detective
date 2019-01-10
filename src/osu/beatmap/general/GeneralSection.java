package osu.beatmap.general;

import osu.beatmap.Key;
import osu.beatmap.Section;
import osu.beatmap.hitobject.SampleSet;

public final class GeneralSection extends Section {
	private static final Key audioFileName = new Key("AudioFilename",String.class);
	private static final Key audioLeadIn = new Key("AudioLeadIn",Integer.class);
	private static final Key previewTime = new Key("PreviewTime",Integer.class);
	private static final Key countdown = new Key("Countdown",Integer.class);
	private static final Key sampleSet = new Key("SampleSet",SampleSet.class);
	private static final Key stackLeniency = new Key("StackLeniency",Double.class);
	private static final Key mode = new Key("Mode",Mode.class);
	private static final Key letterboxInBreaks = new Key("LetterboxInBreaks",Integer.class);
	private static final Key specialStyle = new Key("SpecialStyle",Integer.class);
	private static final Key widecreenStoryboard = new Key("WidescreenStoryboard",Integer.class);

	public GeneralSection() {
		super("[General]");
		addProperty(audioFileName, "audio.mp3");
		addProperty(audioLeadIn, 0);
		addProperty(previewTime, -1);
		addProperty(countdown, 0);
		addProperty(sampleSet, SampleSet.SOFT);
		addProperty(stackLeniency, 0.7);
		addProperty(mode, Mode.MANIA);
		addProperty(letterboxInBreaks, 0);
		addProperty(specialStyle, 0);
		addProperty(widecreenStoryboard, 0);
	}
	
	public Mode getMode() {
		return (Mode) getProperty(mode);
	}

}
