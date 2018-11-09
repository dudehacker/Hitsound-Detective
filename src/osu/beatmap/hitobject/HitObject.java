package osu.beatmap.hitobject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import osu.beatmap.event.Sample;
import osu.beatmap.timing.Timing;

public class HitObject implements Cloneable {
	// Instance Variables
	private int type; // 1=circle 128=LN
	private long endLN;
	private int xposition;
	private final int ypos = 192;
	private long startTime;
	private String hitSound;
	private int volume;
	private int column;
	private HitsoundType whistle_finish_clap = HitsoundType.HITNORMAL;
	private int setID = 0; // X in hit-hitnormalX.wav
	private Addition addition = Addition.AUTO;
	private SampleSet sampleSet = SampleSet.AUTO;
	private SampleSet timingPointSampleSet = SampleSet.SOFT;
	private int timingPointVolume = 70;

	// 64,192,708, 1, 2, 3 :2 :0 :0:
	// x, y ,t, type,whistle, sampleset:addition:setID:volume:
	// drum-softwhistle.wav auto :auto:

	public HitObject(int xposition, long startTime, String hitSound, int volume, HitsoundType whistle_finish_clap,
			int setID, Addition addition, SampleSet sampleSet) {
		this.xposition = xposition;
		this.startTime = startTime;
		if (hitSound == null) {
			hitSound = "";
		}
		this.hitSound = hitSound;
		this.volume = volume;
		this.whistle_finish_clap = whistle_finish_clap;
		this.setID = setID;
		this.addition = addition;
		this.sampleSet = sampleSet;
		type = 1;
	}

	public HitObject(HitObject hitObject) {
		this.addition = hitObject.addition;
		this.column = hitObject.column;
		this.endLN = hitObject.endLN;
		this.hitSound = hitObject.hitSound;
		this.sampleSet = hitObject.sampleSet;
		this.setID = hitObject.setID;
		this.startTime = hitObject.startTime;
		this.timingPointSampleSet = hitObject.timingPointSampleSet;
		this.timingPointVolume = hitObject.timingPointVolume;
		this.type = hitObject.type;
		this.volume = hitObject.volume;
		this.whistle_finish_clap = hitObject.whistle_finish_clap;
		this.xposition = hitObject.xposition;
	}


	public void convertColumnIDtoXpos(int KeyCount) {
		double columnWidth = 512.0 / KeyCount;
		xposition = (int) Math.round(column * columnWidth) + 10;
	}

	public HitObject clearHS() {
		whistle_finish_clap = HitsoundType.HITNORMAL;
		sampleSet = SampleSet.AUTO;
		addition = Addition.AUTO;
		setID = 0;
		hitSound = "";
		volume = 0;
		return this;
	}

	public boolean hasWAV_HS() {
		if (hitSound.contains(".wav")) {
			return true;
		}
		return false;
	}

	public boolean hasDefault_HS() {
		return whistle_finish_clap != HitsoundType.HITNORMAL && !hasWAV_HS();
	}

	public HitObject clone() {
		return new HitObject(this);
	}

