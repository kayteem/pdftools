package de.kayteem.lib.pdftools.exceptions;


/**
 * Author:      Tobias Mielke
 * Created:     01.06.2018
 * Modified:    01.06.2018
 */
public class DocumentNotAssignedException extends RuntimeException {

    public DocumentNotAssignedException() {
        super("pdfDoc is not assigned. Call PDFReader.open() first!");
    }

}
