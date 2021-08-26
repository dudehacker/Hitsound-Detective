package server.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import detective.mistake.Mistake;

public class Mod implements Comparable<Mod>{
	
	private String name;
	private int noteCount;
	
	@Override
	public String toString() {
		return "Mod [name=" + name + ", noteCount=" + noteCount + ", mistakes=" + mistakes + "]";
	}

	@JsonProperty("comments")
	private List<Mistake> mistakes;
	
	public Mod(String diff){
		this.name = diff;
		mistakes = new ArrayList<>();
	}
	
	public void setNoteCount(int count){
		noteCount = count;
	}
	
	public int getNoteCount(){
		return noteCount;
	}

	@JsonProperty("name")
	public String getDifficulty() {
		return name;
	}

	public List<Mistake> getMistakes() {
		return mistakes;
	}
	
	public void addMistake(Mistake mis){
		mistakes.add(mis);
	}

	public void addMistake(List<? extends Mistake> mistakes) {
		this.mistakes.addAll(mistakes);
	}

	@Override
	public int compareTo(Mod other) {
		return this.noteCount - other.noteCount;
	}
	
	
}
