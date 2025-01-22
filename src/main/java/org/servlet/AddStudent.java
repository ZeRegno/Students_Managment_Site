package org.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.Util_methods.DBConnectionUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/AddStudent")
public class AddStudent extends HttpServlet {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/students_db";

    private static final String CHECK_ID_SQL = """
        SELECT id FROM students WHERE id = ?;
    """;

    private static final String INSERT_STUDENT_SQL = """
        INSERT INTO students (id, first_name, last_name, middle_name, birth_date, group_name)
        VALUES (?, ?, ?, ?, ?, ?);
    """;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] credentials = DBConnectionUtil.getDBCredentials();
        String USER = credentials[0];
        String PASSWORD = credentials[1];
        request.setCharacterEncoding("UTF-8");


        int studentId = Integer.parseInt(request.getParameter("id"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String middleName = request.getParameter("middleName");
        String birthDate = request.getParameter("birthDate");
        String groupName = request.getParameter("groupName");


        java.sql.Date sqlBirthDate = null;
        String birthDateError = null;
        try {
            sqlBirthDate = java.sql.Date.valueOf(birthDate);
        } catch (IllegalArgumentException e) {
            birthDateError = "Неправильный формат даты!.";
        }

        if (birthDateError != null) {

            request.getSession().setAttribute("birthDateError", birthDateError);
            HttpSession session = request.getSession();
            session.setAttribute("id", studentId);
            session.setAttribute("firstName", firstName);
            session.setAttribute("lastName", lastName);
            session.setAttribute("middleName", middleName);
            session.setAttribute("birthDate", birthDate);
            session.setAttribute("groupName", groupName);
            response.sendRedirect("addStudent.jsp");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            try (PreparedStatement checkStatement = connection.prepareStatement(CHECK_ID_SQL)) {
                checkStatement.setInt(1, studentId);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {

                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.println("<h2>Ошибка: Студент с таким ID уже существует</h2>");
                    out.println("<a href=\"ShowTable\">Назад к списку</a>");
                    return;
                }
            }


            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENT_SQL)) {
                preparedStatement.setInt(1, studentId);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.setString(4, middleName);
                preparedStatement.setDate(5, sqlBirthDate);
                preparedStatement.setString(6, groupName);

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    response.sendRedirect("ShowTable");
                } else {

                    response.setContentType("text/html;charset=UTF-8");
                    PrintWriter out = response.getWriter();
                    out.println("<h2>Не удалось добавит студента в список.</h2>");
                    out.println("<a href=\"ShowTable\">Back to Students List</a>");
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Ошибка добавления студента в базу данных", e);
        }
    }
}