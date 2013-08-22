<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="forgot" class="com.trainrobots.web.pages.ForgotPasswordPage" />
<%forgot.initiate(pageContext, request.getMethod(), request.getParameter("emailTextBox"));%>
<html>
<head>
<title>Train Robots - Forgot Password</title>
<style type="text/css">
h2 {
	color: white;
	font-size: 20pt;
	font-weight: normal;
	margin-top: 2.5em;
	margin-bottom: 0em;
}

p.formRow {
	margin: 0;
	margin-bottom: 0.6em;
}

p.error {
	color: orange;
	margin-top: 1.8em;
}

.hint {
	color: rgb(200, 200, 200);
	margin: 0;
	margin-top: 2em;
	margin-bottom: 0.5em;
	font-size: 11pt;
}

.info {
	color: rgb(200, 200, 200);
	margin: 0;
	margin-top: 0.6em;
	margin-bottom: 3em;
	font-size: 12pt;
	line-height: 16pt;
}
</style>
</head>
<body>
	<h1>train robots</h1>
	<p class="tagline">help teach robots to become smart as humans</p>
	<form method="post">
		<h2>Forgot your password?</h2>
		<p class="info">Don't worry. Enter your email address below and<br/>we'll send you password reset instructions.</p>
		<p class="hint">Email address</p>
		<p class="formRow">
			<input class="textBox" type="text" name="emailTextBox" value="<%=forgot.getEmail() != null ? forgot.getEmail() : ""%>"/>
		</p>
		<p>
			<input class="formButton" value="Send" type="submit" /> <input
				class="formButton" value="Cancel" type="button"
				style="margin-left: 0.8em;"
				onclick="window.location='/'" />
		</p>
		<% if (forgot.getError() != null) { %><p class="error"><%=forgot.getError()%></p><br/><%}%>
	</form>
</body>
</html>