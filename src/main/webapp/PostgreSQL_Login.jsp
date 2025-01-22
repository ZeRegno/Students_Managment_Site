<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Введите данные для подключения</title>
</head>
<body>
<h2>Ввод данных Пользователя PostgreSQL</h2>
<p>
    <strong>
        Сайт использует базу данных PostgreSQL, для корректной работы нему необходимо развернуть базу данных локально.
    </strong>
</p>
<p>
    <strong>
        Пожалуйста, введите имя и пароль суперпользователя PostgreSQL, с правами на создание базы данных
    </strong>
</p>
<% if (request.getAttribute("error") != null) { %>
<p style="color: red;"><%= request.getAttribute("error") %></p>
<% } %>

<form action="SaveCredentials" method="post">
    <label for="username">Имя пользователя:</label>
    <input type="text" id="username" name="username" required><br><br>

    <label for="password">Пароль:</label>
    <input type="password" id="password" name="password" required><br><br>

    <input type="submit" value="Сохранить и выполнить пробное подключение">
</form>
</body>
</html>