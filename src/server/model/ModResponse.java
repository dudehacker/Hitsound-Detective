package server.model;

import java.util.ArrayList;
import java.util.List;

public class ModResponse {
	
	private String artist;
	private String title;
	private String mapper;
	private int beatmapSetId;
	private String url;
	private List<Mod> tabs;
	
	public ModResponse(String url){
		this.url = url;
		tabs = new ArrayList<Mod>();
	}
	
	public void addTab(Mod mod){
		tabs.add(mod);
	}
	
	public List<Mod> getTabs() {
		return tabs;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMapper(String mapper) {
		this.mapper = mapper;
	}

	public void setBeatmapSetId(int beatmapSetId) {
		this.beatmapSetId = beatmapSetId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getMapper() {
		return mapper;
	}

	public int getBeatmapSetId() {
		return beatmapSetId;
	}

	public String getUrl(){
		return url;
	}
	
}
