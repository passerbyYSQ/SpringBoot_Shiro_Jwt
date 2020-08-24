<%@page contentType="text/html; ISO-8859-1" pageEncoding="utf-8" isELIgnored="false" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>用户注册</title>
</head>
<body>
    <h1>用户注册</h1>

    <form action="${pageContext.request.contextPath}/user/register" method="post">
        用户名：<input type="text" name="username"><br>
        密码：<input type="password" name="password">
        <input type="submit" value="注册">
    </form>

</body>
</html>