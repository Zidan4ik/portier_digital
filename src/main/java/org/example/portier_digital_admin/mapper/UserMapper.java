package org.example.portier_digital_admin.mapper;

import org.example.portier_digital_admin.dto.UserDTO;
import org.example.portier_digital_admin.entity.User;
import org.example.portier_digital_admin.enums.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setEmail(dto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setRole(Role.ROLE_USER);
        return user;
    }
}
