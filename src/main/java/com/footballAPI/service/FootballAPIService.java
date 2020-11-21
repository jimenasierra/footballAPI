package com.footballAPI.service;

import com.footballAPI.dto.*;
import com.footballAPI.entity.CompetitionEntity;
import com.footballAPI.entity.PlayerEntity;
import com.footballAPI.entity.TeamCompetitionEntity;
import com.footballAPI.entity.TeamEntity;
import com.footballAPI.mapper.CompetitionMapper;
import com.footballAPI.mapper.PlayerMapper;
import com.footballAPI.mapper.TeamCompetitionMapper;
import com.footballAPI.mapper.TeamMapper;
import com.footballAPI.repository.CompetitionRepository;
import com.footballAPI.repository.PlayerRepository;
import com.footballAPI.repository.TeamCompetitionRepository;
import com.footballAPI.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FootballAPIService {

    private final CompetitionRepository competitionRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final TeamCompetitionRepository teamCompetitionRepository;

    public FootballAPIService(CompetitionRepository competitionRepository, PlayerRepository playerRepository, TeamRepository teamRepository, TeamCompetitionRepository teamCompetitionRepository) {
        this.competitionRepository = competitionRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.teamCompetitionRepository = teamCompetitionRepository;
    }

    private HttpEntity<String> headers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-AUTH-TOKEN", "e34185592a4546899c8a7372c6a5ee42");

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return entity;
    }

//    @Async("threadPoolTaskExecutor")
    public ResponseDto loadCompetition(String leagueCode) {
        final String uri = "http://api.football-data.org/v2/competitions/" + leagueCode;
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity entity = headers();

        try {
            ResponseEntity<CompetitionDto> result = restTemplate.exchange(uri, HttpMethod.GET, entity, CompetitionDto.class);

            CompetitionDto competitionDto = result.getBody();

            // League not null, means exists
            if (competitionDto != null){
                CompetitionEntity competitionEntity = CompetitionMapper.mapCompetitionToEntity(competitionDto);

                // League already imported
                if (competitionRepository.existsById(competitionEntity.getId())){
                    return new ResponseDto(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), "League already imported", -1);
                }
                // League not imported
                else {
                    competitionRepository.save(competitionEntity);
                    loadCompetitionTeams(leagueCode, competitionDto);
                    /*try {
                        loadCompetitionTeams(leagueCode, competitionDto);
                    } catch (Exception ex) {
                        return new ResponseDto(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT.value(), "Server error", -1);
                    }*/

                    return new ResponseDto(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Successfully imported", -1);
                }
            } else {
                return new ResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Not found", -1);
            }
        } catch (HttpClientErrorException ex){
            return new ResponseDto(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT.value(), "Server error", -1);
        }


    }

//    @Async("threadPoolTaskExecutor")
    public ResponseDto loadCompetitionTeams(String leagueCode, CompetitionDto competitionDto) {
        final String uri = "http://api.football-data.org/v2/competitions/" + leagueCode + "/teams";
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity entity = headers();

        ResponseEntity<ParticipantTeamsDto> result = restTemplate.exchange(uri, HttpMethod.GET, entity, ParticipantTeamsDto.class);

        if (result.getBody() != null) {
            List<TeamDto> teamDtos = result.getBody().getTeams();
            List<TeamCompetitionEntity> teamCompetitionEntities = new ArrayList<TeamCompetitionEntity>();
            List<TeamEntity> teamEntities = new ArrayList<TeamEntity>();
            for (TeamDto teamDto : teamDtos) {

                TeamCompetitionDto teamCompetitionDto = new TeamCompetitionDto(teamDto, competitionDto);
                TeamCompetitionEntity teamCompetitionEntity = TeamCompetitionMapper.mapTeamCompetitionToEntity(teamCompetitionDto);
                teamCompetitionEntities.add(teamCompetitionEntity);

                TeamEntity teamEntity = TeamMapper.mapTeamToEntity(teamDto);
                teamEntities.add(teamEntity);
                teamRepository.save(teamEntity);

                loadTeamPlayers(String.valueOf(teamDto.getId()));
                try {
                    TimeUnit.SECONDS.sleep(60);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return new ResponseDto(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT.value(), "Server error", -1);
                }
            }
            teamCompetitionRepository.saveAll(teamCompetitionEntities);
            return new ResponseDto(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Successfully imported", -1);
        }else {
            return new ResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Not found", -1);
        }
    }

//    @Async("threadPoolTaskExecutor")
    public ResponseDto loadTeamPlayers(String teamCode) {
        final String uri = "http://api.football-data.org/v2/teams/" + teamCode ;
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity entity = headers();
        ResponseEntity<TeamDto> result = restTemplate.exchange(uri, HttpMethod.GET, entity, TeamDto.class);

        if (result.getBody() != null) {
            List<PlayerDto> playerDtos = result.getBody().getSquad();
            List<PlayerEntity> playerEntities = new ArrayList<>();
            for (PlayerDto playerDto  : playerDtos) {
                TeamDto teamDto = new TeamDto();
                teamDto.setId(Long.parseLong(teamCode));
                playerDto.setTeam(teamDto);
                PlayerEntity playerEntity = PlayerMapper.mapTeamToEntity(playerDto);
                playerEntities.add(playerEntity);
            }
            playerRepository.saveAll(playerEntities);
            return new ResponseDto(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Successfully imported", -1);
        }else {
            return new ResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Not found", -1);
        }

    }

    public ResponseDto totalPlayers(String leagueCode){

        try {
            CompetitionDto competitionDto = new CompetitionDto();
            competitionDto.setId(Long.parseLong(leagueCode));
            CompetitionEntity competitionEntity = CompetitionMapper.mapCompetitionToEntitySimple(competitionDto);

            List<TeamCompetitionEntity> teamCompetitionEntities = teamCompetitionRepository.findAllByCompetition(competitionEntity);

            long count = 0L;

            for (TeamCompetitionEntity teamCompetitionEntity : teamCompetitionEntities) {
                count = count + playerRepository.countByTeam(teamCompetitionEntity.getTeam());
            }

            if (count != 0) {
                return new ResponseDto(HttpStatus.OK, HttpStatus.OK.value(), "",count);
            } else {
                return new ResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "",count);
            }
        }catch (Exception ex){
            return new ResponseDto(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT.value(), "Server error", -1);
        }
    }
}
