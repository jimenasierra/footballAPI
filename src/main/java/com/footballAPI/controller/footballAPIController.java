package com.footballAPI.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.footballAPI.dto.ResponseDto;
import com.footballAPI.dto.TotalDto;
import com.footballAPI.service.FootballAPIService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class footballAPIController {

    private final FootballAPIService footballAPIService;

    public footballAPIController(FootballAPIService footballAPIService) {
        this.footballAPIService = footballAPIService;
    }

    @GetMapping("import-league")
    public ResponseDto importLeague(String leagueCode) {return footballAPIService.loadCompetition(leagueCode);
    }

    @GetMapping("import-league-teams")
    public ResponseDto importLeagueTeams(String leagueCode) {return footballAPIService.loadCompetitionTeams(leagueCode, null);
    }

    @GetMapping("import-league-players")
    public ResponseDto importLeaguePlayers(String teamCode) {return footballAPIService.loadTeamPlayers(teamCode);
    }


    @GetMapping("total-players")
    public ResponseDto totalPlayers(String leagueCode) {return footballAPIService.totalPlayers(leagueCode);
    }

}
