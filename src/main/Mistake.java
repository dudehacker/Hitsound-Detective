package main;

public class Mistake implements Comparable<Mistake> {
	private long time;
	private MistakeType description;
	

	
	public Mistake(long time, MistakeType description) {
		this.time = time;
		this.description = description;
	}
	
	public long getTime() {
		return time;
	}
	
	public String getDescription() {
		return description.toString();
	}
	
	public String getURL() {
		String hyperlink = "osu://edit/" + convertTiming();
		return hyperlink;
	}
	

	
	private String convertTiming() {
		if (time == 0) {
			return "00:00:000";
		}
		String input = "" + time;
		String ms = "000";
		String sec = "00";
		String min = "00";
		if (time > 99) {
			ms = input.substring(input.length() - 3, input.length());
			if (time > 999) {
				long seconds = (time - Long.parseLong(ms)) / 1000;
				if (seconds > 59) {
					sec = "" + seconds % 60;
					if (sec.length() == 1) {
						sec = "0" + sec;
					}
					long minutes = (seconds - Long.parseLong(sec)) / 60;
					if (minutes > 99) {
						System.out.println("something wrong in converting timing to string : " + time);
						System.exit(-1);
					} else if (minutes > 9) {
						min = "" + minutes;
					} else {
						min = "0" + minutes;
					}
				} else if (seconds > 9) {
					sec = "" + seconds;
				} else {
					sec = "0" + seconds;
				}
			}
		} else if (time > 9) {
			ms = "0" + time;
		} else {
			ms = "00" + time;
		}

		return min + ":" + sec + ":" + ms;
	}

	@Override
	public int compareTo(Mistake other) {
		return (int) (this.time - other.time);
	}
	
}
