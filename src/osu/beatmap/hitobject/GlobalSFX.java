package osu.beatmap.hitobject;

/**
 * Check if an audio file is a globally used SFX (not specific to a given
 * difficulty)
 *
 * @author DH
 * @see <a href="https://osu.ppy.sh/wiki/en/Skinning/Sounds#gameplay">Skin Wiki</a>
 */

public enum GlobalSFX {
    COUNT("count"),
    COUNT1("count1s"),
    COUNT2("count2s"),
    COUNT3("count3s"),
    GO("gos"),
    READY("readys"),
    COMBO_BREAK("combobreak"),
    COMBO_BURST("comboburst"),
    FAIL_SOUND("failsound"),
    SECTION_PASS("sectionpass"),
    SECTION_FAIL("sectionfail"),
    APPLAUSE("applause"),
    PAUSE_LOOP("pause-loop");

    private final String text;

    GlobalSFX(String text) {
        this.text = text;
    }

    public static boolean isGlobalSFX(String filename) {
        String nameWithoutExt = filename.substring(0, filename.lastIndexOf("."));
        for (GlobalSFX e : GlobalSFX.values()) {
            if (nameWithoutExt.equals(e.text)) {
                return true;
            }
        }
        return false;

    }

}
