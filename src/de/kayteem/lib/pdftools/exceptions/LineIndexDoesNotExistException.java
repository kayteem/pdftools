package de.kayteem.lib.pdftools.exceptions;


/**
 * Author:      Tobias Mielke
 * Created:     01.06.2018
 * Modified:    01.06.2018
 */
public class LineIndexDoesNotExistException extends Exception {

    public LineIndexDoesNotExistException(int lineIdx, int linesSize) {
        super(String.format("The requested line %d does not exist (total lines = %d)", lineIdx, linesSize));
    }

}
