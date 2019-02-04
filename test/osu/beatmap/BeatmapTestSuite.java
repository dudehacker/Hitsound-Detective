package osu.beatmap;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import osu.beatmap.difficulty.TestDifficultySection;
import osu.beatmap.editor.TestEditorSection;
import osu.beatmap.event.TestEventSection;
import osu.beatmap.general.TestGeneralSection;
import osu.beatmap.general.TestMode;
import osu.beatmap.hitobject.TestHitObject;
import osu.beatmap.hitobject.TestHitObjectSection;
import osu.beatmap.hitobject.TestHitsoundType;
import osu.beatmap.metadata.TestMetadataSection;
import osu.beatmap.timing.TestTimingSection;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   TestBeatmap.class,
   
   TestDifficultySection.class,
   TestEditorSection.class,
   TestEventSection.class,
   TestGeneralSection.class,
   TestMode.class,
   TestHitObjectSection.class,
   TestHitObject.class,
   TestMetadataSection.class,
   TestTimingSection.class,
   TestHitsoundType.class
   
})


public class BeatmapTestSuite {

}
