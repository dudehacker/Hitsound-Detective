package osu;

public enum SampleSet {
	AUTO(0,null), NORMAL(1,"normal"), SOFT(2,"soft"), DRUM(3,"drum");
	
	private final int value;
	private final String text;
	
	private SampleSet(int value, String text) {
		this.value = value;
		this.text = text;
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
	
	public int getValue() {
		return value;
	}
}
