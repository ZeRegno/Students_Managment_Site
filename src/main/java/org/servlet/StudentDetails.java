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

@WebServlet("/StudentDetails")
public class StudentDetails extends HttpServlet {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/students_db";

    private static final String SELECT_STUDENT_BY_ID_SQL = """
        SELECT first_name, last_name, middle_name, birth_date, group_name 
        FROM students 
        WHERE id = ?;
    """;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] credentials = DBConnectionUtil.getDBCredentials();
        String USER = credentials[0];
        String PASSWORD = credentials[1];
        String studentId = request.getParameter("studentId");

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_BY_ID_SQL)) {

            preparedStatement.setInt(1, Integer.parseInt(studentId));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String middleName = resultSet.getString("middle_name");
                    String birthDate = resultSet.getDate("birth_date").toString();
                    String groupName = resultSet.getString("group_name");


                    request.setAttribute("studentId", studentId);
                    request.setAttribute("firstName", firstName);
                    request.setAttribute("lastName", lastName);
                    request.setAttribute("middleName", middleName);
                    request.setAttribute("birthDate", birthDate);
                    request.setAttribute("groupName", groupName);


                    request.getRequestDispatcher("/StudentDetails.jsp").forward(request, response);
                } else {

                    request.setAttribute("error", "Student not found.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Error retrieving student details", e);
        }
    }
}

