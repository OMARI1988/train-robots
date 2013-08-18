/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.web.pages;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import com.trainrobots.web.WebException;
import com.trainrobots.web.game.User;
import com.trainrobots.web.services.DataService;
import com.trainrobots.web.services.GameService;
import com.trainrobots.web.services.ServiceContext;

public class SignInPage {

	private static final String EMAIL_REGEX = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	private final DataService dataService = ServiceContext.get().dataService();
	private final GameService gameService = ServiceContext.get().gameService();
	private String email;
	private String password;
	private String emailError;
	private String passwordError;
	private String loginError;
	private boolean isRegister;
	private User user;

	public void initiate(PageContext pageContext, String method, String email,
			String password, String loginType) {

		// Not post back?
		if (method == null || !method.equalsIgnoreCase("POST")) {
			return;
		}

		// E-mail and password.
		this.email = email;
		this.password = password;

		// Register?
		isRegister = loginType != null && loginType.equals("register");

		// Validate.
		if (!validate(pageContext.getServletContext())) {
			return;
		}

		// Clear in-memory password for additional security.
		if (user != null) {
			user.password = null;
		}

		// Initiate.
		String redirectUrl;
		HttpSession session = pageContext.getSession();

		// Register?
		if (isRegister) {

			// Set registration e-mail.
			setRegistrationEmail(session, email);

			// Registration URL.
			redirectUrl = "/register.jsp";
		}

		// Log-in?
		else {

			// Sign the user in.
			signInUser(session, user);

			// Redirect to game.
			redirectUrl = "/game";
		}

		// Redirect.
		try {
			((HttpServletResponse) pageContext.getResponse())
					.sendRedirect(redirectUrl);
		} catch (IOException exception) {
			throw new WebException(exception);
		}
	}

	public String getEmail() {
		return email;
	}

	public boolean isRegister() {
		return isRegister;
	}

	public String getEmailError() {
		return emailError;
	}

	public String getPasswordError() {
		return passwordError;
	}

	public String getLoginError() {
		return loginError;
	}

	private boolean validate(ServletContext servletContext) {

		// Initiate.
		emailError = null;
		passwordError = null;
		loginError = null;
		user = null;
		boolean isValid = true;

		// Valid e-mail?
		if (!isValidEmail(email)) {
			emailError = "please enter a valid e-mail address";
			isValid = false;
		}

		// If not registration, then validate password.
		if (!isRegister) {

			// Valid password?
			if (password == null || password.length() == 0) {
				passwordError = "please enter your password";
				isValid = false;
			} else if (!isValidPassword(password)) {
				passwordError = "please a valid password";
				isValid = false;
			}
		}

		// Failed initial validation?
		if (!isValid) {
			return false;
		}

		// Get user from database.
		user = dataService.getUser(servletContext, email);

		// Login?
		if (!isRegister) {

			// Invalid e-mail or password?
			if (user == null || !password.equals(user.password)) {
				loginError = "The e-mail address or password you entered is incorrect.";
				return false;
			}

			// Not activate?
			if (user.status != 1) {
				loginError = "You cannot sign in because the account <b>"
						+ email + "</b> has not been activated.";
				return false;
			}
		}

		// Registration with existing e-mail address?
		else {

			if (user != null) {
				loginError = "Although you indicated you are a new user, an account already exists for <b>"
						+ email + "</b>.";
				return false;
			}
		}

		// Valid?
		return isValid;
	}

	private void signInUser(HttpSession session, User user) {

		// Sign in.
		session.setAttribute("user", user);
		user.signedIn = true;

		// Initiate game state.
		user.round++;
		user.state = 1;
		user.sceneNumber = gameService.randomSceneNumber();
	}

	private static void setRegistrationEmail(HttpSession session, String email) {
		session.setAttribute("registration_email", email);
	}

	private static boolean isValidEmail(String email) {
		return email != null && email.length() > 0 && email.length() <= 100
				&& email.matches(EMAIL_REGEX);
	}

	private static boolean isValidPassword(String password) {
		if (password == null) {
			return false;
		}
		int size = password.length();
		if (size == 0 || size > 20) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			char ch = password.charAt(i);
			if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
					|| (ch >= '0' && ch <= '9') || ch == '_' || ch == '-'
					|| ch == '*' || ch == '!' || ch == '$' || ch == '#'
					|| ch == '+' || ch == '@' || ch == '|') {
				// valid
			} else {
				return false;
			}
		}
		return true;
	}
}