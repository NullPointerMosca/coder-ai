package it.dueirg.coderai.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "METER_PROD_CODE_MAPPING")
public class MeterProdCodeMapping {

    @Id
    @Column(name = "PRODUCER_CODE", nullable = false, length = 4)
    private String producerCode;

    @Column(name = "MAPPED_PRODUCER_CODE", nullable = false, length = 4)
    private String mappedProducerCode;

    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @Column(name = "DRIVER_TYPE", length = 30)
    private String driverType;

    @Column(name = "MANUFACTURER_CODE", length = 5)
    private String manufacturerCode;

    @Column(name = "VERSION", nullable = false, length = 3, columnDefinition = "varchar(3) default '0'")
    private String version;

    @Column(name = "REVERSE_ST", nullable = false, length = 1, columnDefinition = "char(1) default 'N'")
    private String reverseSt;
}