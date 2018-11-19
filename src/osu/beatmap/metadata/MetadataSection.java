package osu.beatmap.metadata;

import osu.beatmap.Property;
import osu.beatmap.Section;

public final class MetadataSection extends Section{
	
	private static final String titleKey = "Title";
	private static final String titleUnicodeKey = "TitleUnicode";
	private static final String artistKey = "Artist";
	private static final String artistUnicodeKey = "ArtistUnicode";
	private static final String creatorKey = "Creator";
	private static final String versionKey = "Version";
	private static final String sourceKey = "Source";
	private static final String tagsKey = "Tags";
	
	
	private static final String beatmapIdKey = "BeatmapID";
	private static final String beatmapSetIdKey = "BeatmapSetID";
	
	
	public MetadataSection() {
		super("[Metadata]");
		keys.add(titleKey);
		keys.add(titleUnicodeKey);
		keys.add(artistKey);
		keys.add(artistUnicodeKey);
		keys.add(creatorKey);
		keys.add(versionKey);
		keys.add(sourceKey);
		keys.add(tagsKey);
		keys.add(beatmapIdKey);
		keys.add(beatmapSetIdKey);
		
		properties.put(beatmapIdKey, new Property(0));
		properties.put(beatmapSetIdKey, new Property(-1));
	}
	
	
	

}
