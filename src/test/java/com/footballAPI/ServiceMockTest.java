package com.footballAPI;

import com.footballAPI.dto.CompetitionDto;
import com.footballAPI.dto.ResponseDto;
import com.footballAPI.entity.CompetitionEntity;
import com.footballAPI.entity.PlayerEntity;
import com.footballAPI.entity.TeamCompetitionEntity;
import com.footballAPI.entity.TeamEntity;
import com.footballAPI.repository.CompetitionRepository;
import com.footballAPI.repository.PlayerRepository;
import com.footballAPI.repository.TeamCompetitionRepository;
import com.footballAPI.repository.TeamRepository;
import com.footballAPI.service.FootballAPIService;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ServiceMockTest {

    @Mock
    private CompetitionRepository competitionRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private TeamCompetitionRepository teamCompetitionRepository;
    @Mock
    private PlayerRepository playerRepository;

    @Test
    public void testLoadCompetition() {
        FootballAPIService footballAPIServiceMock = mock(FootballAPIService.class);
        ResponseDto responseDto = new ResponseDto("Successfully imported", -1);
        when(footballAPIServiceMock.loadCompetition("2001")).thenReturn(responseDto);
        // Si no tengo que retornar nada entonces que pongo?
        when(competitionRepository.save(any(CompetitionEntity.class))).thenReturn(null);
        verify(competitionRepository).save(any(CompetitionEntity.class));
    }

    @Test
    public void testLoadCompetitionTeams() {
        FootballAPIService footballAPIServiceMock = mock(FootballAPIService.class);
        CompetitionDto competitionDto = new CompetitionDto();
        ResponseDto responseDto = new ResponseDto("Successfully imported", -1);
        when(footballAPIServiceMock.loadCompetitionTeams("2001", competitionDto)).thenReturn(responseDto);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(null);
        when(teamCompetitionRepository.saveAll(anyListOf(TeamCompetitionEntity.class))).thenReturn(null);
        verify(teamRepository).save(any(TeamEntity.class));
        verify(teamCompetitionRepository).save(any(TeamCompetitionEntity.class));
    }

    @Test
    public void testLoadTeamPlayers() {
        FootballAPIService footballAPIServiceMock = mock(FootballAPIService.class);
        ResponseDto responseDto = new ResponseDto("Successfully imported", -1);
        when(footballAPIServiceMock.loadTeamPlayers("668")).thenReturn(responseDto);
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(null);
        verify(playerRepository).save(any(PlayerEntity.class));
    }
}
