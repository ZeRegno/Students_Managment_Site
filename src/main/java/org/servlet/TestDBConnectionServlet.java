package org.servlet;

import org.DB_init.Create_PostgreSQL_DB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.Util_methods.DBConnectionUtil;

import java.io.IOException;
import java.sql.*;

@WebServlet("/TestDBConnection")
public class TestDBConnectionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            try {
                Class.forName("org.postgresql.Driver");
                System.out.println("PostgreSQL Driver loaded successfully.");
            } catch (ClassNotFoundException e) {
                System.out.println("PostgreSQL Driver not found: " + e.getMessage());
                response.sendRedirect("PostgreSQL_Login.jsp");
                return; // Прерываем выполнение, если драйвер не найден
            }


            String[] credentials = DBConnectionUtil.getDBCredentials();
            String USER = credentials[0];
            String PASSWORD = credentials[1];

            String DB_URL = "jdbc:postgresql://localhost:5432/students_db";
            String ROOT_DB_URL = "jdbc:postgresql://localhost:5432/"; // URL для подключения к PostgreSQL, но без имени базы

            System.out.println("Received credentials: USER=" + USER + ", PASSWORD=" + PASSWORD);


            if (!isDatabaseExist(ROOT_DB_URL, USER, PASSWORD)) {
                System.out.println("Database does not exist. Creating database...");

                Create_PostgreSQL_DB.createDatabase(USER, PASSWORD);
                Create_PostgreSQL_DB.createStudentsTable(USER, PASSWORD);
                Create_PostgreSQL_DB.insertStudent(1, "Ivan", "Ivanov", "Ivanovich", "2000-01-01", "A1", USER, PASSWORD);
            }


            if (testConnection(DB_URL, USER, PASSWORD)) {
                System.out.println("Connection to database successful.");

                Create_PostgreSQL_DB.createStudentsTable(USER, PASSWORD);


                response.sendRedirect("ShowTable");
            } else {
                System.out.println("Database connection failed. Redirecting to login page.");
                response.sendRedirect("PostgreSQL_Login.jsp");
            }
        } catch (Exception e) {
            System.out.println("Error during connection process: " + e.getMessage());
            response.sendRedirect("PostgreSQL_Login.jsp");
        }
    }

    private boolean testConnection(String dbUrl, String user, String password) {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, password)) {
            return connection != null;
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    private boolean isDatabaseExist(String rootDbUrl, String user, String password) {
        String rootDbUrlWithPostgres = rootDbUrl + "postgres"; // Подключаемся к системной базе данных
        try (Connection connection = DriverManager.getConnection(rootDbUrlWithPostgres, user, password)) {
            String query = "SELECT 1 FROM pg_catalog.pg_database WHERE datname = 'students_db'";
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    if (resultSet.next()) {
                        return true;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error while checking database existence: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to root database: " + e.getMessage());
            return false;
        }
        return false;
    }


}

