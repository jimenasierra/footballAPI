package com.footballAPI.mapper;

import com.footballAPI.dto.TeamDto;
import com.footballAPI.entity.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Stream;
import java.util.List;

@Mapper
public interface TeamMapper {
    TeamDto mapTeamEntityToDto(TeamEntity teamEntity);

    @Mapping(target = "areaName", source = "area.name")
    TeamEntity mapTeamDtoToEntity(TeamDto teamDto);

    List<TeamDto> teamsToTeamDtos(Stream<TeamEntity> teams);

}
