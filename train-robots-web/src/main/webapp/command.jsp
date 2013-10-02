<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="command" class="com.trainrobots.web.pages.CommandPage" />
<%
	command.initiate(pageContext, request.getParameter("id"));
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
	margin-top: 1em;
}

p.move {
	color: rgb(141, 244, 50);
	font-size: 12pt;
	font-weight: bold;
	margin: 0.2em;
	margin-left: 0.4em;
}

p.info {
	margin: 0;
	margin-top: 0.4em;
	font-size: 12pt;
	color: rgb(200, 200, 200);
	line-height: 16pt;
}

table.rclTable {
	margin-top: 2.5em;
}

table.rclTable th {
	color: white;
	font-size: 12pt;
	font-weight: bold;
	padding-right: 3em;
	padding-bottom: 0.4em;
	text-align: left;
}

table.rclTable td {
	font-size: 12pt;
	color: rgb(200, 200, 200);
	padding-right: 3em;
	text-align: left;
}

table.rclTable td.rcl {
	font-family: Courier New;
}
</style>
<title>Robot Commands Treebank - Command <%=command.getId()%></title>
</head>
<body>
	<h1>train robots</h1>
	<p class="tagline">help teach robots to become smart as humans</p>
	<h2>
		Robot Commands Treebank - Command
		<%=command.getId()%></h2>
	<p class="info"><%=command.getDescription()%></p>
	<h2>Semantic Parse Tree</h2>
	<p>
		<img src="<%=command.getTreeImage()%>" />
	</p>
	<table class="rclTable" cellspacing="0" cellpadding="0">
		<tr>
			<th>Robot Control Language</th>
			<th>Word Alignment
			</td>
		</tr><%=command.getRclLines()%></table>
	<h2>Spatial Context</h2>
	<table id='scene' cellspacing='0' cellpadding='2'>
		<tr>
			<td class="left-image"><p class="move">Before the command</p> <img
				src="<%=command.getImage1()%>" width="325" height="350" /></td>
			<td><img src="/images/right-arrow.png"
				style="margin-left: 20px; margin-right: 20px;" /></td>
			<td class="right-image"><p class="move">After the command</p> <img
				src="<%=command.getImage2()%>" width="325" height="350" /></td>
		</tr>
	</table>
</body>
</html>