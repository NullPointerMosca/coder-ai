package it.dueirg.coderai.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.File;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import it.dueirg.coderai.entity.MmProperties;
import it.dueirg.coderai.repository.MmPropertiesRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CoderaiUtils {

    private MmPropertiesRepository mmPropertiesRepository;

    public CoderaiUtils(MmPropertiesRepository mmPropertiesRepository) {
        this.mmPropertiesRepository = mmPropertiesRepository;
    }

    public interface CSVConvertible {
        String toCSV();
    }

    public String toCSV(Object item) throws NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (item == null) {
            return "";
        }

        if (item instanceof CSVConvertible) {
            return ((CSVConvertible) item).toCSV();
        }

        // Fall back to default implementation
        return defaultToCSV(item);
    }

    public String defaultToCSV(Object item)
            throws NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (item == null) {
            return "";
        }

        StringBuilder csvBuilder = new StringBuilder();
        Field[] fields = item.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String getterName = "get" + StringUtils.capitalize(fieldName);
            try {
                Method getter = item.getClass().getMethod(getterName);
                Object value = getter.invoke(item);
                csvBuilder.append(value != null ? value.toString() : "");

                if (i < fields.length - 1) {
                    csvBuilder.append(";");
                }
            } catch (IllegalAccessException e) {
                // Handle the exception as needed, e.g., log it or throw a runtime exception
                log.error("Errore nella conversione in CSV {}", e);
            }
        }

        return csvBuilder.toString();
    }

    public void saveDiscardedRowsForFile(List<String> discardedRows, String stepName) {
        MmProperties pathProperty = mmPropertiesRepository.findByPropName("massmarket.path.abstract.dc");
        if (pathProperty == null || StringUtils.isEmpty(pathProperty.getPropValue())) {
            throw new IllegalStateException("Path property not found or empty");
        }
        String errorPath = pathProperty.getPropValue() + "/error";

        // Generate file name with the required format
        String currentDate = java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        String fileName = String.format("PeriodicReading_%s_%s_Urmet_da_log_NOK.csv", currentDate, stepName);
        String filePath = errorPath + File.separator + fileName;
        log.info("Sono nel discarded rows, size lista {}", discardedRows.size());
        try {
            if (!discardedRows.isEmpty()) {
                Files.createDirectories(Paths.get(errorPath));
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    for (String row : discardedRows) {
                        writer.write(row);
                        writer.newLine();
                    }
                }
            }
            log.info("Discarded rows for " + stepName + " saved to: " + filePath);
        } catch (IOException e) {
            log.error("Error saving discarded rows for " + stepName + ": " + e.getMessage());
        }
    }
}
