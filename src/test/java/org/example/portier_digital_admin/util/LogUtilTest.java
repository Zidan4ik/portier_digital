package org.example.portier_digital_admin.util;

import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class LogUtilTest {

    @Test
    void testLogErrorWithoutException() {
        String message = "This is an error message without exception";
        LogUtil.logError(message, null);
    }
}