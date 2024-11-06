package org.example.portier_digital_admin.service.imp;

import org.example.portier_digital_admin.service.ImageService;
import org.example.portier_digital_admin.util.LogUtil;
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
        LogUtil.logInitNotification(root.toString());
        try {
            Files.createDirectories(root);
            LogUtil.logInfo("Builded package by path: " + root);
        } catch (IOException e) {
            LogUtil.logError("Failed to initialize folder for upload at " + root, e);
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file, String path) {
        try {
            if (path != null && !path.isEmpty()) {
                Path path_ = Path.of("." + path);
                if (!Files.exists(path_.getParent())) {
                    init(path_.getParent());
                }
                if (file != null) {
                    LogUtil.logInfo("Attempting to save file: " + file.getOriginalFilename() + " to path: " + path);
                    Files.copy(file.getInputStream(), path_);
                    LogUtil.logInfo("File saved successfully to path: " + path_);
                }
            }
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                LogUtil.logError("A file of that name already exists at " + path, e);
                throw new RuntimeException("A file of that name already exists.");
            }
            LogUtil.logError("Error occurred while saving file to " + path, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteByPath(String path) throws IOException {
        LogUtil.logInfo("Attempting to delete file at path: " + path);
        Path path_ = Path.of("." + path);
        if (Files.exists(path_) && Files.isRegularFile(path_)) {
            Files.delete(path_);
            LogUtil.logInfo("File deleted successfully from path: " + path);
        } else {
            LogUtil.logWarning("File not found or is not a regular file at path: " + path);
        }
    }

    @Override
    public String generateFileName(MultipartFile file) {
        String generatedFileName = UUID.randomUUID() + "." + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        LogUtil.logInfo("Generated file name: " + generatedFileName + " for original file: " + file.getOriginalFilename());
        return generatedFileName;
    }
}