package com.footballAPI.repository;

import antlr.collections.List;
import com.footballAPI.entity.PlayerEntity;
import com.footballAPI.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    long countByTeam(TeamEntity teamEntity);

    ArrayList<PlayerEntity> findAllByTeam_IdIn(ArrayList<Long> ids);

}
