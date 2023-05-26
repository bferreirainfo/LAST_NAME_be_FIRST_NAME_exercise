package com.ecore.roles.service.impl;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecore.roles.client.TeamsClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.constants.ValidationConstants;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.TeamsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamsServiceImpl implements TeamsService {

    private final TeamsClient teamsClient;

    public Team getTeam(UUID id) {
        return Optional.ofNullable(teamsClient.getTeam(id).getBody())
                .orElseThrow(() -> new ResourceNotFoundException(
                        format(ValidationConstants.TEAM_NOT_FOUND, id)));
    }

    public List<Team> getTeams() {
        return teamsClient.getTeams().getBody();
    }
}
