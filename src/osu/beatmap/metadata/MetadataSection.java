package osu.beatmap.metadata;

import osu.beatmap.Section;
import util.BeatmapUtils;

public final class MetadataSection extends Section{
	private String title = "";
	private String titleUnicode = "";
	private String artist = "";
	private String artistUnicode = "";
	private String creator = "";
	private String version = "";
	private String source = "";
	private String tags = "";
	private int beatmapID = 0;
	private int beatmapSetID = -1;
	
	private static final String nl = BeatmapUtils.nl;
	
	public MetadataSection() {
		super("[Metadata]");
	}
	
	public void init(String[] lines) {
		title = lines[0].split("Title:")[1].trim();
		titleUnicode = lines[1].split("TitleUnicode:")[1].trim();
		artist = lines[2].split("Artist:")[1].trim();
		artistUnicode = lines[3].split("ArtistUnicode:")[1].trim();
		creator = lines[4].split("Creator:")[1].trim();
		version = lines[5].split("Version:")[1].trim();
		source = lines[6].split("Source:")[1].trim();
		tags = lines[7].split("Tags:")[1].trim();
		beatmapID = Integer.parseInt(lines[8].split(":")[1].trim());
		beatmapSetID = Integer.parseInt(lines[9].split(":")[1].trim());
	}
	
	
	public final String toString() {
		return  getHeader() + nl
				+ "Title:" + title + nl
				+ "TitleUnicode:" + titleUnicode + nl
				+ "Artist:" + artist + nl
				+ "ArtistUnicode:" + artistUnicode + nl
				+ "Creator:" + creator + nl
				+ "Version:" + version + nl
				+ "Source:" + source + nl
				+ "Tags:" + tags + nl
				+ "BeatmapID:" + beatmapID + nl
				+ "BeatmapSetID:" + beatmapSetID + nl;
	}
}
