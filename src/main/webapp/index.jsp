<%@page contentType="text/html; ISO-8859-1" pageEncoding="utf-8" isELIgnored="false" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>系统主页</title>
</head>
<body>
    <h1>系统主页</h1>
    <a href="${pageContext.request.contextPath}/user/logout">退出</a>

    <ul>
        <shiro:hasAnyRoles name="user,admin">
            <li><a href="">用户管理</a>
                <ul>
                    <shiro:hasPermission name="user:add:*"><li>增</li></shiro:hasPermission>
                    <shiro:hasPermission name="user:delete:*"><li>删</li></shiro:hasPermission>
                    <shiro:hasPermission name="user:update:*"><li>改</li></shiro:hasPermission>
                    <shiro:hasPermission name="order:*:*"><li>查</li></shiro:hasPermission>
                </ul>
            </li>
        </shiro:hasAnyRoles>

        <shiro:hasRole name="admin">
            <li><a href="">商品管理</a></li>
            <li><a href="">订单管理</a></li>
            <li><a href="">物流管理</a></li>
        </shiro:hasRole>
    </ul>
</body>
</html>