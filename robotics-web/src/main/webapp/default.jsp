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
<script type="text/javascript" src="scripts/chat.js"></script>
<script type="text/javascript" src="scripts/renderer.js"></script>
<style type="text/css">
body {
	background: rgb(33, 33, 33);
	font-family: Arial;
	margin-left: 5em;
	margin-right: 5em;
}

h1 {
	color: white;
	font-size: 24pt;
	font-weight: normal;
	margin-top: 1.2em;
	margin-bottom: 0;
}

p.tagline {
	color: rgb(200, 200, 200);
	margin-top: 0.15em
}

input.textInput {
	font-family: Arial;
	font-size: 10pt;
	padding: 0.2em;
	padding-left: 0.3em;
	margin-left: 0;
	width: 500px;
}

input.buttonInput {
	margin-top: 0.2em;
	margin-left: 0;
}

div.chatLog {
	height: 240px;
	width: 500px;
	margin: 0;
	margin-bottom: 1em;
	border: 1px solid gray;
	padding: 0.3em;
	padding-left: 0.4em;
	overflow-y: auto;
	background: rgb(20, 20, 20);
}

span.name {
	color: white;
}

p.chat {
	color: rgb(200, 200, 200);
	margin-top: 0.2em;
	margin-bottom: 0;
	font-size: 10pt;
}
</style>
</head>
<body>
	<h1>train robots</h1>
	<p class="tagline">help teach robots to become smart as humans</p>
	<br />
	<br />
	<table cellpadding="0" cellspacing="0">
		<tr>
			<td style="vertical-align: top"><svg id="scene"
					xmlns="http://www.w3.org/2000/svg"
					xmlns:xlink="http://www.w3.org/1999/xlink" width="315" height="315"
					viewBox="0 0 120 120">
			<image xlink:href="images/robot.svg" x="-8" y="17" width="70"
						height="70" />
		</svg></td>
			<td style="vertical-align: top; padding-left: 3em;">
				<div class="chatLog" id="chatLog"></div> <input type="text"
				id="inputBox" class="textInput"></input><br /> <input
				id="sayButton" type="button" value="Say" class="buttonInput"
				onclick="chat()" /> <br /> <br /> <img id="loadingImage"
				src="images/loading.gif" style="visibility: hidden" />
			</td>
		</tr>
	</table>
</body>
<script type="text/javascript">
	document.getElementById('inputBox').onkeypress = function(e) {
		if (!e) {
			e = window.event;
		}
		var keyCode = e.keyCode || e.which;
		if (keyCode == '13') {
			chat();
			return false;
		}
	}

	var instructions =
<%=bean.instructions()%>
	;
	render(instructions);
</script>
</html>