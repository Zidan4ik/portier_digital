package org.example.portier_digital_admin.service.imp;

import org.example.portier_digital_admin.util.LogUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImpTest {
    @InjectMocks
    private ImageServiceImp imageService;

    @TempDir
    private Path tempDir;
    private static final String UPLOAD_DIR = "./test-files";
    private Path uploadPath;
    private Path tempDirectory;
    private Path tempFolder;
    private Path tempFile;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() throws IOException {
        uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test content".getBytes());
        tempDirectory = Files.createTempDirectory(uploadPath, "testDir");
        tempFolder = Files.createDirectory(tempDirectory.resolve("testFolder1"));
        tempFile = Files.createFile(tempDirectory.resolve("testFolder1").resolve("testFile.txt"));
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.walkFileTree(tempDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    void ImageService_Init_Success() {
        Path testPath = tempDir.resolve("uploads-test");
        imageService.init(testPath);
        assertTrue(Files.exists(testPath), "Directory should be created successfully");
    }

    @Test
    void ImageService_Init_Failure() {
        Path mockPath = mock(Path.class);
        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class);
             MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            IOException testIoException = new IOException("Test IOException");
            filesMock.when(() -> Files.createDirectories(mockPath)).thenThrow(testIoException);
            assertThrows(RuntimeException.class, () -> imageService.init(mockPath));
            logUtilMock.verify(() -> LogUtil.logError("Failed to initialize folder for upload at " + mockPath, testIoException));
        }
    }

    @Test
    void ImageService_Save_SuccessfulSave() {
        imageService.save(mockFile, tempFile.toString());
        assertTrue(Files.exists(tempFile), "File should be saved successfully");
    }

    @Test
    void ImageService_Save_DirectoryNotExists() {
        String path = "./test-files/testDir/testFolder1/testFile.txt";
        imageService.save(mockFile, path);
        Path pathFile = Path.of(path);
        assertFalse(Files.exists(pathFile), "File should be not saved successfully");
        assertFalse(Files.exists(pathFile.getParent()), "Parent directory should not be created");
    }

    @Test
    void ImageService_Save_FileIsNull() {
        imageService.save(null, tempFile.toString());
    }
    @Test
    void ImageService_Save_PathIsNull() {
        imageService.save(mockFile, null);
    }
    @Test
    void ImageService_Save_PathIsEmpty() {
        imageService.save(mockFile, "");
    }
    @Test
    void ImageService_Save_WhenExceptionIsIOException() {
        try (MockedStatic<LogUtil> logUtilMock = mockStatic(LogUtil.class);
             MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            IOException testIoException = new IOException("Test IOException");
            filesMock.when(() -> Files.copy(any(InputStream.class),any(Path.class))).thenThrow(testIoException);
            imageService.save(mockFile, "/uploads/test.txt");
            logUtilMock.verify(() -> LogUtil.logError("Error occurred while saving file to /uploads/test.txt", testIoException));
        }
    }
    @Test
    void ImageService_DeleteByPath_WhenFileExists() throws IOException {
        imageService.deleteByPath(tempFile.toString().substring(1));
        assertFalse(Files.exists(tempFile), "File should be deleted successfully");
    }
    @Test
    void ImageService_DeleteByPath_WhenFileDoesNotExist() {
        assertDoesNotThrow(() -> imageService.deleteByPath("./test-files/nonexistent.jpg"),
                "Should not throw an exception if file does not exist");
    }

    @Test
    void testGenerateFileName() {
        String generatedName = imageService.generateFileName(mockFile);
        assertNotNull(generatedName, "Generated file name should not be null");
        assertTrue(generatedName.matches("^[\\w-]+\\.test\\.jpg$"), "Generated file name should match expected pattern");
    }
}