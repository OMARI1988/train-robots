<!DOCTYPE html>
<%@page import="com.trainrobots.web.pages.DefaultPage"%>
<jsp:useBean id="bean" scope="page"
	class="com.trainrobots.web.pages.DefaultPage" />
<%
	bean.initiate(application);
%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Train Robots</title>
<script type="text/javascript" src="scripts/renderer.js"></script>
<style type="text/css">
body {
	background: rgb(33, 33, 33);
	font-family: Arial;
}

h1 {
	color: white;
	font-size: 12pt;
	font-weight: normal;
}
</style>
</head>
<body>
	<h1>
		Scene
		<%=bean.scene().id()%>
	</h1>
	<svg id="scene" xmlns="http://www.w3.org/2000/svg"
		xmlns:xlink="http://www.w3.org/1999/xlink" width="315" height="315"
		viewBox="0 0 120 120">
		<image xlink:href="images/robot.svg" x="-8" y="17" width="70"
			height="70" />
	</svg>
	</p>
</body>
<script type="text/javascript">
	var instructions =
<%=bean.instructions()%>
	;
	render(document.getElementById('scene'), instructions);
</script>
</html>