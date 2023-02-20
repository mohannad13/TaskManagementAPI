package com.example.TaskManagementAPI.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.TaskManagementAPI.TestConfigurationBase;
import com.example.TaskManagementAPI.model.User;
import com.example.TaskManagementAPI.repository.UserRepository;
import com.example.TaskManagementAPI.security.service.UserDetailsImpl;
import com.example.TaskManagementAPI.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void loadUserByUsername_userExists_returnsUserDetails() {
        String email = "john.doe@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("password");
        when(userRepository.findByEmail(email)).thenReturn(user);

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

        assertEquals(email, userDetails.getUsername());
        assertEquals(user.getPasswordHash(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void loadUserByUsername_userDoesNotExist_throwsUsernameNotFoundException() {
        String email = "jane.doe@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(email));
    }
}
