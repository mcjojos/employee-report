package com.jojos.report.data;

/**
 * enumeration that represents the age range with factor of ten.
 *
 * For simplicity we assume that the maximum life expectancy if no more than 100
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

    UNDEFINED(0, 0);

    private final int min;
    private final int max;

    AgeRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static AgeRange forAge(int age) {
        for (AgeRange ageRange : values()) {
            if (age <= ageRange.max) {
                return ageRange;
            }
        }
        return UNDEFINED;
    }
}
