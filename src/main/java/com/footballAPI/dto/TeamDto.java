package com.footballAPI.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamDto {
    private Long id;
    private String name;
    private String tla;
    private String shortName;
    private AreaDto area;
    private String email;
    private List<PlayerDto> squad;
}
