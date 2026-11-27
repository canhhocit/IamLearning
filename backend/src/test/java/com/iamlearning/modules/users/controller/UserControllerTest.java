package com.iamlearning.modules.users.controller;

import com.iamlearning.modules.users.dto.response.UserResponse;
import com.iamlearning.modules.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void listsUsers() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.getAllUsers()).thenReturn(List.of(new UserResponse(id, "Alex", "alex@example.com")));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].name").value("Alex"))
                .andExpect(jsonPath("$[0].email").value("alex@example.com"));
    }

    @Test
    void createsUserAndReturnsCreatedResponse() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.createUser(any())).thenReturn(new UserResponse(id, "Alex", "alex@example.com"));

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Alex",
                                  "email": "alex@example.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Alex"))
                .andExpect(jsonPath("$.email").value("alex@example.com"));
    }

    @Test
    void updatesUserAndReturnsUpdatedResponse() throws Exception {
        UUID id = UUID.randomUUID();
        when(userService.updateUser(eq(id), any()))
                .thenReturn(new UserResponse(id, "Alex Nguyen", "alex.nguyen@example.com"));

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Alex Nguyen",
                                  "email": "alex.nguyen@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alex Nguyen"))
                .andExpect(jsonPath("$.email").value("alex.nguyen@example.com"));
    }

    @Test
    void deletesUserAndReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());
    }
}
