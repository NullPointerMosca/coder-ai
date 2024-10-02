package it.dueirg.coderai.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.dueirg.coderai.entity.MeterProdCodeMapping;

@Repository
public interface MeterProdCodeMappingRepository extends JpaRepository<MeterProdCodeMapping, String> {
    Optional<MeterProdCodeMapping> findByVersionAndMappedProducerCode(String version, String mappedProducerCode);
    MeterProdCodeMapping findByMappedProducerCodeAndVersion(String mappedProducerCode, String version);
}