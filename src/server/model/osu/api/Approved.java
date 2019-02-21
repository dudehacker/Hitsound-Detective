package server.model.osu.api;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Approved {
	LOVED(4), QUALIFIED(3), APPROVED(2), RANKED(1), PENDING(0), WIP(-1), GRAVEYARD(-2);
	
	private final int value;
	
	Approved(final int value){
		this.value = value;
	}
	
	@JsonValue
	@Override
	public String toString(){
		return ""+value;
	}
}
