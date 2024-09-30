-- schema.sql

-- DDL for MeasureGroupView
CREATE TABLE IF NOT EXISTS MEASURE_GROUP_VIEW (
  MG_ID VARCHAR(255) PRIMARY KEY,
  STATUS VARCHAR(255) NOT NULL,
  LAST_CONNECTION_DATE TIMESTAMP,
  POD_SERIAL VARCHAR(255),
  DRIVER_TYPE VARCHAR(255),
  SYSTEM_TITLE VARCHAR(255) NOT NULL,
  KEY_C VARCHAR(255) NOT NULL,
  COMMISIONED_STATUS VARCHAR(255),
  FW_VERS_CODE VARCHAR(255),
  METER_SERIAL VARCHAR(255) NOT NULL,
  CODE_COMPANY VARCHAR(255),
  ID_ENABLING_PROCESS BIGINT,
  WO_CLOSING_DATE TIMESTAMP,
  PRODUCER_CODE VARCHAR(255) NOT NULL,
  DIFF_DATE INTEGER,
  DEVICE_TYPE_ID VARCHAR(255) NOT NULL,
  DIGIT_NUMBER INTEGER NOT NULL,
  TYPE_METER VARCHAR(255),
  TYPE_COMM VARCHAR(255),
  MULTI_CHANNEL CHAR(1),
  MG_NOT_CONF CHAR(1),
  LAST_BALANCING_DATE TIMESTAMP,
  LAST_BITRATE_DATE TIMESTAMP
);

-- DDL for MeterProdCodeMapping
CREATE TABLE IF NOT EXISTS METER_PROD_CODE_MAPPING (
  PRODUCER_CODE VARCHAR(4) NOT NULL,
  MAPPED_PRODUCER_CODE VARCHAR(4) NOT NULL,
  DESCRIPTION VARCHAR(100),
  DRIVER_TYPE VARCHAR(30),
  MANUFACTURER_CODE VARCHAR(5),
  VERSION VARCHAR(3) DEFAULT '0' NOT NULL,
  REVERSE_ST CHAR(1) DEFAULT 'N' NOT NULL,
  CHECK ("PRODUCER_CODE" IS NOT NULL),
  CHECK ("MAPPED_PRODUCER_CODE" IS NOT NULL)
);

-- DDL for MmProperties
CREATE TABLE IF NOT EXISTS MM_PROPERTIES (
  PROP_NAME VARCHAR(100) NOT NULL,
  DESCRIPTION VARCHAR(100),
  PROP_TYPE VARCHAR(200),
  PROP_VALUE VARCHAR(200),
  PRIMARY KEY (PROP_NAME)
);