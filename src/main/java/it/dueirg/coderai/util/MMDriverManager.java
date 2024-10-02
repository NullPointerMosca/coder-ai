package it.dueirg.coderai.util;

import org.springframework.stereotype.Component;

import com.ericsson.device.driver.GenericDriverType.DriverType;
import com.ericsson.device.driver.exceptions.DriverException;
import com.ericsson.device.driver.wmbus.util.DLMSResponseInParameters;
import com.ericsson.urm.device.driver.wmbus.WMBUSReponseStream;
import com.ericsson.urm.device.driver.wmbus.WMBUSRequestStream;
import com.ericsson.urm.device.driver.wmbus.util.WMBusAddress;
import com.ericsson.urm.device.driver.wmbus.util.WMBusUtility;
import com.ericsson.urm.device.driver.wmbus.util.enums.VendorType;
import com.ericsson.urm.device.manager.MMDriverWrapper;
import com.ericsson.urm.device.manager.initialzer.GenericMMDriverResponseParameter;
import com.ericsson.urm.dlmsMM.meter.request.DlmsMMRequestCommand;
import com.ericsson.urm.dlmsMM.meter.response.DlmsMMResponseCommand;
import com.ericsson.urm.dlmsMM.meter.utils.DLMSRequestInParameters;
import com.ericsson.urm.util.AnagraficaUtil;
import com.ericsson.urm.util.HexUtils;
import com.ericsson.urm.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MMDriverManager {
	
/**
 * Restituisce il Driver corretto, in base alla tipologia di DriverType, per la gestione della Risposta dal contatore
 * 
 * @param driverType Tipo di Driver associato al contatore
 * @param parameters Parametri generici per l'inizializzaizone del driver
 * @return Istanza della classe MMDriverMapper
 * @throws DriverException
 */

	public MMDriverWrapper getMMResponseDriver(String mgDriverType, GenericMMDriverResponseParameter parameters) throws DriverException {
		MMDriverWrapper ret = null;
		try {
			String systemTitle = AnagraficaUtil.converterSystemTitle(parameters.getSistemTitle(), true);
			if (mgDriverType != null) {
				if (mgDriverType != null && mgDriverType.equals(MgDriverType.TypeId.WMBUS_AEM.name())) {
					WMBUSReponseStream responseStream = new WMBUSReponseStream(parameters.getApdu(), parameters.getKey_c(), systemTitle, VendorType.aemmeter, parameters.getDcDriverType(), parameters.getPm1ResponseStatus());
					ret = new MMDriverWrapper(DriverType.AEM_WMBUS,responseStream);
				} else if (mgDriverType != null && mgDriverType.equals(MgDriverType.TypeId.WMBUS_WT_SMM.name())) {
					WMBUSReponseStream responseStream = new WMBUSReponseStream(parameters.getApdu(), parameters.getKey_c(), systemTitle, VendorType.watertech, parameters.getDcDriverType(), parameters.getPm1ResponseStatus());
					ret = new MMDriverWrapper(DriverType.SMM_WMBUS,responseStream);
				} else if (mgDriverType != null && mgDriverType.equals(MgDriverType.TypeId.WMBUS_LANDIS.name())) {
					WMBUSReponseStream responseStream = new WMBUSReponseStream(parameters.getApdu(), parameters.getKey_c(), systemTitle, VendorType.landismeter, parameters.getDcDriverType(), parameters.getPm1ResponseStatus());
					ret = new MMDriverWrapper(DriverType.LANDIS_WMBUS,responseStream);
				} else if (mgDriverType != null && mgDriverType.equals(MgDriverType.TypeId.SMG_DLMS_WMBUS.name())) {
					DLMSResponseInParameters dlmsResponseInGetParameters = new DLMSResponseInParameters();
					dlmsResponseInGetParameters.setAuthenticationKey(parameters.getAuthenticationKey());
					dlmsResponseInGetParameters.setDriverType(DriverType.SMG_DLMS_WMBUS);
					dlmsResponseInGetParameters.setEncryptionKey(parameters.getKey_c());
					dlmsResponseInGetParameters.setWmbus_address(systemTitle);
					dlmsResponseInGetParameters.setDcDriverType(parameters.getDcDriverType());
					DlmsMMResponseCommand dlmsMMResponseCommand = new DlmsMMResponseCommand(DriverType.SMG_DLMS_WMBUS, dlmsResponseInGetParameters, parameters.getApdu());
					ret = new MMDriverWrapper(DriverType.SMG_DLMS_WMBUS,dlmsMMResponseCommand);
				} else if (mgDriverType != null && mgDriverType.equals(MgDriverType.TypeId.AEM_DLMS_WMBUS.name())) {
					DLMSResponseInParameters dlmsResponseInGetParameters = new DLMSResponseInParameters();
					dlmsResponseInGetParameters.setAuthenticationKey(parameters.getAuthenticationKey());
					dlmsResponseInGetParameters.setDriverType(DriverType.AEM_DLMS_WMBUS);
					dlmsResponseInGetParameters.setEncryptionKey(parameters.getKey_c());
					dlmsResponseInGetParameters.setWmbus_address(systemTitle);
					dlmsResponseInGetParameters.setDcDriverType(parameters.getDcDriverType());
					DlmsMMResponseCommand dlmsMMResponseCommand = new DlmsMMResponseCommand(DriverType.AEM_DLMS_WMBUS, dlmsResponseInGetParameters, parameters.getApdu());
					ret = new MMDriverWrapper(DriverType.AEM_DLMS_WMBUS,dlmsMMResponseCommand);
				} else if (mgDriverType != null && mgDriverType.equals(MgDriverType.TypeId.MIT_DLMS_WMBUS.name())) {
					DLMSResponseInParameters dlmsResponseInGetParameters = new DLMSResponseInParameters();
					dlmsResponseInGetParameters.setAuthenticationKey(parameters.getAuthenticationKey());
					dlmsResponseInGetParameters.setDriverType(DriverType.MIT_DLMS_WMBUS);
					dlmsResponseInGetParameters.setEncryptionKey(parameters.getKey_c());
					dlmsResponseInGetParameters.setWmbus_address(systemTitle);
					dlmsResponseInGetParameters.setDcDriverType(parameters.getDcDriverType());
					DlmsMMResponseCommand dlmsMMResponseCommand = new DlmsMMResponseCommand(DriverType.MIT_DLMS_WMBUS, dlmsResponseInGetParameters, parameters.getApdu());
					ret = new MMDriverWrapper(DriverType.MIT_DLMS_WMBUS,dlmsMMResponseCommand);
				} else if (mgDriverType != null && mgDriverType.equals(MgDriverType.TypeId.WMBUS_METERSIT.name())) {
					WMBUSReponseStream responseStream = new WMBUSReponseStream(parameters.getApdu(), parameters.getKey_c(), systemTitle, VendorType.metersitmeter, parameters.getDcDriverType(), parameters.getPm1ResponseStatus());
					ret = new MMDriverWrapper(DriverType.MTS_WMBUS,responseStream);
				} else if (mgDriverType != null && mgDriverType.equals(MgDriverType.TypeId.MTS_DLMS_WMBUS.name())) {
					DLMSResponseInParameters dlmsResponseInGetParameters = new DLMSResponseInParameters();
					dlmsResponseInGetParameters.setAuthenticationKey(parameters.getAuthenticationKey());
					dlmsResponseInGetParameters.setDriverType(DriverType.MTS_DLMS_WMBUS);
					dlmsResponseInGetParameters.setEncryptionKey(parameters.getKey_c());
					dlmsResponseInGetParameters.setWmbus_address(systemTitle);
					dlmsResponseInGetParameters.setDcDriverType(parameters.getDcDriverType());
					DlmsMMResponseCommand dlmsMMResponseCommand = new DlmsMMResponseCommand(DriverType.MTS_DLMS_WMBUS, dlmsResponseInGetParameters, parameters.getApdu());
					ret = new MMDriverWrapper(DriverType.MTS_DLMS_WMBUS,dlmsMMResponseCommand);
				} else if (mgDriverType != null && mgDriverType.equals(MgDriverType.TypeId.MTS_HYB_DLMS_WMBUS.name())) {
					DLMSResponseInParameters dlmsResponseInGetParameters = new DLMSResponseInParameters();
					dlmsResponseInGetParameters.setAuthenticationKey(parameters.getAuthenticationKey());
					dlmsResponseInGetParameters.setDriverType(DriverType.MTS_HYB_DLMS_WMBUS);
					dlmsResponseInGetParameters.setEncryptionKey(parameters.getKey_c());
					dlmsResponseInGetParameters.setWmbus_address(systemTitle);
					dlmsResponseInGetParameters.setDcDriverType(parameters.getDcDriverType());
					DlmsMMResponseCommand dlmsMMResponseCommand = new DlmsMMResponseCommand(DriverType.MTS_HYB_DLMS_WMBUS, dlmsResponseInGetParameters, parameters.getApdu());
					ret = new MMDriverWrapper(DriverType.MTS_HYB_DLMS_WMBUS,dlmsMMResponseCommand);
				}
			} else {
				log.info("MeterProdCodMapping not found about WMBUS: " + parameters.getSistemTitle());
			}
		} catch (Exception e) {
			log.error("getMMResponseDriver.Exception: " + e.getMessage() + e);
		} 
		return ret;
	}

	public MMDriverWrapper getMMRequestDriver(GenericMMDriverResponseParameter parameters, String driverType) {
        MMDriverWrapper ret = null;
        try {
// Devo convertire il system title in formato non UNI-TS    
            WMBusAddress wmBusAddress = WMBusUtility.parseWSWMBusAddressStream(HexUtils.hexStringToBytes(AnagraficaUtil.converterSystemTitle(parameters.getSistemTitle(), false)));
            if (driverType.equals(MgDriverType.TypeId.WMBUS_AEM.name())) {
                WMBUSRequestStream requestStream = new WMBUSRequestStream(wmBusAddress.getMacAddress(), wmBusAddress.getIdManufactureDevice(), VendorType.aemmeter, parameters.getKey_c(), parameters.getAcc_out(), wmBusAddress.getVersion(), null, parameters.getDcDriverType());
                ret = new MMDriverWrapper(DriverType.AEM_WMBUS,requestStream, false);
            } else if (driverType.equals(MgDriverType.TypeId.WMBUS_LANDIS.name())) {
                Long umiNumber = (parameters.getUmiNumber() != null ? parameters.getUmiNumber() : null);
                WMBUSRequestStream requestStream = new WMBUSRequestStream(wmBusAddress.getMacAddress(), wmBusAddress.getIdManufactureDevice(), VendorType.landismeter, parameters.getKey_c(), parameters.getAcc_out(), wmBusAddress.getVersion(), umiNumber, parameters.getDcDriverType());
                ret = new MMDriverWrapper(DriverType.LANDIS_WMBUS,requestStream, false);
            } else if (driverType.equals(MgDriverType.TypeId.WMBUS_METERSIT.name())) {
                Long umiNumber = (parameters.getUmiNumber() != null ? parameters.getUmiNumber() : null);
                WMBUSRequestStream requestStream = new WMBUSRequestStream(wmBusAddress.getMacAddress(), wmBusAddress.getIdManufactureDevice(), VendorType.metersitmeter, parameters.getKey_c(), parameters.getAcc_out(), wmBusAddress.getVersion(), umiNumber, parameters.getDcDriverType());
                ret = new MMDriverWrapper(DriverType.MTS_WMBUS,requestStream, false);
            } else if (driverType.equals(MgDriverType.TypeId.WMBUS_WT_SMM.name())) {
                WMBUSRequestStream requestStream = new WMBUSRequestStream(wmBusAddress.getMacAddress(), wmBusAddress.getIdManufactureDevice(), VendorType.watertech, parameters.getKey_c(), parameters.getAcc_out(), wmBusAddress.getVersion(), null, parameters.getDcDriverType());
                ret = new MMDriverWrapper(DriverType.SMM_WMBUS,requestStream, false);
            } else if (driverType.equals(MgDriverType.TypeId.MIT_DLMS_WMBUS.name())) {
                DLMSRequestInParameters dlmsRequestInGetParameters = new DLMSRequestInParameters();
                dlmsRequestInGetParameters.setAuthenticationKey(parameters.getAuthenticationKey());
                dlmsRequestInGetParameters.setAccessNumber(parameters.getAcc_out());
                dlmsRequestInGetParameters.setDriverType(DriverType.MIT_DLMS_WMBUS);
                dlmsRequestInGetParameters.setEncryptionKey(parameters.getKey_c());
                String fc = Long.toHexString(parameters.getFrameCounter());
                dlmsRequestInGetParameters.setFrameCounter(StringUtil.normalizeLeft(fc, "0", 8));
                dlmsRequestInGetParameters.setFirmWare(parameters.getFirmwareVersion());
                dlmsRequestInGetParameters.setWmbus_address(AnagraficaUtil.converterSystemTitle(parameters.getSistemTitle(), true));
                DlmsMMRequestCommand dlmsMMRequestCommand = new DlmsMMRequestCommand(DriverType.MIT_DLMS_WMBUS, dlmsRequestInGetParameters);
                ret = new MMDriverWrapper(DriverType.MIT_DLMS_WMBUS,dlmsMMRequestCommand, true);
            } else if (driverType.equals(MgDriverType.TypeId.AEM_DLMS_WMBUS.name())) {
                DLMSRequestInParameters dlmsRequestInGetParameters = new DLMSRequestInParameters();
                dlmsRequestInGetParameters.setAuthenticationKey(parameters.getAuthenticationKey());
                dlmsRequestInGetParameters.setAccessNumber(parameters.getAcc_out());
                dlmsRequestInGetParameters.setDriverType(DriverType.AEM_DLMS_WMBUS);
                dlmsRequestInGetParameters.setEncryptionKey(parameters.getKey_c());
                String fc = Long.toHexString(parameters.getFrameCounter());
                dlmsRequestInGetParameters.setFirmWare(parameters.getFirmwareVersion());
                dlmsRequestInGetParameters.setFrameCounter(StringUtil.normalizeLeft(fc, "0", 8));
                dlmsRequestInGetParameters.setWmbus_address(AnagraficaUtil.converterSystemTitle(parameters.getSistemTitle(), true));
                DlmsMMRequestCommand dlmsMMRequestCommand = new DlmsMMRequestCommand(DriverType.AEM_DLMS_WMBUS, dlmsRequestInGetParameters);
                ret = new MMDriverWrapper(DriverType.AEM_DLMS_WMBUS,dlmsMMRequestCommand, true);
            } else if (driverType.equals(MgDriverType.TypeId.SMG_DLMS_WMBUS.name())) {
                DLMSRequestInParameters dlmsRequestInGetParameters = new DLMSRequestInParameters();
                dlmsRequestInGetParameters.setAuthenticationKey(parameters.getAuthenticationKey());
                dlmsRequestInGetParameters.setAccessNumber(parameters.getAcc_out());
                dlmsRequestInGetParameters.setDriverType(DriverType.SMG_DLMS_WMBUS);
                dlmsRequestInGetParameters.setEncryptionKey(parameters.getKey_c());
                String fc = Long.toHexString(parameters.getFrameCounter());
                dlmsRequestInGetParameters.setFirmWare(parameters.getFirmwareVersion());
                dlmsRequestInGetParameters.setFrameCounter(StringUtil.normalizeLeft(fc, "0", 8));
                dlmsRequestInGetParameters.setFirmWare(parameters.getFirmwareVersion() != null ? parameters.getFirmwareVersion() : null);
                dlmsRequestInGetParameters.setWmbus_address(AnagraficaUtil.converterSystemTitle(parameters.getSistemTitle(), false));
                DlmsMMRequestCommand dlmsMMRequestCommand = new DlmsMMRequestCommand(DriverType.SMG_DLMS_WMBUS, dlmsRequestInGetParameters);
                ret = new MMDriverWrapper(DriverType.SMG_DLMS_WMBUS,dlmsMMRequestCommand, true);
            } else if (driverType.equals(MgDriverType.TypeId.MTS_DLMS_WMBUS.name())) {
                DLMSRequestInParameters dlmsRequestInGetParameters = new DLMSRequestInParameters();
                dlmsRequestInGetParameters.setAuthenticationKey(parameters.getAuthenticationKey());
                dlmsRequestInGetParameters.setAccessNumber(parameters.getAcc_out());
                dlmsRequestInGetParameters.setDriverType(DriverType.MTS_DLMS_WMBUS);
                dlmsRequestInGetParameters.setEncryptionKey(parameters.getKey_c());
                String fc = Long.toHexString(parameters.getFrameCounter());
                dlmsRequestInGetParameters.setFirmWare(parameters.getFirmwareVersion());
                dlmsRequestInGetParameters.setFrameCounter(StringUtil.normalizeLeft(fc, "0", 8));
                dlmsRequestInGetParameters.setWmbus_address(AnagraficaUtil.converterSystemTitle(parameters.getSistemTitle(), true));
                DlmsMMRequestCommand dlmsMMRequestCommand = new DlmsMMRequestCommand(DriverType.MTS_DLMS_WMBUS, dlmsRequestInGetParameters);
                ret = new MMDriverWrapper(DriverType.MTS_DLMS_WMBUS,dlmsMMRequestCommand, true);
            }
        } catch (Exception e) {
            log.error("getMMRequestDriver.Exception: " + e.getMessage() + "," + e);
        }
        return ret;
    }

}
