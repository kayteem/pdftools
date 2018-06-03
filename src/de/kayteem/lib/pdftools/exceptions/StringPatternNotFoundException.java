package de.kayteem.lib.pdftools.exceptions;


/**
 * Author:      Tobias Mielke
 * Created:     01.06.2018
 * Modified:    03.06.2018
 */
public class StringPatternNotFoundException extends Exception {

    public StringPatternNotFoundException(String stringPattern, int requestedOccurrence, int found) {
        super(String.format(
                "Occurrence %d of string pattern \"%s\" requested (found %d times)",
                requestedOccurrence,
                stringPattern,
                found
        ));
    }

    public StringPatternNotFoundException(String stringPattern) {
        super(String.format(
                "String pattern \"%s\" not found!", stringPattern
        ));
    }

}
