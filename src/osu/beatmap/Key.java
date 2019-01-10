package osu.beatmap;

public class Key{
	private String name;
	private Class<?> type;
	
	public Key(String name, Class<?> c) {
		this.name = name;
		this.type = c;
	}
	
	public String getName() {
		return name;
	}
	
	public Class<?> getType(){
		return type;
	}

}
