package com.footballAPI.repository;

import com.footballAPI.entity.CompetitionEntity;
import com.footballAPI.entity.TeamCompetitionEntity;
import com.footballAPI.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TeamCompetitionRepository extends JpaRepository<TeamCompetitionEntity, Long> {

    ArrayList<TeamCompetitionEntity> findAllByCompetition(CompetitionEntity competitionEntity);
}
