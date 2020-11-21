package com.footballAPI.repository;

import com.footballAPI.entity.CompetitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitionRepository extends JpaRepository <CompetitionEntity, Long>{
    List<CompetitionEntity> findAll();

    CompetitionEntity save(CompetitionEntity competitionEntity);
}
