package org.example.portier_digital_admin.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ApplicationDTOForAdd;
import org.example.portier_digital_admin.dto.ApplicationDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.entity.Application;
import org.example.portier_digital_admin.mapper.ApplicationMapper;
import org.example.portier_digital_admin.repository.ApplicationRepository;
import org.example.portier_digital_admin.service.ApplicationService;
import org.example.portier_digital_admin.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.portier_digital_admin.service.specifications.ApplicationSpecificationBuilder.getSpecification;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImp implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper = new ApplicationMapper();

    @Override
    public Application save(ApplicationDTOForAdd dto) {
        LogUtil.logInfo("Saving application!");
        Application application = applicationRepository.save(applicationMapper.toEntityForAdd(dto));
        LogUtil.logInfo("Application with id: " + application.getId() + " was saved! - " + application);
        return application;
    }

    @Override
    public List<ApplicationDTOForView> getAll() {
        LogUtil.logInfo("Retrieving all applications without pagination!");
        List<ApplicationDTOForView> applicationDTO = applicationMapper.toDTOForView(applicationRepository.findAll());
        LogUtil.logInfo("Fetched applications: " + applicationDTO.size() + "!");
        return applicationDTO;
    }

    @Override
    public PageResponse<ApplicationDTOForView> getAll(ApplicationDTOForView dto, Pageable pageable) {
        LogUtil.logInfo("Fetching all applications with pagination and filter criteria!");
        Page<Application> page = applicationRepository.findAll(getSpecification(dto), pageable);
        List<ApplicationDTOForView> content = page.map(applicationMapper::toDTOForView).toList();
        LogUtil.logInfo("Fetched applications: " + page.getTotalElements() + "!");
        return new PageResponse<>(content, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting application with id: " + id);
        applicationRepository.deleteById(id);
        LogUtil.logInfo("Deleted application with id: " + id + "!");
    }
}
