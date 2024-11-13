package org.example.portier_digital_admin.util;

public class ImageUtil {
    public static String getSubstringPath(String absolutePath) {
        int index = absolutePath.indexOf("/uploads/");
        if (index != -1) {
            return absolutePath.substring(index);
        } else {
            throw new IllegalArgumentException("Invalid path: '/uploads/' not found in the provided path.");
        }
    }
}
