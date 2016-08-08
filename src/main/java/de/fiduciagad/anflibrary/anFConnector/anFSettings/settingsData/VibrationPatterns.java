package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

/**
 * Created by Felix Schiefer on 17.12.2015.
 */
public class VibrationPatterns {

    long[] pattern1 = {0, 1000, 1000, 300, 200, 100, 500, 200, 100, 5000};
    long[] pattern2 = {0, 100, 1000, 300, 200, 100, 500, 200, 100, 500};
    long[] pattern3 = {50, 1000, 4000, 3000, 2000, 1000, 5000, 2000, 1000, 5000};
    long[][] patterns;

    public VibrationPatterns() {
        patterns = new long[][]{pattern1, pattern2, pattern3};
    }

    public long[] getVibrationPattern(int pattern) {
        return patterns[pattern];
    }
}
