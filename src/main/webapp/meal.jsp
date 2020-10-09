<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit or create meal</h2>
<c:if test="${meal != null}">
    <c:set var="dt" value="${meal.dateTime}"/>
    <c:set var="descr" value="${meal.description}"/>
    <c:set var="cal" value="${meal.calories}"/>
    <c:set var="id" value="${meal.id}"/>
</c:if>
<form method="post" action="meals">
    <table border="0">
        <tr>
            <td>
                DateTime:
            </td>
            <td>
                <input type="datetime-local" name="dateTime" value="${dt}">
            </td>
        </tr>
        <tr>
            <td>
                Description:
            </td>
            <td>
                <input type="text" size="100" name="description" value="${descr}">
        </tr>
        <tr>
            <td>
                Calories:
            </td>
            <td>
                <input type="number" name="calories" value="${cal}">
            </td>
        </tr>
    </table>
    <input type="hidden" name="id" value="${id}">
    <button type="submit">Save</button>
    <button onclick="window.history.back()">Cancel</button>
</form>
</body>
</html>
