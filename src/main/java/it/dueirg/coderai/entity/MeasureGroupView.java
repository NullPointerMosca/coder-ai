package it.dueirg.coderai.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "MEASURE_GROUP_VIEW")
public class MeasureGroupView {
    @Id
    @Column(name = "MG_ID")
    private String mgId;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "LAST_CONNECTION_DATE")
    private java.sql.Timestamp lastConnectionDate;

    @Column(name = "POD_SERIAL")
    private String podSerial;

    @Column(name = "DRIVER_TYPE")
    private String driverType;

    @Column(name = "SYSTEM_TITLE", nullable = false)
    private String systemTitle;

    @Column(name = "KEY_C", nullable = false)
    private String keyC;

    @Column(name = "COMMISIONED_STATUS")
    private String commisionedStatus;

    @Column(name = "FW_VERS_CODE")
    private String fwVersCode;

    @Column(name = "METER_SERIAL", nullable = false)
    private String meterSerial;

    @Column(name = "CODE_COMPANY")
    private String codeCompany;

    @Column(name = "ID_ENABLING_PROCESS")
    private Long idEnablingProcess;

    @Column(name = "WO_CLOSING_DATE")
    private java.sql.Timestamp woClosingDate;

    @Column(name = "PRODUCER_CODE", nullable = false)
    private String producerCode;

    @Column(name = "DIFF_DATE")
    private Integer diffDate;

    @Column(name = "DEVICE_TYPE_ID", nullable = false)
    private String deviceTypeId;

    @Column(name = "DIGIT_NUMBER", nullable = false)
    private Integer digitNumber;

    @Column(name = "TYPE_METER")
    private String typeMeter;

    @Column(name = "TYPE_COMM")
    private String typeComm;

    @Column(name = "MULTI_CHANNEL")
    private Character multiChannel;

    @Column(name = "MG_NOT_CONF")
    private Character mgNotConf;

    @Column(name = "LAST_BALANCING_DATE")
    private java.time.LocalDateTime lastBalancingDate;
    
    @Column(name = "LAST_BITRATE_DATE")
    private java.time.LocalDateTime lastBitrateDate;

}