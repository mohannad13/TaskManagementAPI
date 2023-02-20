package com.example.TaskManagementAPI.controller;

import java.util.List;
import java.util.Optional;

import com.example.TaskManagementAPI.security.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.TaskManagementAPI.model.User;
import com.example.TaskManagementAPI.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    /**
     * Get all users in the system.
     *
     * @param request HttpServletRequest object containing the current request
     * @return a list of all users in the system
     * @throws ResponseStatusException if the user does not have the required role to access this resource
     */
    @GetMapping
    public List<User> getAllUsers(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authentication)) {
            return userRepository.findAll();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * Create a new user.
     *
     * @param user    the user to be created
     * @param request HttpServletRequest object containing the current request
     * @return the created user
     * @throws ResponseStatusException if the user does not have the required role to access this resource
     */
    @PostMapping
    public User createUser(@RequestBody User user, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authentication)) {
            userDetailsService.saveUser(user);
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * Update an existing user.
     *
     * @param id      the ID of the user to be updated
     * @param user    the updated user data
     * @param request HttpServletRequest object containing the current request
     * @return the updated user
     * @throws ResponseStatusException if the user does not have the required role to access this resource or is not authorized to update the specified user
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authentication)) {
            Optional<User> currentUser = userRepository.findById(id);
            if (currentUser.isPresent() && currentUser.get().getId().equals(id)) {
                user.setId(id);
                return userRepository.save(user);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this user");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * Delete a user.
     *
     * @param id      the ID of the user to be deleted
     * @param request HttpServletRequest object containing the current request
     * @throws ResponseStatusException if the user does not have the required role to access this resource or is not authorized to delete the specified user
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAdmin(authentication)) {
            Optional<User> currentUser = userRepository.findById(id);
            if (currentUser.isPresent() && currentUser.get().getId().equals(id)) {
                userRepository.deleteById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this user");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * Check if a user has the ADMIN role.
     *
     * @param authentication the authentication object containing the user's authorities
     * @return true if the user has the ADMIN role, false if not.
     */
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));
    }
}
