package it.dueirg.coderai.batch;

import it.dueirg.coderai.entity.MeasureGroupView;
import it.dueirg.coderai.repository.MeasureGroupViewRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Processor implements ItemProcessor<List<File>, ProcessedFiles> {

    @Autowired
    private MeasureGroupViewRepository measureGroupViewRepository;

    @Override
    public ProcessedFiles process(List<File> files) throws Exception {
        Map<String, List<String>> processedLines = new HashMap<>();
        Map<String, List<String>> errorLines = new HashMap<>();

        for (File file : files) {
            String fileName = file.getName();
            List<String> fileProcessedLines = new ArrayList<>();
            List<String> fileErrorLines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(";");
                    if (fields.length != 6) {
                        fileErrorLines.add(line);
                        continue;
                    }

                    String macAddress = fields[0];
                    String acquireDate = fields[1];
                    String rssiApdu = fields[2];
                    String apdu = fields[3];
                    String sts = fields[4];
                    String conf = fields[5];

                    MeasureGroupView measureGroupView = measureGroupViewRepository.findBySystemTitle(macAddress);
                    if (measureGroupView == null) {
                        fileErrorLines.add(line);
                        continue;
                    }

                    String decryptedApdu = decryptApdu(apdu);
                    if (!isCf95(decryptedApdu)) {
                        fileErrorLines.add(line);
                        continue;
                    }

                    String cf17Apdu = reconstructCf17(decryptedApdu);
                    String encryptedCf17Apdu = encryptAndAuthenticateCf17(cf17Apdu);
                    String newLine = String.join(";", macAddress, acquireDate, rssiApdu, encryptedCf17Apdu, sts, conf);
                    fileProcessedLines.add(newLine);
                }
            }

            processedLines.put(fileName, fileProcessedLines);
            errorLines.put(fileName, fileErrorLines);
        }

        return new ProcessedFiles(processedLines, errorLines);
    }

    private String decryptApdu(String apdu) {
        // Implementa la logica di decifratura qui
        return apdu; // Placeholder
    }

    private boolean isCf95(String apdu) {
        // Implementa la logica di verifica qui
        return true; // Placeholder
    }

    private String reconstructCf17(String apdu) {
        // Implementa la logica di ricostruzione qui
        return apdu; // Placeholder
    }

    private String encryptAndAuthenticateCf17(String apdu) {
        // Implementa la logica di crittografia e autenticazione qui
        return apdu; // Placeholder
    }
}