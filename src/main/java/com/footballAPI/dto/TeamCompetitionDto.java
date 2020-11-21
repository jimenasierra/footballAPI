package com.footballAPI.dto;

import lombok.*;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamCompetitionDto {
    private TeamDto teamDto;
    private CompetitionDto competitionDto;
}
