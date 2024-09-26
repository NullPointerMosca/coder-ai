package it.dueirg.coderai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.dueirg.coderai.entity.MmProperties;

@Repository
public interface MmPropertiesRepository extends JpaRepository<MmProperties, String> {
}