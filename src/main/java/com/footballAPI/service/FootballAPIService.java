package com.footballAPI.service;

import com.footballAPI.adapter.FootballDataAdapter;
import com.footballAPI.dto.*;
import com.footballAPI.entity.CompetitionEntity;
import com.footballAPI.entity.PlayerEntity;
import com.footballAPI.entity.TeamCompetitionEntity;
import com.footballAPI.entity.TeamEntity;
import com.footballAPI.exception.LeagueAllReadyImportedException;
import com.footballAPI.exception.NotFoundException;
import com.footballAPI.exception.ServerErrorException;
import com.footballAPI.mapper.*;
import com.footballAPI.property.FootballDataProperty;
import com.footballAPI.repository.CompetitionRepository;
import com.footballAPI.repository.PlayerRepository;
import com.footballAPI.repository.TeamCompetitionRepository;
import com.footballAPI.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@EnableConfigurationProperties(FootballDataProperty.class)
public class FootballAPIService {

    private final CompetitionRepository competitionRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final TeamCompetitionRepository teamCompetitionRepository;
    private final FootballDataAdapter footballDataAdapter;

    private final TeamMapper teamMapper;


    public FootballAPIService(CompetitionRepository competitionRepository, PlayerRepository playerRepository, TeamRepository teamRepository, TeamCompetitionRepository teamCompetitionRepository, FootballDataAdapter footballDataAdapter) {
        this.competitionRepository = competitionRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.teamCompetitionRepository = teamCompetitionRepository;
        this.footballDataAdapter = footballDataAdapter;
        this.teamMapper = Mappers.getMapper(TeamMapper.class);
        ;
    }


    public void tryMapper(String teamCode) {
        TeamEntity teamEntity = teamRepository.findTeamEntitiesById(Long.parseLong(teamCode));
        log.warn(teamEntity.toString());
    }

    public ResponseDto loadCompetition(String leagueCode) {
        try {
            // Not already imported
            if (leagueCode.equals("")) {
                throw new ServerErrorException("Server error. The leagueCode is null");
            } else {
                if (!competitionRepository.existsById(Long.parseLong(leagueCode))) {
                    ResponseEntity<CompetitionDto> result = footballDataAdapter.loadCompetition(leagueCode);
                    CompetitionDto competitionDto = result.getBody();
                    // Exist
                    if (competitionDto != null) {
                        CompetitionEntity competitionEntity = CompetitionMapper.mapCompetitionToEntity(competitionDto);
                        competitionRepository.save(competitionEntity);
                        loadCompetitionTeams(leagueCode, competitionDto);
                        return new ResponseDto("Successfully imported", -1);
                        // Not exist
                    } else {
                        throw new NotFoundException("Not found 4");
                    }
                    // Already imported
                } else {
                    throw new LeagueAllReadyImportedException("League already imported");
                }
            }
        } catch (HttpServerErrorException ex) {
            log.warn(ex.toString());
            throw new ServerErrorException("Server error 1");
        } catch (NumberFormatException ex) {
            throw new ServerErrorException("Server error. The given league code is not a valid one");
        } catch (HttpClientErrorException ex) {
            throw new ServerErrorException("Server error. //" + ex.toString());
        }
    }

    public ResponseDto loadCompetitionTeams(String leagueCode, CompetitionDto competitionDto) {

        ResponseEntity<ParticipantTeamsDto> result = footballDataAdapter.loadCompetitionTeams(leagueCode, competitionDto);

        if (result.getBody() != null) {
            List<TeamDto> teamDtos = result.getBody().getTeams();
            List<TeamCompetitionEntity> teamCompetitionEntities = new ArrayList<TeamCompetitionEntity>();
            for (TeamDto teamDto : teamDtos) {

                TeamCompetitionDto teamCompetitionDto = new TeamCompetitionDto(teamDto, competitionDto);
                TeamCompetitionEntity teamCompetitionEntity = TeamCompetitionMapper.mapTeamCompetitionToEntity(teamCompetitionDto);
                teamCompetitionEntities.add(teamCompetitionEntity);

                TeamEntity teamEntity = TeamMapperImpl.mapTeamToEntity(teamDto);
                teamRepository.save(teamEntity);

                loadTeamPlayers(String.valueOf(teamDto.getId()));
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ServerErrorException("Server error. Timer fail");
                }
            }
            teamCompetitionRepository.saveAll(teamCompetitionEntities);
            return new ResponseDto("Successfully imported", -1);
        } else {
            throw new NotFoundException("Not found 1");
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
            return new ResponseDto("Successfully imported", -1);
        } else {
            throw new NotFoundException("Not found 2");
        }
    }

    public ResponseEntity totalPlayers(String leagueCode) {

        try {
            if (leagueCode.equals("")) {
                throw new NotFoundException("Not found. The given code is nul");
            } else {
                CompetitionDto competitionDto = new CompetitionDto();
                competitionDto.setId(Long.parseLong(leagueCode));
                CompetitionEntity competitionEntity = CompetitionMapper.mapCompetitionToEntitySimple(competitionDto);

                List<TeamCompetitionEntity> teamCompetitionEntities = teamCompetitionRepository.findAllByCompetition(competitionEntity);
                ArrayList<Long> teamsId = new ArrayList<>();

                teamCompetitionEntities.forEach(teamCompetition -> teamsId.add(teamCompetition.getTeam().getId()));

                List<PlayerEntity> playerEntities = playerRepository.findAllByTeam_IdIn(teamsId);

                long count = playerEntities.size();

                if (count != 0) {
                    String result = "Total: " + count;
                    return new ResponseEntity(result, HttpStatus.OK);
                } else {
                    throw new NotFoundException("Not found. The given code is not valid");
                }
            }
        } catch (NumberFormatException ex) {
            throw new ServerErrorException("Server error. The given league code is not valid");
        }
    }
}
