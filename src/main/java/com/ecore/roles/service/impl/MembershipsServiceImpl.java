package com.ecore.roles.service.impl;

import static com.ecore.roles.constants.ValidationConstants.INVALID_MEMBERSHIP_OBJECT;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.constants.ValidationConstants;
import com.ecore.roles.exception.InvalidArgumentException;
import com.ecore.roles.exception.ResourceExistsException;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Membership;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.MembershipsService;
import com.ecore.roles.service.TeamsService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipsServiceImpl implements MembershipsService {
	
	private final MembershipRepository membershipRepository;
    private final RoleRepository roleRepository;
    private final TeamsService teamsService;

    @Override
    public Membership assignRoleToMembership(@NonNull Membership membership) {
    	  UUID roleId = ofNullable(membership.getRole()).map(Role::getId)
                  .orElseThrow(() -> new InvalidArgumentException(Role.class));

          if (membershipRepository.findByUserIdAndTeamId(membership.getUserId(), membership.getTeamId())
                  .isPresent()) {
              throw new ResourceExistsException(Membership.class);
          }

          roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(Role.class, roleId));
          
          Team team = ofNullable(teamsService.getTeam(membership.getTeamId()))
        		  		.orElseThrow(()-> new ResourceNotFoundException(Team.class, membership.getTeamId()));
          List<UUID> teamMemberIds = ofNullable(team.getTeamMemberIds()).orElse(new ArrayList<>());
          
          if(!teamMemberIds.contains(membership.getUserId())){
          	  throw new InvalidArgumentException(INVALID_MEMBERSHIP_OBJECT);
          }
		
          return membershipRepository.save(membership);
    }

    @Override
    public List<Membership> getMemberships(@NonNull UUID roleID) {
        return membershipRepository.findByRoleId(roleID);
    }
}
