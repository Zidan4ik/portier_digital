package org.example.portier_digital_admin.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.portier_digital_admin.repository.CardRepository;
import org.example.portier_digital_admin.service.CardService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class CardServiceImp implements CardService {
    private final CardRepository cardRepository;
    @Override
    public void init(Path root) {

    }

    @Override
    public void save(MultipartFile file, Path path) {

    }

    @Override
    public void deleteByPath(Path path) throws IOException {

    }

    @Override
    public String generateFileName(MultipartFile file) {
        return "";
    }
}
