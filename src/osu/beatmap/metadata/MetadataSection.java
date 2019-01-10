package osu.beatmap.metadata;

import osu.beatmap.Key;
import osu.beatmap.Section;

public final class MetadataSection extends Section{
	
	private static final Key titleKey = new Key("Title",String.class);
	private static final Key titleUnicodeKey = new Key("TitleUnicode",String.class);
	private static final Key artistKey = new Key("Artist",String.class);
	private static final Key artistUnicodeKey = new Key("ArtistUnicode",String.class);
	private static final Key creatorKey = new Key("Creator",String.class);
	private static final Key versionKey = new Key("Version",String.class);
	private static final Key sourceKey = new Key("Source",String.class);
	private static final Key tagsKey = new Key("Tags",String.class);
	private static final Key beatmapIdKey = new Key("BeatmapID",Integer.class);
	private static final Key beatmapSetIdKey = new Key("BeatmapSetID",Integer.class);
	
	
	public MetadataSection() {
		super("[Metadata]");
		addProperty(titleKey, "title");
		addProperty(titleUnicodeKey, "unicode title");
		addProperty(artistKey,"artist");
		addProperty(artistUnicodeKey, "unicode artist");
		addProperty(creatorKey, "creator");
		addProperty(versionKey,"difficulty name");
		addProperty(sourceKey, "source");
		addProperty(tagsKey, "tags");
		addProperty(beatmapIdKey, 0);
		addProperty(beatmapSetIdKey, -1);
	}
	
	
	

}
