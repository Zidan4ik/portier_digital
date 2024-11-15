package org.example.portier_digital_admin.service;

import org.example.portier_digital_admin.dto.UserDTO;
import org.example.portier_digital_admin.entity.User;

public interface UserService {
    User getByEmail(String email);

    User save(User user);

    void registration(UserDTO dto);

    boolean existsByEmail(String name);
}
