package com.footballAPI.controller;

import com.footballAPI.dto.ResponseDto;
import com.footballAPI.service.FootballAPIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class FootballAPIController {

    private final FootballAPIService footballAPIService;

    public FootballAPIController(FootballAPIService footballAPIService) {
        this.footballAPIService = footballAPIService;
    }


    /**/


    @GetMapping("import-league")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto importLeague(String leagueCode) {
        String message = "";
        ResponseDto responseDto = footballAPIService.loadCompetition(leagueCode);
//        String message = footballAPIService.loadCompetition(leagueCode);
        if (message.equals("Successfully imported")) {
            ResponseEntity.ok().body(responseDto);
        }

        return footballAPIService.loadCompetition(leagueCode);
    }

    @GetMapping("import-league-teams")
    public ResponseDto importLeagueTeams(String leagueCode) {
        return footballAPIService.loadCompetitionTeams(leagueCode, null);
    }

    @GetMapping("import-league-players")
    public ResponseDto importLeaguePlayers(String teamCode) {
        return footballAPIService.loadTeamPlayers(teamCode);
    }


    @GetMapping("total-players")
    public ResponseDto totalPlayers(String leagueCode) {
        return footballAPIService.totalPlayers(leagueCode);
    }

}
