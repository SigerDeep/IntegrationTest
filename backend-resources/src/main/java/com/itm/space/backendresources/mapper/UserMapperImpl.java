package com.itm.space.backendresources.mapper;

import com.itm.space.backendresources.api.response.UserResponse;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapperImpl implements UserMapper{
    @Override
    public UserResponse userRepresentationToUserResponse(UserRepresentation userRepresentation, List<RoleRepresentation> roleList, List<GroupRepresentation> groupList) {
        return new UserResponse(
                userRepresentation.getFirstName(),
                userRepresentation.getLastName(),
                userRepresentation.getEmail(),
                roleList.stream().map(RoleRepresentation::getName).toList(),
                groupList.stream().map(GroupRepresentation::getName).toList()
        );
    }

    @Override
    public List<String> mapRoleRepresentationToString(List<RoleRepresentation> roleList) {
        return UserMapper.super.mapRoleRepresentationToString(roleList);
    }

    @Override
    public List<String> mapGroupRepresentationToString(List<GroupRepresentation> groupList) {
        return UserMapper.super.mapGroupRepresentationToString(groupList);
    }
}
