package it.dueirg.coderai.batch;

import it.dueirg.coderai.entity.MmProperties;
import it.dueirg.coderai.repository.MmPropertiesRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Reader implements ItemReader<List<File>> {

    
    private static final Logger logger = LoggerFactory.getLogger(Reader.class);

    @Autowired
    private MmPropertiesRepository mmPropertiesRepository;

    private List<List<File>> fileGroups;
    private int nextGroupIndex;

    @Override
    public List<File> read() throws Exception {
        if (fileGroups == null) {
            MmProperties pathProperty = mmPropertiesRepository.findByPropName("trame.corrotte.path.file.in");
            MmProperties capProperty = mmPropertiesRepository.findByPropName("trame.corrotte.file.cap");
            MmProperties groupProperty = mmPropertiesRepository.findByPropName("trame.corrotte.file.group");

            if (pathProperty == null) {
                throw new RuntimeException("Path property not found");
            }
            if (capProperty == null) {
                throw new RuntimeException("Cap property not found");
            }
            if (groupProperty == null) {
                throw new RuntimeException("Group property not found");
            }

            File directory = new File(pathProperty.getPropValue() + "/DOWNLOAD");
            File[] allFiles = directory.listFiles((dir, name) -> name.matches("PeriodicReading_\\d{4}_\\d{4}_\\d{2}_\\w+_Urmet_da_log_NOK\\.csv"));
           // File[] allFiles = directory.listFiles();
            if (allFiles == null) {
                throw new RuntimeException("No files found in directory");
            }

            logger.info("Found " + allFiles.length + " files in directory: " + directory.getAbsolutePath());

            // Sort files by last modified date (oldest first)
            List<File> sortedFiles = Arrays.stream(allFiles)
                    .sorted(Comparator.comparingLong(File::lastModified))
                    .collect(Collectors.toList());

                    logger.info("Sorted files: " + sortedFiles.size());

            String capValue = capProperty.getPropValue();
            if (!"ALL".equalsIgnoreCase(capValue)) {
                int cap = Integer.parseInt(capValue);
                sortedFiles = sortedFiles.stream().limit(cap).collect(Collectors.toList());
            }

            logger.info("Sorted files: " + sortedFiles.size());

            int groupSize = Integer.parseInt(groupProperty.getPropValue());
            fileGroups = new ArrayList<>();
            for (int i = 0; i < sortedFiles.size(); i += groupSize) {
                fileGroups.add(sortedFiles.subList(i, Math.min(i + groupSize, sortedFiles.size())));
            }

            nextGroupIndex = 0;
        }

        List<File> nextGroup = null;

        if (nextGroupIndex < fileGroups.size()) {
            nextGroup = fileGroups.get(nextGroupIndex);
            nextGroupIndex++;
        }

        return nextGroup;
    }
}