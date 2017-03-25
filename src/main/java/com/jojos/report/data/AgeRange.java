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

    YEAR_0_10,
    YEAR_10_20,
    YEAR_20_30,
    YEAR_30_40,
    YEAR_40_50,
    YEAR_50_60,
    YEAR_60_70,
    YEAR_70_80,
    YEAR_80_90,
    YEAR_90_100;

}
