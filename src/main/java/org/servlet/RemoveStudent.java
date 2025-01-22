package org.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.Util_methods.DBConnectionUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/RemoveStudent")
public class RemoveStudent extends HttpServlet {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/students_db";
    private static final String CHECK_STUDENT_SQL = "SELECT 1 FROM students WHERE id = ?";
    private static final String DELETE_STUDENT_SQL = "DELETE FROM students WHERE id = ?";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] credentials = DBConnectionUtil.getDBCredentials();
        String USER = credentials[0];
        String PASSWORD = credentials[1];
        String studentId = request.getParameter("studentId");


        if (studentId == null || studentId.isEmpty()) {
            System.out.println("Ошибка получения ID студента");
            response.sendRedirect("ShowTable");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            try (PreparedStatement checkStatement = connection.prepareStatement(CHECK_STUDENT_SQL)) {
                checkStatement.setInt(1, Integer.parseInt(studentId));
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (!resultSet.next()) {

                        System.out.println("Студент не найден.");
                        response.sendRedirect("ShowTable");
                        return;
                    }
                }
            }


            try (PreparedStatement deleteStatement = connection.prepareStatement(DELETE_STUDENT_SQL)) {
                deleteStatement.setInt(1, Integer.parseInt(studentId));
                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    response.sendRedirect("ShowTable");
                } else {
                    System.out.println("Ошибка процесса удаления студента");
                    response.sendRedirect("ShowTable");  // Перенаправляем на страницу без ошибки
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Неправильный формат ID");
            e.printStackTrace();
        }
    }
}