<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 09.11.2017
  Time: 14:15
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <title>Meals</title>

    <style type="text/css">

body
{
    background-color: gainsboro;
}

        .tg {
            border:3px solid black;
            border-collapse: collapse;
            border-spacing: 0;
        }

        .tg td {
            font-family: Arial, sans-serif;
            font-size: 14px;
            padding: 10px 5px;
            border:3px solid black;
            overflow: hidden;
            word-break: normal;
            color: black;
        }

        .tg th {
            font-family: Arial, sans-serif;
            font-size: 14px;
            padding: 10px 5px;
            border:3px solid black;
            background-color:grey;
            overflow: hidden;
            word-break: normal;
            color: black;
        }
    </style>

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<table width="100%" class="tg">
        <jsp:useBean id="mealsWithExceeded" scope="request" type="java.util.List"/>
    <tr>
        <th width="200">Date</th>
        <th width="90">Description</th>
        <th width="90">Calories</th>
        <th width="90">Exceed</th>
    </tr>

    <c:if test="${mealsWithExceeded.size()>0}">
    <c:forEach items="${mealsWithExceeded}" var="mealWithExceed">

        <c:if test="${mealWithExceed.isExceed()}">
            <tr bgcolor=red>
                <td>${mealWithExceed.getDateTime()}</td>
                <td>${mealWithExceed.getDescription()}</td>
                <td>${mealWithExceed.getCalories()}</td>
                <td>${mealWithExceed.isExceed()}</td>

            </tr>
        </c:if>

        <c:if test="${!mealWithExceed.isExceed()}">
            <tr bgcolor=green>
                <td>${mealWithExceed.getDateTime()}</td>
                <td>${mealWithExceed.getDescription()}</td>
                <td>${mealWithExceed.getCalories()}</td>
                <td>${mealWithExceed.isExceed()}</td>

            </tr>
        </c:if>
    </c:forEach>
    </c:if>
    </table>


</body>
</html>
