package com.jojos.report.data;

/**
 * Enumerate the genres, N/A if not determined
 *
 * @author karanikasg@gmail.com
 */
public enum Genre {
    FEMALE("F"),
    MALE("M"),
    NA("N/A");

    private final String abbreviation;

    Genre(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static Genre forCaseInsensitiveAbbreviation(String abbreviation) {
        for (Genre genre : values()) {
            if (genre.abbreviation.toUpperCase().equals(abbreviation)) {
                return genre;
            }
        }
        return NA;
    }
}
