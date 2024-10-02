package it.dueirg.coderai.batch;

import it.dueirg.coderai.entity.MeasureGroupView;
import it.dueirg.coderai.entity.MeterProdCodeMapping;
import it.dueirg.coderai.repository.MeasureGroupViewRepository;
import it.dueirg.coderai.repository.MeterProdCodeMappingRepository;
import it.dueirg.coderai.util.Cf17Util;
import it.dueirg.coderai.util.MMDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ericsson.device.driver.wmbus.periodicMessage.CompactFrame_17_Custom;
import com.ericsson.urm.device.manager.MMDriverWrapper;
import com.ericsson.urm.device.manager.initialzer.GenericMMDriverResponseParameter;
import com.ericsson.urm.device.driver.wmbus.util.WMBusAddress;
import com.ericsson.urm.device.driver.wmbus.util.WMBusUtility;
import com.ericsson.urm.util.AnagraficaUtil;
import com.ericsson.urm.util.CryptUtil;
import com.ericsson.urm.util.HexUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Processor implements ItemProcessor<List<File>, ProcessedFiles> {

    @Autowired
    private MeasureGroupViewRepository measureGroupViewRepository;

    @Autowired
    private MeterProdCodeMappingRepository meterProdCodeMappingRepository;

    @Autowired
    private Cf17Util cf17Util;

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

/*                     String decryptedApdu = decryptApdu(apdu);
                    if (!isCf95(decryptedApdu)) {
                        fileErrorLines.add(line);
                        continue;
                    } */

                    if (measureGroupView != null) {
                        log.info("Procedo ad analizzare il contatore {" + measureGroupView.getMgId() + "}");
                        if (measureGroupView.getDriverType() == null) {
                    
                            String systemTitleNoUniTs = AnagraficaUtil.converterSystemTitle(macAddress, true);
                            WMBusAddress wmBusAddress = WMBusUtility
                                    .parseWSWMBusAddressStream(HexUtils.hexStringToBytes(systemTitleNoUniTs));
                    
                            MeterProdCodeMapping meterProdCodeMapping = meterProdCodeMappingRepository
                                    .findByVersionAndMappedProducerCode(
                                            String.format("%02X", wmBusAddress.getVersion()),
                                            wmBusAddress.getIdManufactureDevice())
                                    .get();
                            if (meterProdCodeMapping != null)
                                measureGroupView.setDriverType(meterProdCodeMapping.getDriverType());
                        }
                        CryptUtil cryptUtil = new CryptUtil();
                        if (measureGroupView.getDriverType() != null && measureGroupView.getKeyC() != null) {
                            String ekDecrypt = cryptUtil.decrypt(measureGroupView.getKeyC());
                            GenericMMDriverResponseParameter parameters = new GenericMMDriverResponseParameter(
                                    ekDecrypt, macAddress, ekDecrypt,
                                    HexUtils.hexStringToBytes(apdu.toUpperCase()), "DLMS_DC_ALGORAB");
                            MMDriverManager mmDriverManager = new MMDriverManager();
                            log.info("Contatore {" + measureGroupView.getMgId() + "}, con driverType {"
                                    + measureGroupView.getDriverType() + "} parameter {"
                                    + HexUtils.bytesToHexString(parameters.getApdu()) + "}");
                            MMDriverWrapper mmDriverWrapper = mmDriverManager
                                    .getMMResponseDriver(measureGroupView.getDriverType(), parameters);
                            if (mmDriverWrapper != null) {
                                CompactFrame_17_Custom cf95org = cf17Util.parseAPDUOrg(mmDriverWrapper);
                                if (cf95org != null) {
                                    log.info("Procedo alla creazione della CF17 custom");
                                    String newCF17 = cf17Util.createNewCF_17(cf95org.lineFormat(), measureGroupView,
                                            cf95org.getFrameCounter());
                                    if (newCF17 != null) {
                                        String newLine = String.join(";", macAddress, acquireDate, rssiApdu, newCF17, sts, conf);
                                        log.info(newLine);
                                        fileProcessedLines.add(newLine);
                                    } else
                                        log.info("CF17 non valida, scarto la lettura");
                                } else
                                    log.info("CF95 non valida, scarto la lettura");
                            } else
                                log.info("Nessun driver per l'analisi della trama originale, scarto la lettura");
                        } else
                            log.info("Chiave o driver non trovato scarto la lettura");
                    }
                   // return new ProcessedFiles(processedLines, errorLines);

              //      String cf17Apdu = reconstructCf17(decryptedApdu);
              //      String encryptedCf17Apdu = encryptAndAuthenticateCf17(cf17Apdu);
              //      String newLine = String.join(";", macAddress, acquireDate, rssiApdu, encryptedCf17Apdu, sts, conf);
              //      fileProcessedLines.add(newLine);
                }
            }

            processedLines.put(fileName, fileProcessedLines);
            errorLines.put(fileName, fileErrorLines);
        }

        return new ProcessedFiles(processedLines, errorLines);
    }
}