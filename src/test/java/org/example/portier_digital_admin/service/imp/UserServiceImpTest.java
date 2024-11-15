package org.example.portier_digital_admin.service.imp;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import org.example.portier_digital_admin.dto.UserDTO;
import org.example.portier_digital_admin.entity.User;
import org.example.portier_digital_admin.enums.Role;
import org.example.portier_digital_admin.mapper.UserMapper;
import org.example.portier_digital_admin.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImp userService;
    private User user;
    private UserDTO userDTO;
    @BeforeEach
    public void setUp() {
    user = new User(1L,"Roma","roo@gmail.com","1234", Role.ROLE_USER);
    userDTO = new UserDTO(1L,"Roma","roo@gmail.com","1234","1234");
    }

    @Test
    public void testGetByEmail_WhenUserExists() {
        String email = "roo@gmail.com";
        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.getByEmail(email);

        assertNotNull(result, "User should not be null");
        assertEquals(user, result, "Returned user should match the mock user");
        verify(userRepository, times(1)).getByEmail(email);
    }

    @Test
    public void testGetByEmail_WhenUserDoesNotExist() {
        String email = "nonexistent@example.com";

        when(userRepository.getByEmail(email)).thenReturn(Optional.empty());
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            userService.getByEmail(email);
        });
        assertEquals("User with email: " + email + " was not found!", thrown.getMessage());
    }

    @Test
    public void testGetByEmail_WhenUserExistByEmail() {
        String email = "roo@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        userService.existsByEmail(email);
        verify(userRepository, times(1)).existsByEmail(email);
    }
    @Test
    public void testGetByEmail_WhenUserNoExistByEmail() {
        String email = "roo@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        userService.existsByEmail(email);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void testSave() {
        when(userRepository.save(user)).thenReturn(user);
        User savedUser = userService.save(user);
        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId(), "Id's should be match");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testRegistration() {
        userService.registration(userDTO);
        verify(userRepository,times(1)).save(any(User.class));
    }
}