	public Set<String> toHitsoundString() {
		List<Sample> samples = toSample();
		Set<String> output = new HashSet<>();
		for (Sample sample : samples) {
			output.add(sample.gethitSound());
		}
		return output;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addition == null) ? 0 : addition.hashCode());
		result = prime * result + (int) (endLN ^ (endLN >>> 32));
		result = prime * result + ((hitSound == null) ? 0 : hitSound.hashCode());
		result = prime * result + ((sampleSet == null) ? 0 : sampleSet.hashCode());
		result = prime * result + setID;
		result = prime * result + (int) (startTime ^ (startTime >>> 32));
		result = prime * result + ((timingPointSampleSet == null) ? 0 : timingPointSampleSet.hashCode());
		result = prime * result + timingPointVolume;
		result = prime * result + type;
		result = prime * result + volume;
		result = prime * result + ((whistle_finish_clap == null) ? 0 : whistle_finish_clap.hashCode());
		result = prime * result + xposition;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HitObject other = (HitObject) obj;
		if (addition != other.addition)
			return false;
		if (endLN != other.endLN)
			return false;
		if (hitSound == null) {
			if (other.hitSound != null)
				return false;
		} else if (!hitSound.equals(other.hitSound))
			return false;
		if (sampleSet != other.sampleSet)
			return false;
		if (setID != other.setID)
			return false;
		if (startTime != other.startTime)
			return false;
		if (timingPointSampleSet != other.timingPointSampleSet)
			return false;
		if (timingPointVolume != other.timingPointVolume)
			return false;
		if (type != other.type)
			return false;
		if (volume != other.volume)
			return false;
		if (whistle_finish_clap != other.whistle_finish_clap)
			return false;
		if (xposition != other.xposition)
			return false;
		return true;
	}

	public List<Sample> toSample() {
		List<Sample> output = new ArrayList<>();

		if (hitSound.contains(".wav")) {
			Sample s = new Sample(startTime, hitSound, volume);
			output.add(s);

		} else {
			String tempHS = "";

			String id = "";
			if (setID > 1) {
				id += setID;
			}

			// Apply SampleSet
			if (timingPointSampleSet == SampleSet.AUTO) {
				System.out.println("Timing Point SampleSet is not set!!");
				System.exit(0);
			}

			if (sampleSet == SampleSet.AUTO) {
				tempHS = timingPointSampleSet.toString() + "-";
			} else {
				tempHS = sampleSet.toString() + "-";
			}

			// Apply Addition
			if (whistle_finish_clap != HitsoundType.HITNORMAL) {
				if (addition != Addition.AUTO) {
					tempHS = addition.toString() + "-";
				}
			}

			// Apply Hitsound Type
			if (whistle_finish_clap.toString() == null) { // split
				for (HitsoundType type : whistle_finish_clap.split()) {
					output.add(new Sample(startTime, tempHS + type.toString() + id + ".wav", timingPointVolume));
				}
			} else {
				tempHS += whistle_finish_clap.toString() + id + ".wav";
				output.add(new Sample(startTime, tempHS, timingPointVolume));
			}

		}
		return output;
	}

	@Override
	public String toString() {
		if (type != 128) {
			// for a single note
			return "" + xposition + "," + ypos + "," + startTime + "," + type + "," + whistle_finish_clap.getValue() + ","
					+ sampleSet.getValue() + ":" + addition.getValue() + ":0:" + volume + ":" + hitSound;
		}
		// for a LN
		return "" + xposition + "," + ypos + "," + startTime + "," + type + "," + whistle_finish_clap.getValue() + ","
				+ endLN + ":" + sampleSet.getValue() + ":" + addition.getValue() + ":0:" + volume + ":" + hitSound;
	}

	public static Comparator<HitObject> StartTimeComparator = new Comparator<HitObject>() {
		@Override
		public int compare(HitObject ho1, HitObject ho2) {
			long t1 = ho1.startTime;
			long t2 = ho2.startTime;
			/* For ascending order */
			return (int) (t1 - t2);
		}
	};

	public static Comparator<HitObject> AdditionComparator = new Comparator<HitObject>() {
		@Override
		public int compare(HitObject ho1, HitObject ho2) {
			int a1 = ho1.addition.getValue();
			int a2 = ho2.addition.getValue();
			/* For ascending order */
			return a1 - a2;
		}
	};

	public long getStartTime() {
		return startTime;
	}

	public void applyTimingPoint(List<Timing> timingPoints) {
		long t = startTime;
		if (timingPoints.size() > 1) {
			for (int i = 0; i < timingPoints.size() - 1; i++) {
				Timing tp1 = timingPoints.get(i);
				long t1 = tp1.getOffset();
				Timing tp2 = timingPoints.get(i + 1);
				long t2 = tp2.getOffset();

				if (t == t1) {
					applyTimingPoint(tp1);
					return;
				} else if (t == t2) {
					applyTimingPoint(tp2);
					return;
				} else if (t1 < t && t < t2) {
					applyTimingPoint(tp1);
					return;
				}

			}
		} else if (timingPoints.size() == 1) {
			Timing tp = timingPoints.get(0);
			applyTimingPoint(tp);
			return;
		}
		applyTimingPoint(timingPoints.get(timingPoints.size() - 1));
	}

	// only for default HS
	private void applyTimingPoint(Timing tp) {
		if (volume == 0) {
			timingPointVolume = tp.getVolume();
		}
		if (sampleSet == SampleSet.AUTO) {
			timingPointSampleSet = tp.getSampleSet();
		}
		if (setID == 0) {
			setID = tp.getSetID();
		}
	}

	public int getTimingPointVolume() {
		return timingPointVolume;
	}

	public void copyHS(HitObject input) {
		hitSound = input.hitSound;
		volume = input.volume;
		whistle_finish_clap = input.whistle_finish_clap;
		setID = input.setID;
		sampleSet = input.sampleSet;
		addition = input.addition;
		timingPointVolume = input.timingPointVolume;
		timingPointSampleSet = input.timingPointSampleSet;
	}

	public boolean isMuted() {
		if (hasWAV_HS() && volume == 0) {
			return true;
		}
		return false;
	}

	public boolean hasHitsound() {
		return hasWAV_HS() || whistle_finish_clap != HitsoundType.HITNORMAL;
	}

	public static Comparator<HitObject> ColumnComparator = new Comparator<HitObject>() {
		@Override
		public int compare(HitObject n1, HitObject n2) {
			long c1 = n1.xposition;
			long c2 = n2.xposition;
			/* For ascending order */
			return (int) (c1 - c2);
		}
	};

	public Addition getAddition() {
		return this.addition;
	}

	public HitsoundType getWhistleFinishClap() {
		return this.whistle_finish_clap;
	}

	public void setType(int type) {
		this.type = type;
	}
}
