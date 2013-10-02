<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="scene" class="com.trainrobots.web.pages.ScenePage" />
<%
	scene.initiate(pageContext, request.getParameter("id"));
%>
<html>
<head>
<style type="text/css">
h2 {
	color: white;
	font-size: 12pt;
	font-weight: bold;
	margin-top: 2.5em;
	margin-bottom: 0em;
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

p.information {
	margin-top: 0.4em;
	color: rgb(200, 200, 200);
	font-size: 12pt;
	line-height: 16pt;
}

table.commandTable td {
	color: rgb(200, 200, 200);
	line-height: 16pt;
	vertical-align: top;
	padding-top: 0.5em;
	padding-right: 1em;
}
</style>
<title>Robot Commands Treebank - Scene <%=scene.getSceneNumber()%></title>
</head>
<body>
	<h1>train robots</h1>
	<p class="tagline">help teach robots to become smart as humans</p>
	<h2>
		Robot Commands Treebank - Scene
		<%=scene.getSceneNumber()%></h2>
	<p class="information">Click a commmand number below to view
		semantic annotation.</p>
	<table id='scene' cellspacing='0' cellpadding='2'>
		<tr>
			<td class="left-image"><p class="move">Before the command</p> <img
				src="<%=scene.getImage1()%>" width="325" height="350" />
			</td>
			<td><img src="/images/right-arrow.png"
				style="margin-left: 20px; margin-right: 20px;" />
			</td>
			<td class="right-image"><p class="move">After the command</p> <img
				src="<%=scene.getImage2()%>" width="325" height="350" />
			</td>
		</tr>
	</table>
	<h2><%=scene.formatCommandsHeader()%></h2>
	<%=scene.getCommands()%>
</body>
</html>