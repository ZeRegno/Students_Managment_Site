package org.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.Util_methods.DBConnectionUtil;

@WebServlet("/ShowTable")
public class ViewStudentsTable extends HttpServlet {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/students_db";
    private static final String SELECT_ALL_STUDENTS_SQL = """
        SELECT id, first_name, last_name, group_name FROM students;
    """;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Student> students = getStudentsList();

        request.setAttribute("students", students);
        request.getRequestDispatcher("/viewStudents.jsp").forward(request, response);
    }

    public List<Student> getStudentsList() throws ServletException {
        List<Student> students = new ArrayList<>();
        try {
            String[] credentials = DBConnectionUtil.getDBCredentials(); // Может выбросить IOException
            String USER = credentials[0];
            String PASSWORD = credentials[1];

            try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(SELECT_ALL_STUDENTS_SQL)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String groupName = resultSet.getString("group_name");

                    students.add(new Student(id, firstName, lastName, groupName));
                }
            }
        } catch (IOException e) {
            throw new ServletException("Error while reading database credentials", e);
        } catch (SQLException e) {
            throw new ServletException("Error while retrieving students from the database", e);
        }
        return students;
    }

    public static class Student {
        private final int id;
        private final String firstName;
        private final String lastName;
        private final String groupName;

        public Student(int id, String firstName, String lastName, String groupName) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.groupName = groupName;
        }

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getGroupName() {
            return groupName;
        }
    }
}


