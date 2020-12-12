package com.footballAPI.mapper;

import com.footballAPI.dto.PlayerDto;
import com.footballAPI.entity.PlayerEntity;

public class PlayerMapper {

    public static PlayerEntity mapTeamToEntity(PlayerDto dto) {
        PlayerEntity entity = PlayerEntity.builder().id(dto.getId()).name(dto.getName()).position(dto.getPosition()).dateOfBirth(dto.getDateOfBirth()).countryOfBirth(dto.getCountryOfBirth()).nationality(dto.getNationality()).team(TeamMapperImpl.mapTeamToEntitySimple(dto.getTeam())).build();
        return entity;
    }

}
