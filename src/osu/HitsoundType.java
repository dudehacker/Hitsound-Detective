package osu;

public enum HitsoundType {
	HITNORMAL(0, "hitnormal"), HITWHISTLE(2,"hitwhistle"), HITFINISH(4,"hitfinish"), HITCLAP(8,"hitclap"), HITWHISTLE_FINISH(6), HITWHISTLE_CLAP(
			10), HITWHISTLE_FINISH_CLAP(14), HITFINISH_CLAP(12);

	private final int value;
	private final String text;

	private HitsoundType(int value) {
		this.value = value;
		text = null;
	}

	private HitsoundType(int value, String text) {
		this.value = value;
		this.text = text;
	}
	
	public static HitsoundType createHitsoundType(int value) {
		for (HitsoundType hitsoundType : HitsoundType.values()) {
			if (value == hitsoundType.value) {
				return hitsoundType;
			}
		}
		throw new IllegalArgumentException("Invalid value :" + value);
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return text;
	}

	public int getNumberOfHitsounds() {
		if (this == HITWHISTLE_FINISH_CLAP) {
			return 3;
		}

		if (value == 6 || value > 8) {
			return 2;
		}

		return 1;
	}

	public HitsoundType[] split() {
		int size = getNumberOfHitsounds();
		HitsoundType[] output = new HitsoundType[size];

		if (size == 1) {
			output[0] = this;
			return output;
		}
				
		if (value == HITWHISTLE_FINISH_CLAP.value) {
			output[0] = HITWHISTLE;
			output[1] = HITFINISH;
			output[2] = HITCLAP;
			return output;
		} 

		if (value == HITFINISH_CLAP.value) {
			output[0] = HITFINISH;
			output[1] = HITCLAP;
		}
			
		if (value == HITWHISTLE_FINISH.value) {
			output[0] = HITWHISTLE;
			output[1] = HITFINISH;
		}
			
		if (value == HITWHISTLE_CLAP.value) {
			output[0] = HITWHISTLE;
			output[1] = HITCLAP;
		}

		return output;
	}
}
