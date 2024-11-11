package org.example.portier_digital_admin.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.UserDTO;
import org.example.portier_digital_admin.entity.User;
import org.example.portier_digital_admin.mapper.UserMapper;
import org.example.portier_digital_admin.repository.UserRepository;
import org.example.portier_digital_admin.service.UserService;
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();

    @Override
    public User getByEmail(String email) {
        LogUtil.logInfo("Fetched user with email: " + email);
        User user = userRepository.getByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User with email: " + email + " was not found!")
        );
        LogUtil.logInfo("Fetched user with email: " + email + " - " + user);
        return user;
    }

    @Override
    public User save(User user) {
        LogUtil.logInfo("Saving user!");
        User user_ = userRepository.save(user);
        LogUtil.logInfo("User with id: " + user.getId() + "was saved! - " + user);
        return user_;
    }

    @Override
    public void registration(UserDTO dto) {
        LogUtil.logInfo("Start registration user!");
        User user = userMapper.toEntity(dto);
        save(user);
        LogUtil.logInfo("Finish registration user!");
    }
}
