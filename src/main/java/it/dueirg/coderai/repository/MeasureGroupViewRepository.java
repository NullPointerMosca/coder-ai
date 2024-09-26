package it.dueirg.coderai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.dueirg.coderai.entity.MeasureGroupView;

@Repository
public interface MeasureGroupViewRepository extends JpaRepository<MeasureGroupView, String> {
}