package org.example.portier_digital_admin.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.example.portier_digital_admin.entity.User;
import org.example.portier_digital_admin.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@ExtendWith(MockitoExtension.class)
class UserDetailsImpTest {
    private User user;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        user = new User(1L,"Roma","roo@gmail.com","1234", Role.ROLE_ADMIN);
        userDetails = new UserDetailsImp(user);
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(Role.ROLE_ADMIN.toString())));
    }

    @Test
    public void testGetPassword() {
        String password = userDetails.getPassword();
        assertEquals("1234", password);
    }

    @Test
    public void testGetUsername() {
        String username = userDetails.getUsername();
        assertEquals("roo@gmail.com", username);
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }
}