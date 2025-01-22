<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Student Details</title>
</head>
<body>
<h2>Более подробная информация о студенте</h2>
<p><strong>Имя:</strong> ${firstName}</p>
<p><strong>Фамилия:</strong> ${lastName}</p>
<p><strong>Отчество:</strong> ${middleName}</p>
<p><strong>Дата Рождения:</strong> ${birthDate}</p>
<p><strong>Название группы:</strong> ${groupName}</p>

<!-- Кнопка для удаления студента -->
<form action="RemoveStudent" method="post">
    <input type="hidden" name="studentId" value="${studentId}">
    <button type="submit">Удалить из списка</button>
</form>

<a href="ShowTable">
    <button type="button">Назад к списку студентов</button>
</a>
</body>
</html>
