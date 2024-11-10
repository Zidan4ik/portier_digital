package org.example.portier_digital_admin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.portier_digital_admin.enums.Role;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String email;
    private String password;
    private Role role;
}
