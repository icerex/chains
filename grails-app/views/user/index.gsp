<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 16/1/20
  Time: 下午8:41
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>${user.nickname}</title>
    <meta name="Keywords" content="时光鸡">
    <meta name="Description" content="时光鸡">
    <meta name="viewport" content="width=750, user-scalable=no, target-densitydpi=device-dpi">
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <link href="//assets.teamlinking.com/assets/css/index.css" rel="stylesheet">
</head>
<body>
    <header>
        <h1 href="" id="avatar"></h1>
    </header>
    <div id="root" class="contain-line">
        <ul id="nav-container" class="l1"></ul>
        <ul id="subject-container" class="l1 incipient"></ul>
    </div>
    <div id="data-area">
        <em id="id">${user.id}</em>
        <em id="avatarUrl">${user.headImgUrl}</em>
    </div>
    <script src="//assets.teamlinking.com/assets/lib/js/jquery.min.js"></script>    
    <script src="//assets.teamlinking.com/assets/js/data.js"></script>
    <script src="//assets.teamlinking.com/assets/js/index.js"></script>
</body>
</html>