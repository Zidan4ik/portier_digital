package org.example.portier_digital_admin.auth;

import org.example.portier_digital_admin.enums.Role;
import org.example.portier_digital_admin.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.portier_digital_admin.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImpTest {
    @InjectMocks
    private UserDetailsServiceImp userDetailsServiceImp;
    @Mock
    private UserService userService;
    private User user;
    @BeforeEach
    public void setUp() {
        user = new User(1L,"Roma","roo@gmail.com","1234", Role.ROLE_USER);
    }

    @Test
    public void testLoadUserByUsername_Success() {
        when(userService.getByEmail("roo@gmail.com")).thenReturn(user);
        UserDetails userDetails = userDetailsServiceImp.loadUserByUsername("roo@gmail.com");
        assertNotNull(userDetails);
        assertEquals("roo@gmail.com", userDetails.getUsername());
        assertEquals("1234", userDetails.getPassword());
    }
}