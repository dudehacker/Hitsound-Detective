package server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import detective.mistake.Mistake;
import detective.mistake.MistakeType;
import detective.mistake.TimedMistake;
import osu.beatmap.Beatmap;
import osu.beatmap.metadata.MetadataSection;

public class ModResponse {
	
	private String artist;
	private String title;
	private String mapper;
	private int beatmapSetId;
	private String url;
	private List<Mod> tabs = new ArrayList<>();
	

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModResponse){
			ModResponse mod = (ModResponse) obj;
			return this.toString().equals(mod.toString());
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return "ModResponse [artist=" + artist + ", title=" + title + ", mapper=" + mapper + ", beatmapSetId="
				+ beatmapSetId + ", url=" + url + ", tabs=" + tabs + "]";
	}

	public ModResponse(String url){
		this.url = url;
		tabs = new ArrayList<>();
	}
	
	public ModResponse(Beatmap beatmap) {
		MetadataSection metadata = beatmap.getMetadataSection();
		setArtist(metadata.getProperty(MetadataSection.artistUnicodeKey).toString());
		setTitle(metadata.getProperty(MetadataSection.titleKey).toString());
		setMapper(metadata.getProperty(MetadataSection.creatorKey).toString());
		setBeatmapSetId((int) metadata.getProperty(MetadataSection.beatmapSetIdKey));
		setUrl("https://osu.ppy.sh/beatmapsets/"+beatmapSetId+"/#mania");
	}
	
	static ModResponse dummyModResponse(String url){
		ModResponse res = new ModResponse(url);
		res.setArtist("Unknown Artist");
		res.setTitle("Unknown Title");
		res.setMapper("Unknown Mapper");
		Mod all = new Mod("All");
		all.setNoteCount(-1);
		all.addMistake(new Mistake(MistakeType.MissingImage));
		all.addMistake(new Mistake(MistakeType.BadResolutionImage));
		res.addTab(all);

		Mod ez = new Mod("EZ");
		ez.addMistake(new TimedMistake(230, MistakeType.MutedHO));
		ez.addMistake(new TimedMistake(450, MistakeType.Inconsistency));
		ez.addMistake(new TimedMistake(870, MistakeType.Inconsistency));
		ez.addMistake(new TimedMistake(1020, MistakeType.SBwhenNoNote));
		res.addTab(ez);

		Mod nm = new Mod("NM");
		nm.addMistake(new TimedMistake(100, MistakeType.UnusedGreenTiming));
		nm.addMistake(new TimedMistake(200, MistakeType.DuplicateHitsound));
		nm.addMistake(new TimedMistake(480, MistakeType.Inconsistency));
		res.addTab(nm);

		return res;
	}

	public void addTab(Mod mod){
		tabs.add(mod);
		Collections.sort(tabs);
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
