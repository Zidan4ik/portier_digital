package org.example.portier_digital_admin.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.ApplicationDTOForView;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.service.ApplicationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping("/applications")
    public ModelAndView viewApplications() {
        return new ModelAndView("admin/applications");
    }

    @GetMapping("/applications/data")
    @ResponseBody
    public ResponseEntity<PageResponse<ApplicationDTOForView>> getApplications(
            @ModelAttribute ApplicationDTOForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ApplicationDTOForView> all = applicationService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/application/{id}/delete")
    public ResponseEntity<String> deleteApplicationById(@PathVariable(name = "id") Long id) {
        applicationService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Application with id " + id + " was deleted!");
    }
    @ModelAttribute("isActiveApplications")
    public boolean toActiveApplications(){
        return true;
    }
}
