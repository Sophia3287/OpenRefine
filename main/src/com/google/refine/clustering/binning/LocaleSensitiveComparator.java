package com.google.refine.clustering.binning;

import java.text.Collator;
import java.util.Locale;
import org.apache.tika.language.LanguageIdentifier;

public class LocaleSensitiveComparator {

    /**
     * Detects the most likely Locale of the input text.
     *
     * @param text The text to analyze.
     * @return A Locale that represents the detected language.
     */
    public static Locale detectLocale(String text) {
        LanguageIdentifier identifier = new LanguageIdentifier(text);
        String languageCode = identifier.getLanguage();
        return new Locale(languageCode);
    }

    /**
     * Compares two strings based on their content language and specified strength.
     *
     * @param s1       The first string to compare.
     * @param s2       The second string to compare.
     * @param strength The strength of comparison (primary, secondary, tertiary).
     * @return An integer representing the comparison result.
     */
    public static int compareStrings(String s1, String s2, int strength) {
        Locale locale = detectLocale(s1 + " " + s2);  // Combine strings to improve language detection accuracy
        Collator collator = Collator.getInstance(locale);
        collator.setStrength(strength);
        return collator.compare(s1, s2);
    }

}

