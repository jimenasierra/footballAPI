package com.footballAPI.mapper;

import com.footballAPI.dto.TeamCompetitionDto;
import com.footballAPI.entity.CompetitionEntity;
import com.footballAPI.entity.TeamCompetitionEntity;
import com.footballAPI.entity.TeamEntity;

public class TeamCompetitionMapper {

    public static TeamCompetitionEntity mapTeamCompetitionToEntity(TeamCompetitionDto dto) {
        TeamEntity teamEntity = TeamMapperImpl.mapTeamToEntity(dto.getTeamDto());
        CompetitionEntity competitionEntity = CompetitionMapper.mapCompetitionToEntity(dto.getCompetitionDto());
        TeamCompetitionEntity entity = TeamCompetitionEntity.builder().competition(competitionEntity).team(teamEntity).build();
        return entity;
    }

}
