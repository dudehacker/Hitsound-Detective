package detective;

import detective.image.ImageDetective;

public enum MistakeType {
	Inconsistency("Inconsistent hitsound"),
	SBwhenNoNote("Please move SB sound on notes"),
	MutedSB("Muted Storyboard Sample"),
	MutedHO("Muted hitsound"),
	SameOffsetTiming("Same Offset Timing"),
	UnusedGreenTiming("Unused timing"),
	MissingImage("Bg image is missing"),
	BadResolutionImage("Bg image must be within " + ImageDetective.MAX_WIDTH + "x"+ ImageDetective.MAX_HEIGHT + " pixels"), 
	DuplicateHitsound("Duplicate Hitsound")
	;

	private final String value;
	
	private MistakeType(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
