package org.example.portier_digital_admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationDTOForView {
    private Long id;
    private String name;
    private String email;
    private String desire;
}
