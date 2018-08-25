package osu;

public enum Addition {
	AUTO(0, null), NORMAL(1, "normal"), SOFT(2, "soft"), DRUM(3, "drum");

	private final int value;
	private final String text;

	private Addition(int value, String text) {
		this.value = value;
		this.text = text;
	}

	public String toString() {
		return text;
	}

	public int getValue() {
		return value;
	}

	public static Addition createAddition(int value) {
		for (Addition addition : Addition.values()) {
			if (value == addition.value) {
				return addition;
			}
		}
		throw new IllegalArgumentException("Invalid value :" + value);
	}
}
