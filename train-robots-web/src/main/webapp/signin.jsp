<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="signIn" class="com.trainrobots.web.pages.SignInPage" />
<%signIn.initiate(
		pageContext,
		request.getMethod(),
		request.getParameter("emailTextBox"),
		request.getParameter("passwordTextBox"),
		request.getParameter("loginType"));%>
<html>
<head>
<title>Train Robots - Sign In</title>
<style type="text/css">
	table.loginTable td {
		padding-top: 0.3em;
		padding-bottom: 0.5em;
	}
	
	table.loginTable td.section {
		color: skyblue;
	}
	
	table.loginTable td.detail {
		padding-left: 1.5em;
	}
	
	table.inputTable td {
		padding: 0;
	}

	td.error, span.error {
		font-style: italic;
		color: orange;
	}

	input.disabled {
		background: rgb(224, 224, 224);
	}

	input.enabled {
		background: white;
	}
</style>
<script type='text/javascript'>
	function enablePassword(isEnabled) {
		var passwordTextBox = document.getElementById("passwordTextBox");
		passwordTextBox.disabled = !isEnabled;
		passwordTextBox.value = "";
		passwordTextBox.className = isEnabled  ? 'enabled' : 'disabled';
	}
</script>
</head>
<body>
	<form method="post">
	<p class="first">
		Sign in to start playing! You need an account to play.
	</p>
	<table class="loginTable" cellpadding="0" cellspacing="0" border="0">
		<tr><td class="section">What is your e-mail address?</td></tr>
		<tr><td class="detail">
			<table cellpadding="0" cellspacing="0" border="0" class="inputTable">
				<tr><td>My e-mail address is:&nbsp;</td><td><input name="emailTextBox" style="width:250px" value="<%=signIn.getEmail() != null ? signIn.getEmail() : ""%>"/></td></tr>
				<tr><td>&nbsp;</td><td class="error"><%=signIn.getEmailError() != null ? signIn.getEmailError() : "&nbsp;"%></td></tr>
			</table>
		</td></tr>
		<tr><td class="section" style="padding-top: 1em;">Do you have a password with this game?</td></tr>
		<tr><td class="detail"><input onClick="enablePassword(false);" name="loginType" type="radio" value="register" <%=signIn.isRegister() ? "checked" : ""%>/>No, I need to register for a new account.</td></tr>
		<tr><td class="detail"><input onClick="enablePassword(true);"name="loginType" type="radio" value="login" <%=!signIn.isRegister() ? "checked" : ""%>/>Yes, my password is: <input id="passwordTextBox" name="passwordTextBox" type="password" style="width:100px" class="<%=signIn.isRegister() ? "disabled" : "enabled"%>"/> <span class="error"><%=signIn.getPasswordError() != null ? signIn.getPasswordError() : "&nbsp;"%></span></td></tr>
		<tr><td class="detail"><input type="submit" value="Sign In"/></td></tr>
		<tr><td style="padding-top: 2em; line-height: 1.5em;">
			<% if (signIn.getLoginError() != null) { %><span class="error"><%=signIn.getLoginError()%></span><br/><%}%>
			If you have forgotten your password, please <a href="/contact.jsp">contact us</a>.
		</td></tr>
	</table>
	</form>
</body>
</html>
