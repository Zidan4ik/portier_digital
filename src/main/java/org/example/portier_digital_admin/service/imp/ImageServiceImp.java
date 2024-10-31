package org.example.portier_digital_admin.service.imp;

import org.example.portier_digital_admin.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageServiceImp implements ImageService {
    @Override
    public void init(Path root) {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file, String path) {
        try {
            if (path != null && !path.isEmpty()) {
                Path path_ = Path.of(path);
                if (!Files.exists(path_.getParent())) {
                    init(path_);
                }
                if (file != null) {
                    Files.copy(file.getInputStream(), path_);
                }
            }
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteByPath(Path path) throws IOException {
        if (Files.exists(path) && Files.isRegularFile(path)) {
            Files.delete(path);
        }
    }

    @Override
    public String generateFileName(MultipartFile file) {
        return UUID.randomUUID() + "." + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    }
}