package main;

public enum MistakeType {
	Inconsistency("Inconsistent hitsound"),
	SBwhenNoNote("Please move SB sound on notes"),
	MutedSB("Muted Storyboard Sample"),
	MutedHO("Muted hitsound"),
	SameOffsetTiming("Same Offset Timing"),
	UnusedGreenTiming("Unused timing")
	;

	private final String value;
	
	private MistakeType(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
}
