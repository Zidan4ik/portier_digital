package org.example.portier_digital_admin.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtil {
    public static void logInitNotification(String message) {
        log.info("Starting initialization package: {}", message);
    }

    public static void logInfo(String message) {
        log.info(message);
    }

    public static void logError(String message, Throwable e) {
        if (e != null) {
            log.error(message, e);
        } else {
            log.error(message);
        }
    }

    public static void logWarning(String message) {
        log.warn(message);
    }
}
