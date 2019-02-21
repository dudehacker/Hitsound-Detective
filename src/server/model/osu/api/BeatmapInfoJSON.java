package server.model.osu.api;

import java.util.Date;

import osu.beatmap.general.Mode;

public class BeatmapInfoJSON {
	private Approved approved;
	private Date approved_date;
	private Date last_update;
	private String artist;
	private String beatmap_id;
	private String beatmapset_id;
	private float bpm;
	private String creator;
	private String creator_id;
	private String difficultyrating; // star rating
	private float diff_size; // key count
	private float diff_overall; // Overall difficulty (OD)
	private float diff_approach; // Approach Rate (AR)
	private float diff_drain; // Healthdrain (HP)
	private float hit_length; // seconds from first note to last note not including breaks
	private String source;
	private Genre genre_id;
	private String language_id;
	private String title;
	private float total_length;
	private String version; // diff name
	private String file_md5;
	private Mode mode;
	private String tags;
	private int favourite_count;
	private int playcount;
	private int passcount;
	private int max_combo;
	
	@Override
	public String toString() {
		return "BeatmapInfoJSON [approved=" + approved + ", approved_date=" + approved_date + ", last_update="
				+ last_update + ", artist=" + artist + ", beatmap_id=" + beatmap_id + ", beatmapset_id=" + beatmapset_id
				+ ", bpm=" + bpm + ", creator=" + creator + ", creator_id=" + creator_id + ", difficultyrating="
				+ difficultyrating + ", diff_size=" + diff_size + ", diff_overall=" + diff_overall + ", diff_approach="
				+ diff_approach + ", diff_drain=" + diff_drain + ", hit_length=" + hit_length + ", source=" + source
				+ ", genre_id=" + genre_id + ", language=" + language_id + ", title=" + title + ", total_length="
				+ total_length + ", version=" + version + ", file_md5=" + file_md5 + ", mode=" + mode + ", tags=" + tags
				+ ", favourite_count=" + favourite_count + ", playcount=" + playcount + ", passcount=" + passcount
				+ ", max_combo=" + max_combo + "]";
	}
	
	public Approved getApproved() {
		return approved;
	}

	public Date getApproved_date() {
		return approved_date;
	}

	public Date getLast_update() {
		return last_update;
	}

	public String getArtist() {
		return artist;
	}

	public String getBeatmap_id() {
		return beatmap_id;
	}

	public String getBeatmapset_id() {
		return beatmapset_id;
	}

	public float getBpm() {
		return bpm;
	}

	public String getCreator() {
		return creator;
	}

	public String getCreator_id() {
		return creator_id;
	}

	public String getDifficultyrating() {
		return difficultyrating;
	}

	public float getDiff_size() {
		return diff_size;
	}

	public float getDiff_overall() {
		return diff_overall;
	}

	public float getDiff_approach() {
		return diff_approach;
	}

	public float getDiff_drain() {
		return diff_drain;
	}

	public float getHit_length() {
		return hit_length;
	}

	public String getSource() {
		return source;
	}

	public Genre getGenre_id() {
		return genre_id;
	}

	public String getLanguage_id() {
		return language_id;
	}

	public String getTitle() {
		return title;
	}

	public float getTotal_length() {
		return total_length;
	}

	public String getVersion() {
		return version;
	}

	public String getFile_md5() {
		return file_md5;
	}

	public Mode getMode() {
		return mode;
	}

	public String getTags() {
		return tags;
	}

	public int getFavourite_count() {
		return favourite_count;
	}

	public int getPlaycount() {
		return playcount;
	}

	public int getPasscount() {
		return passcount;
	}

	public int getMax_combo() {
		return max_combo;
	}

	public void setApproved(Approved approved) {
		this.approved = approved;
	}
	public void setApproved_date(Date approved_date) {
		this.approved_date = approved_date;
	}
	public void setLast_update(Date last_update) {
		this.last_update = last_update;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public void setBeatmap_id(String beatmap_id) {
		this.beatmap_id = beatmap_id;
	}
	public void setBeatmapset_id(String beatmapset_id) {
		this.beatmapset_id = beatmapset_id;
	}
	public void setBpm(float bpm) {
		this.bpm = bpm;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}
	public void setDifficultyrating(String difficultyrating) {
		this.difficultyrating = difficultyrating;
	}
	public void setDiff_size(float diff_size) {
		this.diff_size = diff_size;
	}
	public void setDiff_overall(float diff_overall) {
		this.diff_overall = diff_overall;
	}
	public void setDiff_approach(float diff_approach) {
		this.diff_approach = diff_approach;
	}
	public void setDiff_drain(float diff_drain) {
		this.diff_drain = diff_drain;
	}
	public void setHit_length(float hit_length) {
		this.hit_length = hit_length;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public void setGenre_id(Genre genre_id) {
		this.genre_id = genre_id;
	}
	public void setLanguage(String language_id) {
		this.language_id = language_id;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setTotal_length(float total_length) {
		this.total_length = total_length;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public void setFile_md5(String file_md5) {
		this.file_md5 = file_md5;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public void setFavourite_count(int favourite_count) {
		this.favourite_count = favourite_count;
	}
	public void setPlaycount(int playcount) {
		this.playcount = playcount;
	}
	public void setPasscount(int passcount) {
		this.passcount = passcount;
	}
	public void setMax_combo(int max_combo) {
		this.max_combo = max_combo;
	}
}
