package osu.beatmap.hitobject;

public enum SampleSet {
	AUTO(0,null), NORMAL(1,"normal"), SOFT(2,"soft"), DRUM(3,"drum");
	
	private final int value;
	private final String text;
	
	private SampleSet(int value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public static SampleSet createSampleSet(String value) {
		if (value.equalsIgnoreCase("none")) {
			return AUTO;
		}
		for (SampleSet sampleSet : SampleSet.values()) {
			if (value.equalsIgnoreCase(sampleSet.text)) {
				return sampleSet;
			}
		} 
		throw new IllegalArgumentException("Invalid value :" + value);
	}
	
	public static SampleSet createSampleSet(int value) {
		for (SampleSet sampleSet : SampleSet.values()) {
			if (value == sampleSet.value) {
				return sampleSet;
			}
		} 
		throw new IllegalArgumentException("Invalid value :" + value);
	}
	
	public String toString() {
		return text;
	}
	
	public String toUpperString() {
		return (""+text.charAt(0)).toUpperCase() + text.substring(1, text.length());
	}
	
	public int getValue() {
		return value;
	}
}
