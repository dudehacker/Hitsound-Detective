package osu.beatmap;

public abstract class Section {
	private String header;
	
	public Section(String header) {
		this.header = header;
	}
	
	public final String getHeader() {
		return header;
	}
	
	public abstract void init(String[] lines);
	
}
