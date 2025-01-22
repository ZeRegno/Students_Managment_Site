<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Database Connection Status</title>
</head>
<body>
<h1>Подключение к базе данных</h1>
<p>
    <strong>
        Это начальная страница, которая выполняет проверку статуса подключения к базе данных PostgreSQL. Нажмите кнопку ниже для проверки подключения.
    </strong>
</p>
<p>
    <strong>
        <%= request.getAttribute("dbStatus") != null ? request.getAttribute("dbStatus") : "(Нет сообщения о статусе базы данных)" %>
    </strong>
</p>


<form action="TestDBConnection" method="get">
    <button type="submit">Выполнить пробное подключение к базе данных</button>
</form>
</body>
</html>