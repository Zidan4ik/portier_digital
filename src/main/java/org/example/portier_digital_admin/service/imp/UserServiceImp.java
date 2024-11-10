package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.UserDTO;
import org.example.portier_digital_admin.entity.User;
import org.example.portier_digital_admin.mapper.UserMapper;
import org.example.portier_digital_admin.repository.UserRepository;
import org.example.portier_digital_admin.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email: " + email + " was not found!")
        );
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void registration(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        save(user);
    }
}
