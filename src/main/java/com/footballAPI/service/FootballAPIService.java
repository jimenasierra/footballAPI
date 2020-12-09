package com.footballAPI.service;

import com.footballAPI.adapter.FootballDataAdapter;
import com.footballAPI.dto.*;
import com.footballAPI.entity.CompetitionEntity;
import com.footballAPI.entity.PlayerEntity;
import com.footballAPI.entity.TeamCompetitionEntity;
import com.footballAPI.entity.TeamEntity;
import com.footballAPI.mapper.CompetitionMapper;
import com.footballAPI.mapper.PlayerMapper;
import com.footballAPI.mapper.TeamCompetitionMapper;
import com.footballAPI.mapper.TeamMapper;
import com.footballAPI.property.FootballDataProperty;
import com.footballAPI.repository.CompetitionRepository;
import com.footballAPI.repository.PlayerRepository;
import com.footballAPI.repository.TeamCompetitionRepository;
import com.footballAPI.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@EnableConfigurationProperties(FootballDataProperty.class)
public class FootballAPIService {

    private final CompetitionRepository competitionRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final TeamCompetitionRepository teamCompetitionRepository;
    private final FootballDataAdapter footballDataAdapter;


    public FootballAPIService(CompetitionRepository competitionRepository, PlayerRepository playerRepository, TeamRepository teamRepository, TeamCompetitionRepository teamCompetitionRepository, FootballDataAdapter footballDataAdapter) {
        this.competitionRepository = competitionRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.teamCompetitionRepository = teamCompetitionRepository;
        this.footballDataAdapter = footballDataAdapter;
    }

    public ResponseDto loadCompetition(String leagueCode) {
        try {
            // Not already imported
            if (!competitionRepository.existsById(Long.parseLong(leagueCode))) {
                ResponseEntity<CompetitionDto> result = footballDataAdapter.loadCompetition(leagueCode);
                CompetitionDto competitionDto = result.getBody();
                // Exist
                if (competitionDto != null) {
                    CompetitionEntity competitionEntity = CompetitionMapper.mapCompetitionToEntity(competitionDto);
                    competitionRepository.save(competitionEntity);
                loadCompetitionTeams(leagueCode, competitionDto);
                    return new ResponseDto(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Successfully imported", -1);
                    // Not exist
                } else {
                    return new ResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Not found", -1);
                }
                // Already imported
            } else {
                return new ResponseDto(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), "League already imported", -1);
            }
        }catch (HttpServerErrorException ex) {
            return new ResponseDto(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT.value(), "Server error", -1);
        }
    }

    public ResponseDto loadCompetitionTeams(String leagueCode, CompetitionDto competitionDto) {

        ResponseEntity<ParticipantTeamsDto> result = footballDataAdapter.loadCompetitionTeams(leagueCode, competitionDto);

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
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return new ResponseDto(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT.value(), "Server error", -1);
                }
            }
            teamCompetitionRepository.saveAll(teamCompetitionEntities);
            return new ResponseDto(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Successfully imported", -1);
        } else {
            return new ResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Not found", -1);
        }
    }

    //    @Async("threadPoolTaskExecutor")
    public ResponseDto loadTeamPlayers(String teamCode) {

        ResponseEntity<TeamDto> result = footballDataAdapter.loadTeamPlayers(teamCode);

        if (result.getBody() != null) {
            List<PlayerDto> playerDtos = result.getBody().getSquad();
            List<PlayerEntity> playerEntities = new ArrayList<>();
            for (PlayerDto playerDto : playerDtos) {
                TeamDto teamDto = new TeamDto();
                teamDto.setId(Long.parseLong(teamCode));
                playerDto.setTeam(teamDto);
                PlayerEntity playerEntity = PlayerMapper.mapTeamToEntity(playerDto);
                playerEntities.add(playerEntity);
            }
            playerRepository.saveAll(playerEntities);
            return new ResponseDto(HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), "Successfully imported", -1);
        } else {
            return new ResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "Not found", -1);
        }
    }

    public ResponseDto totalPlayers(String leagueCode) {

        try {
            CompetitionDto competitionDto = new CompetitionDto();
            competitionDto.setId(Long.parseLong(leagueCode));
            CompetitionEntity competitionEntity = CompetitionMapper.mapCompetitionToEntitySimple(competitionDto);

            List<TeamCompetitionEntity> teamCompetitionEntities = teamCompetitionRepository.findAllByCompetition(competitionEntity);
            ArrayList<Long> teamsId = new ArrayList<>();

            teamCompetitionEntities.forEach(teamCompetition -> teamsId.add(teamCompetition.getTeam().getId()));

            List<PlayerEntity> playerEntities = playerRepository.findAllByTeam_IdIn(teamsId);

            long count = playerEntities.size();

            if (count != 0) {
                return new ResponseDto(HttpStatus.OK, HttpStatus.OK.value(), "", count);
            } else {
                return new ResponseDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "", count);
            }
        } catch (Exception ex) {
            return new ResponseDto(HttpStatus.GATEWAY_TIMEOUT, HttpStatus.GATEWAY_TIMEOUT.value(), "Server error", -1);
        }
    }
}
