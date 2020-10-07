<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<html>
<head>
    <title>Meals</title>
    <style type="text/css">
        table {
            border-collapse: collapse;
            border: 1px solid black;
        }
        td, th {
            border: 1px solid black;
            padding: 5px;
        }
        th {
            text-align: center;
        }
        tr.red {
            color: red;
        }
        tr.green {
            color: green;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <tr class="${meal.excess ? 'red' : 'green'}">
            <td>
                <javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm"/>
            </td>
            <td>
                ${meal.description}
            </td>
            <td>
                ${meal.calories}
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
