package org.example.portier_digital_admin.mapper;

import org.example.portier_digital_admin.dto.ApplicationDTOForAdd;
import org.example.portier_digital_admin.dto.ApplicationDTOForView;
import org.example.portier_digital_admin.entity.Application;

import java.util.List;

public class ApplicationMapper {
    public Application toEntityForAdd(ApplicationDTOForAdd dto) {
        Application entity = new Application();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setDesire(dto.getDesire());
        return entity;
    }

    public ApplicationDTOForView toDTOForView(Application application) {
        return ApplicationDTOForView.builder()
                .id(application.getId())
                .name(application.getName())
                .email(application.getEmail())
                .desire(application.getDesire())
                .build();
    }

    public List<ApplicationDTOForView> toDTOForView(List<Application> applications) {
        return applications.stream()
                .map(val -> ApplicationDTOForView.builder()
                        .id(val.getId())
                        .name(val.getName())
                        .email(val.getEmail())
                        .desire(val.getDesire())
                        .build())
                .toList();
    }
}