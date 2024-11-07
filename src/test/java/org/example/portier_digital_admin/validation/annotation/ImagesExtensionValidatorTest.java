package org.example.portier_digital_admin.validation.annotation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class ImagesExtensionValidatorTest {

    private ImagesExtensionValidator validator;;
    private MultipartFile emptyFile;
    private MultipartFile nullFile;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new ImagesExtensionValidator();
        emptyFile = new MockMultipartFile("file", "empty.jpg", "image/jpeg", new byte[0]);
        nullFile = null;
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    public void testIsValid_withNullFile() {
        assertTrue(validator.isValid(nullFile, context), "Null file should pass validation (returns true)");
    }
    @Test
    public void testIsValid_withWithNotEmpty() {
        MockMultipartFile file = new MockMultipartFile("image-name1", "image1.html", "text/html", "content".getBytes());
        assertFalse(validator.isValid(file, context), "File data are not empty!");
    }

    @Test
    public void testIsValid_withEmptyFile() {
        assertTrue(validator.isValid(emptyFile, context), "Empty file should pass validation (returns true)");
    }
}