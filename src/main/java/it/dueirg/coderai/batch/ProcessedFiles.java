package it.dueirg.coderai.batch;

import java.util.List;
import java.util.Map;

public class ProcessedFiles {
    private Map<String, List<String>> processedLines;
    private Map<String, List<String>> errorLines;

    public ProcessedFiles(Map<String, List<String>> processedLines, Map<String, List<String>> errorLines) {
        this.processedLines = processedLines;
        this.errorLines = errorLines;
    }

    public Map<String, List<String>> getProcessedLines() {
        return processedLines;
    }

    public Map<String, List<String>> getErrorLines() {
        return errorLines;
    }
}