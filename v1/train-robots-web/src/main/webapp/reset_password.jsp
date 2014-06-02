<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="reset" class="com.trainrobots.web.pages.ResetPasswordPage" />
<%reset.initiate(
		pageContext,
		request.getMethod(),
		request.getParameter("token"),
		request.getParameter("passwordTextBox"),
		request.getParameter("confirmPasswordTextBox"));%>
<html>
<head>
<title>Train Robots - Reset Password</title>
<style type="text/css">
h2 {
	color: white;
	font-size: 20pt;
	font-weight: normal;
	margin-top: 2.5em;
	margin-bottom: 0;
}

p.formRow {
	margin: 0;
	margin-bottom: 0.6em;
}

p.error {
	color: orange;
	margin-top: 1.8em;
}

.info {
	color: rgb(200, 200, 200);
	margin: 0;
	margin-top: 0.6em;
	margin-bottom: 3em;
	font-size: 12pt;
	line-height: 16pt;
}

.hint {
	color: rgb(200, 200, 200);
	margin: 0;
	margin-top: 1.5em;
	margin-bottom: 0.3em;
	font-size: 11pt;
}
</style>
</head>
<body>
	<h1>train robots</h1>
	<p class="tagline">help teach robots to become smart as humans</p>
	<form method="post">
		<h2>Password reset</h2>
		<p class="info">We found your game account for kais@kaisdukes.com</br>Please enter a new password below.</p>
		<p class="hint">New password</p>
		<p class="formRow">
			<input class="textBox" type="password" name="passwordTextBox" />
		</p>
		<p class="hint">Confirm password</p>
		<p class="formRow">
			<input class="textBox" type="password" name="confirmPasswordTextBox"/>
		</p>
		<p>
			<input class="formButton" value="Save" type="submit" />
		</p>
		<% if (reset.getError() != null) { %><p class="error"><%=reset.getError()%></p><br/><%}%>
	</form>
</body>
</html>