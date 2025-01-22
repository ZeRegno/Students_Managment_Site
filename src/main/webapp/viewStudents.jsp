<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Students</title>
</head>
<body>
<h2>Список студентов</h2>
<table border="1">
    <tr>
        <th>ФИО</th>
        <th>Группа</th>
    </tr>
    <%

        List<org.servlet.ViewStudentsTable.Student> students =
                (List<org.servlet.ViewStudentsTable.Student>) request.getAttribute("students");

        if (students != null && !students.isEmpty()) {
            for (org.servlet.ViewStudentsTable.Student student : students) {
    %>
    <tr>
        <td>
            <a href="StudentDetails?studentId=<%= student.getId() %>">
                <%= student.getLastName() %> <%= student.getFirstName() %>
            </a>
        </td>
        <td><%= student.getGroupName() %></td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="2">Информация не найдена</td>
    </tr>
    <%
        }
    %>
</table>

<a href="addStudent.jsp">
    <button type="button">Добавить студента</button>
</a>

</body>
</html>