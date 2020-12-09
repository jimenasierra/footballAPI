package com.footballAPI;

import com.footballAPI.dto.CompetitionDto;
import com.footballAPI.dto.ResponseDto;
import com.footballAPI.service.FootballAPIService;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServiceMockTest {

    @Test
    public void testLoadCompetition() {
        FootballAPIService footballAPIServiceMock = mock(FootballAPIService.class);
        ResponseDto responseDto = new ResponseDto(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Successfully imported", -1);
        when(footballAPIServiceMock.loadCompetition("2001")).thenReturn(responseDto);
    }

    @Test
    public void testLoadCompetitionTeams(){
        FootballAPIService footballAPIServiceMock = mock(FootballAPIService.class);
        CompetitionDto competitionDto = new CompetitionDto();
        ResponseDto responseDto = new ResponseDto(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Successfully imported", -1);
        when(footballAPIServiceMock.loadCompetitionTeams("2001",competitionDto)).thenReturn(responseDto);
    }

    @Test
    public void testLoadTeamPlayers(){
        FootballAPIService footballAPIServiceMock = mock(FootballAPIService.class);
        ResponseDto responseDto = new ResponseDto(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Successfully imported", -1);
        when(footballAPIServiceMock.loadTeamPlayers("668")).thenReturn(responseDto);
    }
}
