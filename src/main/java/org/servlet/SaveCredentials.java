package org.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet("/SaveCredentials")
public class SaveCredentials extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("Received credentials: username=" + username + " password=" + password);


        saveCredentialsToFile(username, password);


        String dbUrl = "jdbc:postgresql://localhost:5432/students_db";
        System.out.println("Testing database connection to: " + dbUrl);
        response.sendRedirect("TestDBConnection?username=" + username + "&password=" + password);
    }

    private void saveCredentialsToFile(String username, String password) throws IOException {

        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + "db_credentials.txt";

        System.out.println("Saving credentials to file: " + filePath);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("username=" + username + "\n");
            writer.write("password=" + password + "\n");
        }
    }


}