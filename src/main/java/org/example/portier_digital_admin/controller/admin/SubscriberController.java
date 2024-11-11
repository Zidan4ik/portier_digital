package org.example.portier_digital_admin.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.dto.PageResponse;
import org.example.portier_digital_admin.dto.SubscriberDTOForView;
import org.example.portier_digital_admin.service.SubscriberService;
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
public class SubscriberController {
    private final SubscriberService subscriberService;

    @GetMapping("/subscribers")
    public ModelAndView viewSubscribers() {
        return new ModelAndView("admin/subscribers");
    }

    @GetMapping("/subscribers/data")
    @ResponseBody
    public ResponseEntity<PageResponse<SubscriberDTOForView>> getSubscribers(
            @ModelAttribute SubscriberDTOForView dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<SubscriberDTOForView> all = subscriberService.getAll(dto, pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/subscriber/{id}/delete")
    public ResponseEntity<String> deleteSubscriberById(@PathVariable(name = "id") Long id) {
        subscriberService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Subscriber with id " + id + " was deleted!");
    }
    @ModelAttribute("isActiveSubscribers")
    public boolean toActiveSubscribers(){
        return true;
    }
}
