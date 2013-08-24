<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="org.joda.time.DateTimeConstants"%>
<%@page import="org.joda.time.DateTimeZone"%>
<%
response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
response.setDateHeader("Expires", 0); // Proxies.
%>
<html>
<head>
<style type="text/css">
h2 {
	color: white;
	font-size: 14pt;
	font-weight: normal;
	margin-top: 4.2em;
	margin-bottom: 0;
}

.player {
	color: rgb(141, 244, 50);
	margin: 0;
	margin-top: 0.3em;
}
</style>
<title>Train Robots - Robot Commands Annotation Game</title>
</head>
<body>
	<table id="main" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<h1>train robots</h1>
				<p class="tagline">help teach robots to become smart as humans</p>
				<table class="menu" cellspacing="0" cellpadding="0">
					<tr>
						<td><img src="images/next.png" width="39" height="39" /></td>
						<td><a href="/game">play</a>
						</td>
					</tr>
					<tr>
						<td><img src="images/info.png" width="39" height="39" /></td>
						<td><a href="instructions.jsp">instructions</a>
						</td>
					</tr>
					<tr>
						<td><img src="images/contact.png" width="39" height="39" />
						</td>
						<td><a href="contact.jsp">contact</a>
						</td>
					</tr>
				</table>
				<h2>robot intelligence at <%=Math.round(85 + 7 * Math.sin(new DateTime(DateTimeZone.UTC).getMillis() / 4000000.0))%>%</h2>
				<p class="tagline" />
				<%int t = (int) (new DateTime(DateTimeZone.UTC).getMillis() / DateTimeConstants.MILLIS_PER_MINUTE);%>
				<%=Math.round(200 + 60 * Math.sin(0.1 * t) + 30 * Math.sin(0.01 * t) + 20 * Math.sin(0.05 * t))%> players online
				</p>
				<p class="player" style="margin-top: 2.8em">today's best player
					is</p>
				<p class="player">
					<b>itkonen513</b> with 17,321 points!
				</p></td>
			<td><img style="margin-left: 3em" src="images/arm-large.png"
				width="282" height="288" />
			</td>
		</tr>
	</table>
</body>
</html>