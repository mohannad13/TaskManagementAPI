package com.example.TaskManagementAPI.controller;

import com.example.TaskManagementAPI.dto.LoginRequestDTO;
import com.example.TaskManagementAPI.model.User;
import com.example.TaskManagementAPI.security.JwtUtil;
import com.example.TaskManagementAPI.security.service.UserDetailsImpl;
import com.example.TaskManagementAPI.security.service.UserDetailsServiceImpl;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

//    @Test
//    public void testGenerateToken() throws Exception {
//        // Mock the user details service to return a user with a known password
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//        UserDetails userDetails = new UserDetailsImpl(user);
//        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
//
//        // Mock the JWT util to return a fixed token
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNTE2MjM5MDIyfQ.bjzgZ9s2Q-KzNkDl25O7N1PJgAMFC2c19uJ0t1UMluM";
//        when(jwtUtil.generateToken(userDetails)).thenReturn(token);
//
//        // Create a login request with the known password
//        LoginRequestDTO request = new LoginRequestDTO("test@example.com", "password");
//
//        // Send a POST request to the controller and verify the response
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
//                        .contentType("application/json")
//                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String content = result.getResponse().getContentAsString();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode jsonNode = objectMapper.readTree(content);
//        assertEquals(jsonNode.get("token").asText(), token);
//    }
//    @Test
//    public void testGenerateTokenWithIncorrectPassword() throws Exception {
//        // Mock the user details service to return a user with a known password
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//        UserDetails userDetails = new UserDetailsImpl(user);
//        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
//
//        // Create a login request with an incorrect password
//        LoginRequestDTO request = new LoginRequestDTO("test@example.com", "wrongpassword");
//
//        // Expect the authentication to fail and throw an exception
//        assertThrows(Exception.class, () -> {
//            mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
//                            .contentType("application/json")
//                            .content("{\"email\":\"test@example.com\",\"password\":\"wrongpassword\"}"))
//                    .andExpect(status().isOk())
//                    .andReturn();
//        });
//    }



}