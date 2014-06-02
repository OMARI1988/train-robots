<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="treebank"
	class="com.trainrobots.web.pages.TreebankPage" />
<%
	treebank.initiate(pageContext);
%>
<html>
<head>
<style type="text/css">
#information {
	color: rgb(200, 200, 200);
	font-size: 12pt;
	line-height: 16pt;
	width: 600px;
}

h2 {
	color: rgb(141, 244, 50);
	font-size: 12pt;
	font-weight: bold;
	margin-top: 2.5em;
	margin-bottom: 0;
}

table.sceneTable td {
	color: rgb(200, 200, 200);
	padding-right: 1em;
}
</style>
<title>Robot Commands Treebank</title>
</head>
<body>
	<h1>train robots</h1>
	<p class="tagline">help teach robots to become smart as humans</p>
	<div id="information">
		<h2>Robot Commands Treebank</h2>
		<p>
			The Robot Commands Treebank provides semantic annotation for robotic
			spatial commands. Collected through crowdsourcing, the treebank
			provides annotation for
			<%=treebank.formatCommandCount()%>
			sentences (<%=treebank.formatWordCount()%>
			words). Commands are organized into scenes. These are pairs of images
			showing a game board before and after each command.
		</p>
		<p>In the treebank, natural language sentences have been mapped to
			a compositional Robot Control Language (RCL). Each RCL statement
			forms a semantic tree that provides instructions to a spatial planner
			for command execution. The treebank also provides word-aligned
			annotations between leaf nodes in RCL statements and words in the
			corresponding annotated sentence.</p>
		<p>
			The semantic treebank is freely available for research. <a
				href="/contact.jsp">Contact us</a> for more information.
		</p>
		<p>
			Click on a scene number below to browse the treebank, or view a <a
				href="/scene.jsp">random scene</a>.
		</p>
		<h2 style="margin-bottom: 1em">Scenes</h2>
		<%=treebank.getScenes()%>
	</div>
</body>
</html>