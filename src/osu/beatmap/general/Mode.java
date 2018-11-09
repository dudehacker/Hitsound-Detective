package osu.beatmap.general;

import osu.beatmap.hitobject.SampleSet;

public enum Mode {
	STD(0),MANIA(3),TAIKO(1),CTB(2);
	
	private final int value;
	
	private Mode(int value) {
		this.value = value;
	}
	
	public String toString() {
		return ""+value;
	}

	public static Mode createMode(int value) {
		for (Mode mode : Mode.values()) {
			if (value == mode.value) {
				return mode;
			}
		} 
		throw new IllegalArgumentException("Invalid value :" + value);
	}
}
