package server.model;

import lombok.Data;
import osu.beatmap.Beatmap;
import osu.beatmap.metadata.MetadataSection;

import java.util.SortedSet;
import java.util.TreeSet;

@Data
public class ModResponse {

    private String artist;
    private String title;
    private String mapper;
    private int beatmapSetId;
    private SortedSet<Mod> tabs = new TreeSet<>();
    private String hitsoundDiff;

    public ModResponse(Beatmap beatmap) {
        MetadataSection metadata = beatmap.getMetadataSection();
        setArtist(metadata.getProperty(MetadataSection.artistUnicodeKey).toString());
        setTitle(metadata.getProperty(MetadataSection.titleKey).toString());
        setMapper(metadata.getProperty(MetadataSection.creatorKey).toString());
        setBeatmapSetId((int) metadata.getProperty(MetadataSection.beatmapSetIdKey));
        setHitsoundDiff(metadata.getProperty(MetadataSection.versionKey).toString());
    }

    public String getUrl() {
        return "https://osu.ppy.sh/beatmapsets/" + beatmapSetId + "/#mania";
    }

    public void addTab(Mod mod) {
        tabs.add(mod);
    }

}
