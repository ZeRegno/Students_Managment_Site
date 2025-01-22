package org.DB_init;

import java.sql.*;

public class Create_PostgreSQL_DB {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/"; // URL для подключения к PostgreSQL
    private static final String DATABASE_NAME = "students_db";

    private static final String CREATE_TABLE_QUERY = """
       CREATE TABLE IF NOT EXISTS students (
          id INT PRIMARY KEY,
          first_name VARCHAR(50) NOT NULL,
          last_name VARCHAR(50) NOT NULL,
          middle_name VARCHAR(50),
          birth_date DATE NOT NULL,
          group_name VARCHAR(50) NOT NULL
                );
    """;
    private static final String INSERT_STUDENT_QUERY =
            """
       INSERT INTO students (id, first_name, last_name, middle_name, birth_date,
            group_name)
       VALUES (?, ?, ?, ?
            , ?, ?)
    """;
    // Метод для создания базы данных
    public static void createDatabase(String user, String password) throws SQLException {
        // Регистрируем драйвер PostgreSQL
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found: " + e.getMessage());
            return;
        }

        // Подключаемся к базе данных postgres (без указания имени базы данных)
        String rootDbUrl = DB_URL + "postgres"; // Подключение к системной базе данных
        try (Connection connection = DriverManager.getConnection(rootDbUrl, user, password);
             Statement statement = connection.createStatement()) {

            // SQL-запрос для создания базы данных
            String createDatabaseQuery = "CREATE DATABASE " + DATABASE_NAME;
            statement.executeUpdate(createDatabaseQuery);
            System.out.println("Database \"" + DATABASE_NAME + "\" created successfully.");
        } catch (SQLException e) {
            if ("42P04".equals(e.getSQLState())) { // Код ошибки PostgreSQL: база данных уже существует
                System.out.println("Database \"" + DATABASE_NAME + "\" already exists.");
            } else {
                System.out.println("Error while creating database: " + e.getMessage());
                throw e;
            }
        }
    }

    // Метод для создания таблицы "students" в уже существующей базе данных
    public static void createStudentsTable(String user, String password) throws SQLException {
        // Регистрируем драйвер PostgreSQL
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found: " + e.getMessage());
            return;
        }

        String databaseUrl = DB_URL + DATABASE_NAME; // Подключаемся к только что созданной базе данных
        try (Connection connection = DriverManager.getConnection(databaseUrl, user, password);
             Statement statement = connection.createStatement()) {

            // Выполняем запрос для создания таблицы "students"
            statement.executeUpdate(CREATE_TABLE_QUERY);
            System.out.println("Table \"students\" created successfully in database \"" + DATABASE_NAME + "\".");
        } catch (SQLException e) {
            System.out.println("Error while creating table: " + e.getMessage());
            throw e;
        }
    }
    public static boolean isStudentExist(String firstName, String lastName, String middleName, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM students WHERE first_name = ? AND last_name = ? AND middle_name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, middleName);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;  // Если найдено хотя бы одно совпадение, возвращаем true
                }
            }
        }
        return false;  // Если студент с такими данными не найден
    }

    // Метод для вставки студента
    public static void insertStudent(int id, String firstName, String lastName, String middleName, String birthDate, String group, String user, String password) {
        String insertQuery = "INSERT INTO students (id, first_name, last_name, middle_name, birth_date, student_group) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/students_db", user, password)) {
            // Сначала проверяем, есть ли такой студент
            if (isStudentExist(firstName, lastName, middleName, connection)) {
                System.out.println("Student already exists in the database.");
                return;  // Прерываем выполнение, если студент уже существует
            }

            // Если такого студента нет, вставляем запись
            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                stmt.setInt(1, id);
                stmt.setString(2, firstName);
                stmt.setString(3, lastName);
                stmt.setString(4, middleName);
                stmt.setString(5, birthDate);
                stmt.setString(6, group);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Student inserted successfully.");
                } else {
                    System.out.println("Error inserting student.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while inserting student: " + e.getMessage());
        }
    }
}


