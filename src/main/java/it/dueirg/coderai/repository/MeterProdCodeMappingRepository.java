package it.dueirg.coderai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.dueirg.coderai.entity.MeterProdCodeMapping;

@Repository
public interface MeterProdCodeMappingRepository extends JpaRepository<MeterProdCodeMapping, String> {
}