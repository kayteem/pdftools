package de.kayteem.lib.pdftools;


import de.kayteem.lib.pdftools.exceptions.DocumentNotAssignedException;
import de.kayteem.lib.pdftools.exceptions.LineIndexDoesNotExistException;
import de.kayteem.lib.pdftools.exceptions.StringPatternNotFoundException;

import java.io.IOException;
import java.util.List;


/**
 * Author:      Tobias Mielke
 * Created:     01.06.2018
 * Modified:    03.06.2018
 */
public interface IPDFReader {

    // Open, parse and close PDF document
    void open() throws IOException;
    String parse() throws IOException, DocumentNotAssignedException;
    void close() throws IOException;

    // Retrieve occurrence
    int getOccurrences(String stringPattern) throws StringPatternNotFoundException;

    // Retrieve line indices.
    List<Integer> getIndicesOfLinesContaining(String stringPattern);
    Integer getIndexOfLineContaining(String stringPattern, int occurrence);
    Integer getIndexOfFirstLineContaining(String stringPattern);
    Integer getIndexOfLastLineContaining(String stringPattern);

    // Retrieve lines.
    List<String> getAllLines();
    List<String> getLines(int startIdx, int endIdx) throws LineIndexDoesNotExistException;
    String getLine(int lineIdx) throws LineIndexDoesNotExistException;
    List<String> getLinesContaining(String stringPattern) throws StringPatternNotFoundException;
    String getLineContaining(String stringPattern, int occurrence) throws StringPatternNotFoundException;
    String getFirstLineContaining(String stringPattern) throws StringPatternNotFoundException;
    String getLastLineContaining(String stringPattern) throws StringPatternNotFoundException;

    // Retrieve words
    List<String> getWordsOfLine(int lineIdx) throws LineIndexDoesNotExistException;
    List<String> getWordsOfLineContaining(String stringPattern, int occurrence) throws StringPatternNotFoundException;
    List<String> getWordsOfFirstLineContaining(String stringPattern) throws StringPatternNotFoundException;
    List<String> getWordsOfLastLineContaining(String stringPattern) throws StringPatternNotFoundException;

    // Retrieve word
    String getWordOfLine(int lineIdx, int wordIdx) throws LineIndexDoesNotExistException;
    String getWordOfLineContaining(String stringPattern, int occurrence, int wordIdx) throws StringPatternNotFoundException;
    String getWordOfFirstLineContaining(String stringPattern, int wordIdx) throws StringPatternNotFoundException;
    String getWordOfLastLineContaining(String stringPattern, int wordIdx) throws StringPatternNotFoundException;

}
