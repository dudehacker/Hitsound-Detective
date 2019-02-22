package detective.mistake;

import detective.image.ImageDetective;

public enum MistakeType {
	Inconsistency("Inconsistent hitsound", Severity.WARNING),
	DuplicateHitsound("Duplicate Hitsound", Severity.WARNING),
	SBwhenNoNote("Please move SB sound on notes", Severity.WARNING),
	MutedSB("Muted Storyboard Sample", Severity.PROBLEM),
	MutedHO("Muted hitsound",Severity.PROBLEM),
	SameOffsetTiming("Same Offset Timing",Severity.PROBLEM),
	UnusedGreenTiming("Unused timing",Severity.PROBLEM),
	MissingImage("Bg image is missing",Severity.PROBLEM),
	BadResolutionImage("Bg image must be within " + ImageDetective.MAX_WIDTH + "x"+ ImageDetective.MAX_HEIGHT + " pixels", Severity.PROBLEM),
	MissingHitsound("Add missing hitsound sample",Severity.PROBLEM),
	WrongFormatHitsound("Unrankeable format sample",Severity.PROBLEM),
	UnusedHitsound("Remove un-used sample",Severity.PROBLEM)
	;

	private final String value;
	
	private final Severity severity;
	
	private MistakeType(String value, Severity severity) {
		this.severity = severity;
		this.value = value;
	}
	
	public Severity getSeverity() {
		return severity;
	}

	@Override
	public String toString() {
		return value;
	}
}
