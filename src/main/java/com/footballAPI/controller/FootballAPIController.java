package com.footballAPI.controller;

import com.footballAPI.dto.ResponseDto;
import com.footballAPI.exception.LeagueAllReadyImportedException;
import com.footballAPI.exception.NotFoundException;
import com.footballAPI.exception.ServerErrorException;
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
    public ResponseEntity importLeague(String leagueCode) {
        try {
            ResponseDto responseDto = footballAPIService.loadCompetition(leagueCode);
            return new ResponseEntity(responseDto.getMessage(), HttpStatus.CREATED);
        }catch (LeagueAllReadyImportedException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
        } catch (NotFoundException ex){
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        }catch (ServerErrorException ex){
            return new ResponseEntity(ex.getMessage() , HttpStatus.GATEWAY_TIMEOUT);
        }
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
    public ResponseEntity totalPlayers(String leagueCode) {
        try {
            ResponseEntity responseEntity = footballAPIService.totalPlayers(leagueCode);
            return responseEntity;
        } catch (NotFoundException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ServerErrorException ex) {
            return new ResponseEntity(ex.getMessage() , HttpStatus.GATEWAY_TIMEOUT);
        }
    }

    @GetMapping("try")
    public void tryMapper(String teamCode) {
        footballAPIService.tryMapper(teamCode);
    }

}
