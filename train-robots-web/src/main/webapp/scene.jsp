<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="scene" class="com.trainrobots.web.pages.ScenePage" />
<%scene.initiate(
		pageContext,
		request.getParameter("id"));%>
<html>
<head>
<style type="text/css">
h2 {
	color: white;
	font-size: 12pt;
	font-weight: bold;
	margin-top: 2.5em;
	margin-bottom: 1.5em;
}

table#scene {
	background: black;
}

p.move {
	color: rgb(141, 244, 50);
	font-size: 12pt;
	font-weight: bold;
	margin: 0.2em;
	margin-left: 0.4em;
}

p.text {
	margin-top: 2em;
	margin-bottom: 0.25em;
	color: white;
	line-height: 16pt;
}

p.info {
	margin: 0;
	font-size: 11pt;
	color: rgb(200, 200, 200);
}
</style>
<title>Train Robots - Scene</title>
</head>
<body>
	<h1>train robots</h1>
	<p class="tagline">help teach robots to become smart as humans</p>
	<h2>Scene <%=scene.getSceneNumber()%></h2>
	<table id='scene' cellspacing='0' cellpadding='2'>
	<tr>
		<td class="left-image"><p class="move">Before the command</p><img src="<%=scene.getImage1()%>" width="325" height="350"/></td>
		<td><img src="/images/right-arrow.png" style="margin-left:20px; margin-right:20px;"/></td>
		<td class="right-image"><p class="move">After the command</p><img src="<%=scene.getImage2()%>" width="325" height="350"/></td></tr>
	</table>
	<p>
		<img src='/images/layout.png' width='280' height='180'/>
	</p>
	<%=scene.getCommands()%>
</body>
</html>