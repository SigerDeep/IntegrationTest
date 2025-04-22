package com.itm.space.backendresources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import javax.ws.rs.core.MediaType;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Slf4j
class IntegrationTest extends BaseIntegrationTest {

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    private final UserRequest userRequest = new UserRequest("user",
            "user@mail.com",
            "password",
            "firstName",
            "lastName");

    private final UserResponse userResponse =
            new UserResponse("firstName","lastName","email@example.com",
                    null,null);

    @Test
    @WithMockUser(roles="MODERATOR")
    void create() throws Exception {
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk());
        verify(userService,times(1)).createUser(any(UserRequest.class));
    }

    @Test
    @WithMockUser(roles="MODERATOR")
    void getUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        Mockito.when(userService.getUserById(userId)).thenReturn(userResponse);

        mvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk());
        verify(userService,times(1)).getUserById(userId);
    }

    @Test
    @WithMockUser(roles="MODERATOR")
    void hello() throws Exception {
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk());
    }
}