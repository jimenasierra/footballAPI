package com.footballAPI.adapter;

import com.footballAPI.dto.CompetitionDto;
import com.footballAPI.dto.ParticipantTeamsDto;
import com.footballAPI.dto.TeamDto;
import com.footballAPI.property.FootballDataProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class FootballDataAdapter {


    private final FootballDataProperty footballDataProperty;

    public FootballDataAdapter(FootballDataProperty footballDataProperty) {
        this.footballDataProperty = footballDataProperty;
    }

    private HttpEntity<String> headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-AUTH-TOKEN", footballDataProperty.getToken());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        return entity;
    }

    public ResponseEntity<CompetitionDto> loadCompetition(String leagueCode) {
        final String uri = footballDataProperty.getUrl() + "competitions/" + leagueCode;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity headers = headers();
        ResponseEntity<CompetitionDto> result = restTemplate.exchange(uri, HttpMethod.GET, headers, CompetitionDto.class);
        return result;
    }

    public ResponseEntity<ParticipantTeamsDto> loadCompetitionTeams(String leagueCode, CompetitionDto competitionDto) {
        final String uri = footballDataProperty.getUrl() + "competitions/" + leagueCode + "/teams";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity entity = headers();
        ResponseEntity<ParticipantTeamsDto> result = restTemplate.exchange(uri, HttpMethod.GET, entity, ParticipantTeamsDto.class);
        return result;
    }

    public ResponseEntity<TeamDto> loadTeamPlayers(String teamCode) {
        final String uri = footballDataProperty.getUrl() + "teams/" + teamCode;
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity headers = headers();
        ResponseEntity<TeamDto> result = restTemplate.exchange(uri, HttpMethod.GET, headers, TeamDto.class);
        return result;
    }
}
