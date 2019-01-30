package server.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import detective.mistake.Mistake;

public class Mod {
	private String name;
	
	@JsonProperty("comments")
	private List<Mistake> mistakes;
	
	public Mod(String diff){
		this.name = diff;
		mistakes = new ArrayList<>();
	}

	public String getDifficulty() {
		return name;
	}

	public List<Mistake> getMistakes() {
		return mistakes;
	}
	
	public void addMistake(Mistake mis){
		mistakes.add(mis);
	}
	
	
	
}
