package osu.beatmap.timing;

import java.util.ArrayList;
import java.util.List;

import osu.beatmap.Section;
import osu.beatmap.hitobject.SampleSet;
import util.BeatmapUtils;

public final class TimingSection extends Section{
	
	private List<Timing> timings;
	
	public TimingSection() {
		super("[TimingPoints]");
		timings = new ArrayList<>();
	}

	public void init(String[] lines) {
		for (String line : lines) {
			if (line.contains(",") ) {
				String[] parts = line.split(",");
				if (parts[0].contains(".")) {
					parts[0] = parts[0].substring(0, parts[0].indexOf('.'));
				}
				long offset = Long.parseLong(parts[0]);
				double mspb = Double.parseDouble(parts[1]);
				int meter = Integer.parseInt(parts[2]);
				SampleSet sampleSet = SampleSet.createSampleSet(Integer.parseInt(parts[3]));
				int setID = Integer.parseInt(parts[4]);
				int volume = Integer.parseInt(parts[5]);
				int isInherited = Integer.parseInt(parts[6]);
				int isKiai = Integer.parseInt(parts[7]);
				Timing timing = new Timing(offset, mspb, meter, sampleSet, setID, volume, isInherited, isKiai);
				timings.add(timing);
			}
		}
	}

	@Override
	public final String toString() {
		return getHeader() + BeatmapUtils.nl
				+ BeatmapUtils.convertListToString(timings) ;
	}
}
