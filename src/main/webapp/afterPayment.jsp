<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <title>AfterPayment</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>

<div class="container">
    <div>
        <% if (request.getAttribute("purchase") != null) { %>
        К сожалению, пока Вы оформляли данный билет, его уже купили...
        <% } else { %>
        Покупка прошла успешно!
        <% } %>
    </div>
    <div>
        <a class="nav-link" href="<%=request.getContextPath()%>/index.html">Назад на страницу выбора билетов</a>
    </div>
</div>
</body>
</html>
