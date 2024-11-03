package org.example.portier_digital_admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "work_cards")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String pathToImage;
}
