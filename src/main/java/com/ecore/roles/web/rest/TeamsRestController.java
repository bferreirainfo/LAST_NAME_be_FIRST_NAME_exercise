package com.ecore.roles.web.rest;

import static com.ecore.roles.constants.RestConstants.APPLICATION_JSON;
import static com.ecore.roles.constants.RestConstants.FIELD_TEAM_ID;
import static com.ecore.roles.constants.RestConstants.PATH_TEAM_ID;
import static com.ecore.roles.constants.RestConstants.V1_TEAMS;
import static com.ecore.roles.web.dto.TeamDto.fromModel;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecore.roles.service.TeamsService;
import com.ecore.roles.web.TeamsApi;
import com.ecore.roles.web.dto.TeamDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = V1_TEAMS)
public class TeamsRestController implements TeamsApi {

    private final TeamsService teamsService;

    @Override
    @GetMapping(
            produces = {APPLICATION_JSON})
    public ResponseEntity<List<TeamDto>> getTeams() {
        return ResponseEntity
                .status(200)
                .body(teamsService.getTeams().stream()
                        .map(TeamDto::fromModel)
                        .collect(Collectors.toList()));
    }

    @Override
    @GetMapping(
            path = PATH_TEAM_ID,
            produces = {APPLICATION_JSON})
    public ResponseEntity<TeamDto> getTeam(
            @PathVariable(FIELD_TEAM_ID) UUID teamId) {
        return ResponseEntity
                .status(200)
                .body(fromModel(teamsService.getTeam(teamId)));
    }

}
