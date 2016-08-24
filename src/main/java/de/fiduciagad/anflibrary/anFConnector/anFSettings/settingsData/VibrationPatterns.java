package de.fiduciagad.anflibrary.anFConnector.anFSettings.settingsData;

/**
 * In this class Vibration Patterns for signals are implemented. In Further Versions of the
 * framework more named patterns will be added
 */
public class VibrationPatterns {

    // TODO FS Name them!
    private static final long[] pattern1 = {0, 1000, 1000, 300, 200, 100, 500, 200, 100, 5000};
    private static final long[] pattern2 = {0, 100, 1000, 300, 200, 100, 500, 200, 100, 500};
    private static final long[] pattern3 = {50, 1000, 4000, 3000, 2000, 1000, 5000, 2000, 1000, 5000};

    private static final long[][] patterns = new long[][]{pattern1, pattern2, pattern3};

    /**
     * With this function you can receive a defined vibration pattern
     *
     * @param pattern The position of the pattern in the array
     * @return The choosen decision pattern
     */
    public long[] getVibrationPattern(int pattern) {
        return patterns[pattern];
    }
}
