package com.itm.space.backendresources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class UserServiceTest extends BaseIntegrationTest{

    @Autowired
    private UserService userService;
    @Autowired
    private Keycloak keycloak;

    private final UserRequest userRequest = new UserRequest("username",
            "user@mail.com",
            "password",
            "firstName",
            "lastName");

    @Test
    @WithMockUser(roles="MODERATOR")
    void createUser() throws Exception {
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isOk());
        UserRepresentation createdUser = keycloak.realm("ITM").users().search(userRequest.getUsername()).get(0);
        assertEquals(createdUser.getUsername(), userRequest.getUsername());
        assertEquals(createdUser.getFirstName(), userRequest.getFirstName());
        assertEquals(createdUser.getLastName(), userRequest.getLastName());
        assertEquals(createdUser.getEmail(), userRequest.getEmail());

        keycloak.realm("ITM").users().get(createdUser.getId()).remove();
    }

    @Test
    @WithMockUser(roles="MODERATOR")
    void getUserById() throws Exception {
        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isOk());
        UserRepresentation createdUser = keycloak.realm("ITM").users().search(userRequest.getUsername()).get(0);
        UserResponse userResponse = userService.getUserById(UUID.fromString(createdUser.getId()));

        assertNotNull(userResponse);
        assertEquals(createdUser.getFirstName(), userResponse.getFirstName());
        assertEquals(createdUser.getLastName(), userResponse.getLastName());
        assertEquals(createdUser.getEmail(), userResponse.getEmail());
        keycloak.realm("ITM").users().get(createdUser.getId()).remove();
    }


    @Test
    void getUserByIdNegative(){
        List<UserRepresentation> listOfCreatedUser = keycloak.realm("ITM").users().search("abracadabra");
        assertEquals(0, listOfCreatedUser.size());
    }
}