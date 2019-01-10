package osu.beatmap.general;

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

	public static Mode createMode(String rawValue) {
		try {
			return createMode(Integer.parseInt(rawValue.trim()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException("Invalid value :" + rawValue);
	}
}
