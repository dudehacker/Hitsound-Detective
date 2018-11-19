package osu.beatmap;

public class Property {
	
	private Integer intValue;
	private Double doubleValue;
	private String value;
	
	private static final char separator = ':';

	public Property(String line) {
		if (line.charAt(line.length()-1) != separator) {
			String s = line.split(""+separator)[1].trim();
			try {
				doubleValue = Double.parseDouble(s);
				return;
			} catch (NumberFormatException e) {
				
			}
			try {
				intValue = Integer.parseInt(s);
				return;
			} catch (NumberFormatException e) {
				
			}
			value = s;
		}
	}
	
	public Property(double d) {
		this.doubleValue = d;
	}
	
	public Property(int i) {
		this.intValue = i;
	}

	@Override
	public String toString() {
		if (doubleValue != null) {
			return ""+doubleValue;
		}
		if (intValue != null) {
			return ""+intValue;
		}
		if (value != null) {
			return value;
		}
		return "";
	}
	
}
