package com.footballAPI.dto;

import com.footballAPI.entity.CompetitionEntity;
import com.footballAPI.entity.TeamEntity;
import lombok.*;

import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantTeamsDto {
    private CompetitionEntity competition;
    private SeasonDto season;
    private List<TeamDto> teams;
}
