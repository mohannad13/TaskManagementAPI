package com.example.TaskManagementAPI.controller;



import java.util.HashMap;
import java.util.Map;

import com.example.TaskManagementAPI.dto.LoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.example.TaskManagementAPI.security.JwtUtil;
@RestController
public class LoginController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Generates a JWT token for the given login credentials.
     *
     * @param request the login request DTO containing the user's email and password
     * @return a map containing the JWT token generated for the user
     * @throws Exception if the email or password are incorrect
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public Map<String, String> generateToken(@RequestBody LoginRequestDTO request) throws Exception {
        // Load the user details for the given email address
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Check if the password provided in the login request matches the user's actual password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new Exception("Incorrect username or password");
        }

        // Generate a JWT token for the user
        final String jwt = jwtUtil.generateToken(userDetails);

        // Return the JWT token in a map
        Map<String, String> result = new HashMap<>();
        result.put("token", jwt);
        return result;
    }
}
