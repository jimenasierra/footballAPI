package com.footballAPI.mapper;

import com.footballAPI.dto.AreaDto;
import com.footballAPI.dto.TeamDto;
import com.footballAPI.entity.TeamEntity;

import java.util.List;
import java.util.stream.Stream;


public class TeamMapperImpl implements TeamMapper{


    public static TeamEntity mapTeamToEntity(TeamDto dto) {
        TeamEntity entity = TeamEntity.builder().id(dto.getId()).name(dto.getName()).tla(dto.getTla()).shortName(dto.getShortName()).areaName(dto.getArea().getName()).email(dto.getEmail()).build();
        return entity;
    }

    public static TeamEntity mapTeamToEntitySimple(TeamDto dto) {
        TeamEntity entity = TeamEntity.builder().id(dto.getId()).build();
        return entity;
    }

    public static TeamDto mapTeamToDto(TeamEntity entity) {
        AreaDto areaDto = new AreaDto();
        areaDto.setName(entity.getAreaName());
        TeamDto dto = TeamDto.builder().id(entity.getId()).name(entity.getName()).tla(entity.getTla()).shortName(entity.getShortName()).area(areaDto).email(entity.getEmail()).build();
        return dto;
    }

    @Override
    public TeamDto mapTeamEntityToDto(TeamEntity teamEntity) {
        return null;
    }

    @Override
    public TeamEntity mapTeamDtoToEntity(TeamDto teamDto) {
        return null;
    }

    @Override
    public List<TeamDto> teamsToTeamDtos(Stream<TeamEntity> teams) {
        return null;
    }
}
