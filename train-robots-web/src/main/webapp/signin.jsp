<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="signIn" class="com.trainrobots.web.pages.SignInPage" />
<%signIn.initiate(
		pageContext,
		request.getMethod(),
		request.getParameter("emailTextBox"),
		request.getParameter("passwordTextBox"));%>
<html>
<head>
<title>Train Robots - Sign In</title>
<style type="text/css">
h2 {
	color: white;
	font-size: 20pt;
	font-weight: normal;
	margin-top: 2.5em;
	margin-bottom: 1em;
}

p.formRow {
	margin: 0;
	margin-bottom: 0.6em;
}

p.error {
	color: orange;
	margin-top: 1.8em;
}

p.links {
	margin-top: 3em;
	line-height: 20pt;
}

.hint {
	color: rgb(200, 200, 200);
	margin: 0;
	margin-top: 2em;
	margin-bottom: 0.5em;
	font-size: 11pt;
}
</style>
</head>
<body>
	<h1>train robots</h1>
	<p class="tagline">help teach robots to become smart as humans</p>
	<form method="post">
		<h2>Sign in</h2>
		<p class="hint">Use your email address and game password</p>
		<p class="formRow">
			<input class="textBox" type="text" name="emailTextBox" value="<%=signIn.getEmail() != null ? signIn.getEmail() : ""%>"/>
		</p>
		<p class="formRow">
			<input class="textBox" type="password" name="passwordTextBox"/>
		</p>
		<p>
			<input class="formButton" value="Sign in" type="submit" /> <input
				class="formButton" value="Cancel" type="button"
				style="margin-left: 0.8em;"
				onclick="window.location='/'" />
		</p>
		<% if (signIn.getLoginError() != null) { %><p class="error"><%=signIn.getLoginError()%></p><br/><%}%>
		<p class="links">
			<a href="register.jsp">Create a free account</a><br /> <a href="forgot_password.jsp">Forgot
				your password?</a>
		</p>
	</form>
</body>
</html>