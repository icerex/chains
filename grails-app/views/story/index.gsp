<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 16/1/20
  Time: 上午11:09
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>${story.title}</title>
    <meta name="Keywords" content="时光鸡">
    <meta name="Description" content="时光鸡">
    <meta name="viewport" content="width=750, user-scalable=no, target-densitydpi=device-dpi">
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <link href="//assets.teamlinking.com/assets/css/index.css" rel="stylesheet">
    <link rel="stylesheet" href="//assets.teamlinking.com/assets/lib/css/video-js.css">
</head>
<body id="detail">
    <header>
        <h1 href="" id="avatar"></h1>
    </header>
    <ul id="detail-list" class="contain-line"></ul>
    <div id="data-area">
        <em id="story-id">${story.id}</em>
        <em id="header-bg">${story.pic}</em>
        <em id="avatar-url">${user.headImgUrl}</em>
        <em id="sequence">${desc}</em>
        <span>vo:${vo}<br>hasNext:${hasNext}</span>
    </div>
    <script src="//assets.teamlinking.com/assets/lib/js/jquery.min.js"></script>
    <script src="//assets.teamlinking.com/assets/lib/js/video.js"></script>
    <script src="//assets.teamlinking.com/assets/lib/js/videojs.hls.min.js"></script>
    <script src="//assets.teamlinking.com/assets/js/data.js"></script>
    <script src="//assets.teamlinking.com/assets/js/detail.js"></script>
</body>
</html>
