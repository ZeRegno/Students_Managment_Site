package org.Util_methods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DBConnectionUtil {

    public static String[] getDBCredentials() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + "db_credentials.txt"; // Путь к файлу db_credentials.txt

        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("Credentials file not found at: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String username = reader.readLine().split("=")[1];
            String password = reader.readLine().split("=")[1];
            return new String[]{username, password};
        }
    }
}
