package server.model.osu.api;

public enum Genre {
	ANY(0), UNSPECIFIED(1),VIDEO_GAME(2),ANIME(3),ROCK(4),POP(5),OTHER(6),NOVELTY(7),HIP_POP(9),ELECTRONIC(10);
	private int value;
	
	private Genre(int value){
		this.value = value;
	}
	
	public int getValue(){
		return value;
	}
}
