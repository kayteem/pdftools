package de.kayteem.lib.pdftools;

import de.kayteem.lib.pdftools.exceptions.DocumentNotAssignedException;
import de.kayteem.lib.pdftools.exceptions.LineIndexDoesNotExistException;
import de.kayteem.lib.pdftools.exceptions.StringPatternNotFoundException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Author:      Tobias Mielke
 * Created:     01.06.2018
 * Modified:    03.06.2018
 */
public class PDFReader implements IPDFReader {

    // DEPENDENCIES
    private final File pdfFile;


    // FIELDS
    private PDDocument pdfDoc;
    private String pdfString;
    private List<String> pdfLines;

    private PDFTextStripper textStripper;


    // CONSTRUCTION
    public PDFReader(File pdfFile) {
        this.pdfFile = pdfFile;
        this.pdfString = null;
        this.pdfLines = null;

        this.textStripper = null;

        // Achieves higher rendering speed with JDK8 and above.
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
    }


    // IMPLEMENTATION (Open, parse and close PDF document)
    public void open() throws IOException {
        pdfDoc = PDDocument.load(pdfFile);
    }

    public String parse() throws IOException, DocumentNotAssignedException {

        // [1] - Check pdfDoc.
        if (pdfDoc == null) {
            throw new DocumentNotAssignedException();
        }

        // [2] - Create text stripper.
        textStripper = new PDFTextStripper();

        // [3] - Retrieve string.
        pdfString = textStripper.getText(pdfDoc);

        // [4] - Populate lines.
        StringTokenizer tokenizer = new StringTokenizer(pdfString, textStripper.getLineSeparator());
        pdfLines = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            pdfLines.add(tokenizer.nextToken());
        }

        return pdfString;
    }

    public void close() throws IOException {
        if (pdfDoc != null) {
            pdfDoc.close();
        }
    }


    // IMPLEMENTATION (Retrieve occurrence)
    public int getOccurrences(String stringPattern) throws StringPatternNotFoundException {
        return getLinesContaining(stringPattern).size();
    }


    // IMPLEMENTATION (Retrieve line indices)
    public List<Integer> getIndicesOfLinesContaining(String stringPattern) {
        List<Integer> lineIndices = new ArrayList<>();

        for (int idx = 0; idx < pdfLines.size(); idx++) {
            String line = pdfLines.get(idx);

            if (line.contains(stringPattern)) {
                lineIndices.add(idx);
            }
        }

        return lineIndices;
    }

    public Integer getIndexOfLineContaining(String stringPattern, int occurrence) {
        return getIndicesOfLinesContaining(stringPattern).get(occurrence);
    }

    public Integer getIndexOfFirstLineContaining(String stringPattern) {
        return getIndexOfLineContaining(stringPattern, 1);
    }

    public Integer getIndexOfLastLineContaining(String stringPattern) {
        List<Integer> indices = getIndicesOfLinesContaining(stringPattern);
        int lastIdx = indices.size() - 1;

        return getIndexOfLineContaining(stringPattern, indices.get(lastIdx));
    }


    // IMPLEMENTATION (Retrieve lines)
    public List<String> getAllLines() {
        return pdfLines;
    }

    public List<String> getLines(int startIdx, int endIdx) throws LineIndexDoesNotExistException {

        // [1] - Check indices.
        if (startIdx >= pdfLines.size()) {
            throw new LineIndexDoesNotExistException(startIdx, pdfLines.size());
        }
        if (endIdx>= pdfLines.size()) {
            throw new LineIndexDoesNotExistException(endIdx, pdfLines.size());
        }

        List<String> lines = new ArrayList<>();
        for (int i = startIdx; i <= endIdx; i++) {
            lines.add(pdfLines.get(i));
        }

        return lines;
    }

    public String getLine(int lineIdx) throws LineIndexDoesNotExistException {

        // [1] - Check idx.
        if (lineIdx >= pdfLines.size()) {
            throw new LineIndexDoesNotExistException(lineIdx, pdfLines.size());
        }

        // [2] - Retrieve line.
        return pdfLines.get(lineIdx);
    }

    public List<String> getLinesContaining(String stringPattern) throws StringPatternNotFoundException {
        List<String> lines = new ArrayList<>();

        for (String line : pdfLines) {
            if (line.contains(stringPattern)) {
                lines.add(line);
            }
        }

        if (lines.isEmpty()) {
            throw new StringPatternNotFoundException(stringPattern);
        }

        return lines;
    }

    public String getLineContaining(String stringPattern, int occurrence) throws StringPatternNotFoundException {
        List<String> lines = getLinesContaining(stringPattern);

        if (occurrence > lines.size()) {
            throw new StringPatternNotFoundException(stringPattern, occurrence, lines.size());
        }

        return lines.get(occurrence - 1);
    }

    public String getFirstLineContaining(String stringPattern) throws StringPatternNotFoundException {
        return getLineContaining(stringPattern, 1);
    }

    public String getLastLineContaining(String stringPattern) throws StringPatternNotFoundException {
        List<String> lines = getLinesContaining(stringPattern);
        int lastIdx = lines.size() - 1;

        return lines.get(lastIdx);
    }


    // IMPLEMENTATION (Retrieve words)
    public List<String> getWordsOfLine(int lineIdx) throws LineIndexDoesNotExistException {
        String line = getLine(lineIdx);

        return getWordsInString(line);
    }

    public List<String> getWordsOfLineContaining(String stringPattern, int occurrence) throws StringPatternNotFoundException {
        String line = getLineContaining(stringPattern, occurrence);

        return getWordsInString(line);
    }

    public List<String> getWordsOfFirstLineContaining(String stringPattern) throws StringPatternNotFoundException {
        String line = getFirstLineContaining(stringPattern);

        return getWordsInString(line);
    }

    public List<String> getWordsOfLastLineContaining(String stringPattern) throws StringPatternNotFoundException {
        String line = getLastLineContaining(stringPattern);

        return getWordsInString(line);
    }


    // IMPLEMENTATION (Retrieve word)
    public String getWordOfLine(int lineIdx, int wordIdx) throws LineIndexDoesNotExistException {
        List<String> words = getWordsOfLine(lineIdx);

        return words.get(wordIdx);
    }

    public String getWordOfLineContaining(String stringPattern, int occurrence, int wordIdx) throws StringPatternNotFoundException {
        return getWordsOfLineContaining(stringPattern, occurrence).get(wordIdx);
    }

    public String getWordOfFirstLineContaining(String stringPattern, int wordIdx) throws StringPatternNotFoundException {
        return getWordsOfFirstLineContaining(stringPattern).get(wordIdx);
    }

    public String getWordOfLastLineContaining(String stringPattern, int wordIdx) throws StringPatternNotFoundException {
        return getWordsOfLastLineContaining(stringPattern).get(wordIdx);
    }


    // HELPERS
    private List<String> getWordsInString(String str) {
        String[] words = str.trim().split("\\s+");

        return Arrays.asList(words);
    }

}
