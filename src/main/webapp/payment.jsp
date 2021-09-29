<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <title>Payment</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>

<script>
    function validate() {
        let result = true;
        let answer = '';
        let elements = [$("#username"), $("#phone"), $("#email")];
        for (let i = 0; i < elements.length; i++) {
            if (elements[i].val() === '') {
                answer += "Пожалуйста, введите "  + elements[i].attr("placeholder").toLowerCase() + "\n";
                result = false;
            }
        }
        if (!result) {
            alert(answer);
        }
        return result;
    }
</script>

<%
    String ticketValue = request.getParameter("ticket");
    String ticketRow = String.valueOf(ticketValue.charAt(0));
    String ticketCell = String.valueOf(ticketValue.charAt(1));
%>

<div class="container">
    <div class="row pt-3">
        <h3>
            Вы выбрали ряд <%=ticketRow%> место <%=ticketCell%>, Сумма : 500 рублей.
        </h3>
    </div>
    <div class="row">
        <form id="currentForm" action="<%=request.getContextPath()%>/payment.do?ticket=<%=ticketValue%>" method="post">
            <div class="form-group">
                <label>ФИО</label>
                <input type="text" class="form-control" name="username" placeholder="ФИО">
            </div>
            <div class="form-group">
                <label>Номер телефона</label>
                <input type="text" class="form-control" name="phone" placeholder="Номер телефона">
            </div>
            <div class="form-group">
                <label>Электронная почта</label>
                <input type="text" class="form-control" name="email" placeholder="Электронная почта">
            </div>
            <button type="submit" class="btn btn-success" onclick="return validate();">Оплатить</button>
        </form>
    </div>
    <div>
        <% if (request.getAttribute("purchase") != null) { %>
        К сожалению, пока Вы оформляли данный билет, его уже купили...
        <% } %>
    </div>
    <div>
        <a class="nav-link" href="<%=request.getContextPath()%>/index.html">Назад на страницу выбора билетов</a>
    </div>

</div>
</body>
</html>