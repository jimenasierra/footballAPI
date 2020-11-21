package com.footballAPI.dto;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionDto {
    private Long id;
    private String name;
    private String code;
    private AreaDto area;
    private SeasonDto season;
}
