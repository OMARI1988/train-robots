<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="register" class="com.trainrobots.web.pages.RegisterPage" />
<%register.initiate(
		pageContext,
		request.getMethod(),
		request.getParameter("emailTextBox"),
		request.getParameter("nameTextBox"),
		request.getParameter("passwordTextBox"),
		request.getParameter("confirmPasswordTextBox"));%>
<html>
<head>
<title>Train Robots - Register</title>
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
		<h2>Create an account</h2>
		<p class="hint" style="margin-top:2em">Email address</p>
		<p class="formRow">
			<input class="textBox" type="text" name="emailTextBox" value="<%=register.getEmail() != null ? register.getEmail() : ""%>"/>
		</p>
		<p class="hint">Your name in the game (e.g. HappyBunny)</p>
		<p class="formRow">
			<input class="textBox" type="text" name="nameTextBox" value="<%=register.getName() != null ? register.getName() : ""%>"/>
		</p>
		<p class="hint">Password for the game</p>
		<p class="formRow">
			<input class="textBox" type="password" name="passwordTextBox" />
		</p>
		<p class="hint">Confirm password</p>
		<p class="formRow">
			<input class="textBox" type="password" name="confirmPasswordTextBox"/>
		</p>
		<p>
			<input class="formButton" value="Register" type="submit" />
		</p>
		<% if (register.getError() != null) { %><p class="error"><%=register.getError()%></p><br/><%}%>
		<p class="links">
			<a href="signin.jsp">Already have an account?</a>
		</p>
	</form>
</body>
</html>