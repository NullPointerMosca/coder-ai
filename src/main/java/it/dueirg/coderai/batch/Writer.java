package it.dueirg.coderai.batch;

import it.dueirg.coderai.entity.MmProperties;
import it.dueirg.coderai.repository.MmPropertiesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class Writer implements ItemWriter<ProcessedFiles> {

    private static final Logger logger = LoggerFactory.getLogger(Writer.class);

    @Autowired
    private MmPropertiesRepository mmPropertiesRepository;

    @Override
    public void write(List<? extends ProcessedFiles> items) throws Exception {
        logger.info("Inizio scrittura dei file elaborati");
        for (ProcessedFiles processedFiles : items) {
            Map<String, List<String>> processedLinesMap = processedFiles.getProcessedLines();
            Map<String, List<String>> errorLinesMap = processedFiles.getErrorLines();

            for (Map.Entry<String, List<String>> entry : processedLinesMap.entrySet()) {
                String fileName = entry.getKey();
                List<String> processedLines = entry.getValue();
                List<String> errorLines = errorLinesMap.get(fileName);

                logger.info("Scrittura del file: " + fileName);

                if (!processedLines.isEmpty()) {
                    String newFileName = generateFileName(processedLines.get(0), fileName);
                    MmProperties outputPathProperty = mmPropertiesRepository.findByPropName("massmarket.path.abstract.dc");
                    if (outputPathProperty == null) {
                        throw new RuntimeException("Output path property not found");
                    }
                    File newFile = new File(outputPathProperty.getPropValue() + "/DOWNLOAD/" + newFileName);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(newFile))) {
                        for (String line : processedLines) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                    logger.info("File di output creato: " + newFile.getAbsolutePath());
                }

                if (!errorLines.isEmpty()) {
                    String errorFileName = generateFileName(errorLines.get(0), fileName);
                    MmProperties errorPathProperty = mmPropertiesRepository.findByPropName("massmarket.path.abstract.dc");
                    if (errorPathProperty == null) {
                        throw new RuntimeException("Error path property not found");
                    }
                    File errorFile = new File(errorPathProperty.getPropValue() + "/ERROR/" + errorFileName);
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(errorFile))) {
                        for (String line : errorLines) {
                            writer.write(line);
                            writer.newLine();
                        }
                    }
                    logger.info("File di errore creato: " + errorFile.getAbsolutePath());
                }

                MmProperties donePathProperty = mmPropertiesRepository.findByPropName("trame.corrotte.path.file.in");
                if (donePathProperty == null) {
                    throw new RuntimeException("Done path property not found");
                }
                File doneFile = new File(donePathProperty.getPropValue() + "/DONE/" + generateFileName(processedLines.get(0), fileName));
                Files.move(new File(fileName).toPath(), doneFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.info("File originale spostato nella directory DONE: " + doneFile.getAbsolutePath());
            }
        }
        logger.info("Scrittura completata");
    }

    private String generateFileName(String line, String fileName) {
        String[] fields = line.split(";");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMM");
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH");

        String ddmm = now.format(dateFormatter);
        String yyyy = now.format(yearFormatter);
        String hh = now.format(hourFormatter);

        String[] fileNameParts = fileName.split("_");
        String serialDc = fileNameParts[4];

        return String.format("PeriodicReading_%s_%s_%s_%s_Urmet_da_log_NOK.cvs", ddmm, yyyy, hh, serialDc);
    }
}