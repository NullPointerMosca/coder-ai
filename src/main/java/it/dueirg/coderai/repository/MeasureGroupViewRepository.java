package it.dueirg.coderai.repository;

import it.dueirg.coderai.entity.MeasureGroupView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureGroupViewRepository extends JpaRepository<MeasureGroupView, String> {
    MeasureGroupView findBySystemTitle(String systemTitle);
}