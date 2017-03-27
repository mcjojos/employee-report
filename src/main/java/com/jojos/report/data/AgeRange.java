package com.jojos.report.data;

/**
 * enumeration that represents the age range with factor of ten.
 *
 * Assumed maximum life expectancy if not more than 129 years old.
 *
 * For each age-range assume that the lower limit is inclusive and the upper is exclusive,
 * e.g.:
 * YEAR_30_40 is translated as 30 inclusive until 40 exclusive.
 *
 * @author karanikasg@gmail.com
 */
public enum AgeRange {

    YEAR_0_10(0, 9),
    YEAR_10_20(10, 19),
    YEAR_20_30(20, 29),
    YEAR_30_40(30, 39),
    YEAR_40_50(40, 49),
    YEAR_50_60(50, 59),
    YEAR_60_70(60, 69),
    YEAR_70_80(70, 79),
    YEAR_80_90(80, 89),
    YEAR_90_100(90, 99),
    YEAR_100_110(100, 109),
    YEAR_110_120(110, 119),
    YEAR_120_130(120, 129);

    private final int min;
    private final int max;

    AgeRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return min + "-" + max;
    }

    public static AgeRange forAge(int age) {
        for (AgeRange ageRange : values()) {
            if (age <= ageRange.max) {
                return ageRange;
            }
        }
        return null;
    }
}
