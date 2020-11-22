package com.footballAPI.mapper;

import com.footballAPI.dto.CompetitionDto;
import com.footballAPI.entity.CompetitionEntity;

public class CompetitionMapper {


    public static CompetitionEntity mapCompetitionToEntity(CompetitionDto dto) {
        CompetitionEntity entity = CompetitionEntity.builder().id(dto.getId()).name(dto.getName()).areaName(dto.getArea().getName()).build();
        return entity;
    }

    public static CompetitionEntity mapCompetitionToEntitySimple(CompetitionDto dto) {
        CompetitionEntity entity = CompetitionEntity.builder().id(dto.getId()).build();
        return entity;
    }
}
