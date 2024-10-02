package it.dueirg.coderai.util;

import java.io.Serializable;


public class MgDriverType implements Serializable {
	private static final long serialVersionUID = 1L;
	public static enum TypeId {
		CTR_CPL("CTR_CPL", true), DLMS_V1("DLMS_V1", false),DLMS_V1_COMPACT("DLMS_V1_COMPACT",false),DLMS_V1_ECORA("DLMS_V1_ECORA",false),
		DLMS_ELSTER_EK280("DLMS_ELSTER_EK280",false),DLMS_ELSTER_BK("DLMS_ELSTER_BK",false),DLMS_AEM("DLMS_AEM",false),CTR_ELSTER_EK155("CTR_ELSTER_EK155", true), 
		CTR_ELSTER("CTR_ELSTER", true), CTR_FIORENTINI("CTR_FIORENTINI", true), CTR_TECNOSYSTEM("CTR_TECNOSYSTEM", true), CTR_V9("CTR_V9", true),
		CTR_CPL_ECOR4("CTR_CPL_ECOR4", true),CTR_SACOFGAS("CTR_SACOFGAS", true),DLMS_METERSIT("DLMS_METERSIT", false),
		WMBUS_AEM("WMBUS_AEM", false),WMBUS_WT_SMM("WMBUS_WT_SMM", false),WMBUS_LANDIS("WMBUS_LANDIS", false),WMBUS_METERSIT("WMBUS_METERSIT", false),
		MIT_DLMS_WMBUS("MIT_DLMS_WMBUS", false),SMG_DLMS_WMBUS("SMG_DLMS_WMBUS", false),MTS_DLMS_WMBUS("MTS_DLMS_WMBUS", false), MTS_HYB_DLMS_WMBUS("MTS_HYB_DLMS_WMBUS", false),AEM_DLMS_WMBUS("AEM_DLMS_WMBUS", false);	
		
		private String value = null;
		private boolean ctrDriver=false;

		private TypeId(String value,boolean ctrDriver) {
			this.value = value;
			this.ctrDriver=ctrDriver;
		}

		public String getValue() {
			return value;
		}

		public boolean isCtrDriver() {
			return ctrDriver;
		}

		public static TypeId parse(String value) {
			TypeId ret = null;
			if (CTR_CPL.value.equals(value)) {
				ret = CTR_CPL;
			} else if (DLMS_V1.value.equals(value)) {
				ret = DLMS_V1;
			} else if (CTR_ELSTER.value.equals(value)) {
				ret = CTR_ELSTER;
			} else if (CTR_FIORENTINI.value.equals(value)) {
				ret = CTR_FIORENTINI;
			} else if (CTR_TECNOSYSTEM.value.equals(value)) {
				ret = CTR_TECNOSYSTEM;
			} else if (CTR_V9.value.equals(value)) {
				ret = CTR_V9;
			} else if (DLMS_V1_COMPACT.value.equals(value)) {
				ret = DLMS_V1_COMPACT;
			} else if (CTR_ELSTER_EK155.value.equals(value)) {
				ret = CTR_ELSTER_EK155;
			} else if (CTR_CPL_ECOR4.value.equals(value)) {
				ret = CTR_CPL_ECOR4;
			} else if (DLMS_ELSTER_EK280.value.equals(value)) {
				ret = DLMS_ELSTER_EK280;
			} else if (DLMS_ELSTER_BK.value.equals(value)) {
				ret = DLMS_ELSTER_BK;
			}else if (DLMS_V1_ECORA.value.equals(value)) {
				ret = DLMS_V1_ECORA;
			}else if (CTR_SACOFGAS.value.equals(value)) {
				ret = CTR_SACOFGAS;
			}else if (DLMS_METERSIT.value.equals(value)) {
				ret = DLMS_METERSIT;
			}else if (DLMS_AEM.value.equals(value)) {
				ret = DLMS_AEM;
			}else if (SMG_DLMS_WMBUS.value.equals(value)) {
				ret = SMG_DLMS_WMBUS;
			}else if (MTS_DLMS_WMBUS.value.equals(value)) {
				ret = MTS_DLMS_WMBUS;
			}else if (AEM_DLMS_WMBUS.value.equals(value)) {
				ret = AEM_DLMS_WMBUS;
			}else if (MIT_DLMS_WMBUS.value.equals(value)) {
				ret = MIT_DLMS_WMBUS;
			}else if (WMBUS_AEM.value.equals(value)) {
				ret = WMBUS_AEM;
			}else if (WMBUS_WT_SMM.value.equals(value)) {
				ret = WMBUS_WT_SMM;
			}else if (WMBUS_LANDIS.value.equals(value)) {
				ret = WMBUS_LANDIS;
			}else if (WMBUS_METERSIT.value.equals(value)) {
				ret = WMBUS_METERSIT;
			}
			else
				throw new IllegalArgumentException("Invalid value:[" + value + "]");

			return ret;
		}

	}

}
