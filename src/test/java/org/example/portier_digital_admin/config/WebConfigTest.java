package org.example.portier_digital_admin.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebConfigTest {
    @InjectMocks
    private WebConfig webConfig;

    @Mock
    private ResourceHandlerRegistry registry;

    @Mock
    private ResourceHandlerRegistration registration;

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("uploadsTest");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempDir);
    }

    @Test
    void shouldAddResourceHandler_whenDirectoryDoesNotExist() throws IOException {
        Files.deleteIfExists(tempDir);
        ReflectionTestUtils.setField(webConfig, "path", tempDir.toString());
        when(registry.addResourceHandler("/uploads/**")).thenReturn(registration);
        webConfig.addResourceHandlers(registry);
        verify(registry).addResourceHandler("/uploads/**");
        verify(registration).addResourceLocations("file:" + tempDir.toString() + "/");
        assertTrue(Files.exists(tempDir), "The directory should be created by addResourceHandlers method.");
    }
    @Test
    void shouldAddResourceHandler_whenDirectoryExists() {
        ReflectionTestUtils.setField(webConfig, "path", tempDir.toString());
        when(registry.addResourceHandler("/uploads/**")).thenReturn(registration);
        webConfig.addResourceHandlers(registry);
        verify(registry).addResourceHandler("/uploads/**");
        verify(registration).addResourceLocations("file:" + tempDir.toString() + "/");
        assertTrue(Files.exists(tempDir), "The directory should already exist.");
    }
}