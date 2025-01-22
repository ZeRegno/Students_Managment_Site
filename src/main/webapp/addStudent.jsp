<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Страница Добавления Студента</title>
</head>
<body>
<style>
    /* Убираем стрелочки у поля ввода типа number */
    input[type="number"]::-webkit-outer-spin-button,
    input[type="number"]::-webkit-inner-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }

    input[type="number"] {
        -moz-appearance: textfield;
    }
</style>
<form action="AddStudent" method="post">
    <label for="id">Введите уникальный номер студента:</label><br>
    <input type="number" id="id" name="id"
           value="<%= request.getSession().getAttribute("id") != null ? request.getSession().getAttribute("id") : "" %>"
           required><br><br>

    <label for="firstName">Имя:</label><br>
    <input type="text" id="firstName" name="firstName"
           value="<%= request.getSession().getAttribute("firstName") != null ? request.getSession().getAttribute("firstName") : "" %>"
           required><br><br>

    <label for="lastName">Фамилия:</label><br>
    <input type="text" id="lastName" name="lastName"
           value="<%= request.getSession().getAttribute("lastName") != null ? request.getSession().getAttribute("lastName") : "" %>"
           required><br><br>

    <label for="middleName">Отчество:</label><br>
    <input type="text" id="middleName" name="middleName"
           value="<%= request.getSession().getAttribute("middleName") != null ? request.getSession().getAttribute("middleName") : "" %>"
           required><br><br>

    <label for="birthDate">Дата Рождения:</label><br>
    <input type="date" id="birthDate" name="birthDate"
           value="<%= request.getSession().getAttribute("birthDate") != null ? request.getSession().getAttribute("birthDate") : "" %>"
           required>
    <%
        // Выводим ошибку формата даты, если она существует
        String birthDateError = (String) request.getSession().getAttribute("birthDateError");
        if (birthDateError != null) {
    %>
    <span style="color:red;"><%= birthDateError %></span>
    <%
            request.getSession().removeAttribute("birthDateError"); // Удаляем сообщение после отображения
        }
    %>
    <br><br>

    <label for="groupName">Название группы:</label><br>
    <input type="text" id="groupName" name="groupName"
           value="<%= request.getSession().getAttribute("groupName") != null ? request.getSession().getAttribute("groupName") : "" %>"
           required><br><br>

    <button type="submit">Подтвердить ввод данных</button>
</form>
<form action="ShowTable" method="get" style="margin-top: 20px;">
    <button type="submit">Назад к списку студентов</button>
</form>
</body>
</html>