package com.footballAPI.repository;

import com.footballAPI.entity.PlayerEntity;
import com.footballAPI.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    long countByTeam(TeamEntity teamEntity);
}
