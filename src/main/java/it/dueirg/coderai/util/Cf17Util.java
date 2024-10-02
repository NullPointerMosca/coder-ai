package it.dueirg.coderai.util;

import org.springframework.stereotype.Component;

import com.ericsson.device.driver.GenericData.TipoValidita;
import com.ericsson.device.driver.wmbus.commandRequest.DLMSReqOutParameters;
import com.ericsson.device.driver.wmbus.commandRequest.ICommandRequest;
import com.ericsson.device.driver.wmbus.periodicMessage.CompactFrame_17_Custom;
import com.ericsson.device.driver.wmbus.periodicMessage.IPeriodicMessage;
import com.ericsson.urm.device.manager.MMDriverWrapper;
import com.ericsson.urm.device.manager.initialzer.GenericMMDriverResponseParameter;
import com.ericsson.urm.util.AnagraficaUtil;
import com.ericsson.urm.util.CryptUtil;
import com.ericsson.urm.util.HexUtils;

import it.dueirg.coderai.entity.CommandDriverParameters;
import it.dueirg.coderai.entity.MeasureGroupView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Cf17Util {
    
    public CompactFrame_17_Custom parseAPDUOrg(MMDriverWrapper mmDriverWrapper) {
        CompactFrame_17_Custom oldCF95= null;
        try {
            System.setProperty("URM_HOME", "URM_HOME");
            IPeriodicMessage peridicMessageDriver = mmDriverWrapper.openSession(IPeriodicMessage.class, false);
            oldCF95 = peridicMessageDriver.getCompactFrame_trameCorrotte();
            if (oldCF95.getValidita().equals(TipoValidita.VALIDO)) {
                log.info("Ricevuta trama custom {" + oldCF95.getApduEncrypted() + "}");
            } else
            log.info("Trama originale non valida");
           
        } catch (Exception e) {
           log.error("Errore parse trama originale " + e, e.getMessage());
        }
        return oldCF95;
    }

    public String createNewCF_17(String oldCF95, MeasureGroupView measureGroupView, long framCounter) {
        String newCF17= null;
        try {
            log.info("Trama CF da criptare {" + oldCF95 + "}");
            CommandDriverParameters commandDriverParameters = getDriverParameter("DLMS_DC_URMET_STANDARD", measureGroupView.getSystemTitle(), null, measureGroupView.getKeyC(), measureGroupView.getKeyC(), measureGroupView.getFwVersCode(), framCounter, measureGroupView.getDriverType());
            if (commandDriverParameters != null) {
                MMDriverWrapper mmDriverWrapper = commandDriverParameters.getMmDriverWrapper();
                ICommandRequest peridicMessageDriver = mmDriverWrapper.openSession(ICommandRequest.class, false);
                DLMSReqOutParameters dlmsReqOutParameters = peridicMessageDriver.sendCustomCF17(oldCF95);
                log.info("Trama CF17 non criptata {" + HexUtils.bytesToHexString(dlmsReqOutParameters.getClearReqAPDU()) + "}");
                log.info("Trama CF17 criptata {" + HexUtils.bytesToHexString(dlmsReqOutParameters.getEncryptReqAPDU()) + "}");
                newCF17 = HexUtils.bytesToHexString(dlmsReqOutParameters.getEncryptReqAPDU());
                newCF17 = "7D000000000000000002808000"+newCF17;
            } else
            log.info("Impossibile ottnere i dati per la crittografia della trama");
        } catch (Exception e) {
            log.error("Errore crittografia nuova trama " + e);
        }
        return newCF17;
    }

    public CommandDriverParameters getDriverParameter(String dcDriverType, String macAddress, String producerCode, String encryptionKey, String authenticationKey, String firmware, long frameCounter, String meterDriverType) {
        CommandDriverParameters commandDriverParameters = null;
        try {
            int accOut = 10;
            Long umiNumber = 10L;
            CryptUtil cryptUtil = new CryptUtil();
            String ekDecrypt = cryptUtil.decrypt(encryptionKey);
            String akDecrypt = cryptUtil.decrypt(authenticationKey);
            GenericMMDriverResponseParameter parameters = new GenericMMDriverResponseParameter(ekDecrypt, macAddress, akDecrypt, accOut, frameCounter, umiNumber, producerCode, dcDriverType, firmware);
            log.info("Command parameters: " + parameters.toString());
            MMDriverManager mmDriverManager = new MMDriverManager();
            MMDriverWrapper mmDriverWrapper  = mmDriverManager.getMMRequestDriver(parameters, meterDriverType);
            if (mmDriverWrapper != null) {
                commandDriverParameters = new CommandDriverParameters(accOut, frameCounter, umiNumber, mmDriverWrapper, AnagraficaUtil.converterSystemTitle(macAddress, true), ekDecrypt, macAddress);
            } else {
                log.info("No MMDriverManager class: " + parameters.getSistemTitle());
            }
        } catch (Exception e) {
            log.error("[getDriverParameter] Exception: " + e.getMessage() + "," + e);
        }
        return commandDriverParameters;
    }
}
