package org.example.portier_digital_admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface CardService {
    void init(Path root);

    void save(MultipartFile file, Path path);

    void deleteByPath(Path path) throws IOException;

    String generateFileName(MultipartFile file);
}
