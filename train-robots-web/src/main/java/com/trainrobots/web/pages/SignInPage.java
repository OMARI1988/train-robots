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
import javax.servlet.jsp.PageContext;

import com.trainrobots.web.WebException;
import com.trainrobots.web.WebUtil;
import com.trainrobots.web.game.User;
import com.trainrobots.web.services.DataService;
import com.trainrobots.web.services.GameService;
import com.trainrobots.web.services.ServiceContext;
import com.trainrobots.web.services.UserService;

public class SignInPage {

	private final UserService userService = ServiceContext.get().userService();
	private final DataService dataService = ServiceContext.get().dataService();
	private final GameService gameService = ServiceContext.get().gameService();
	private String email;
	private String password;
	private String loginError;
	private User user;

	public void initiate(PageContext pageContext, String method, String email,
			String password) {

		// Not post back?
		if (method == null || !method.equalsIgnoreCase("POST")) {
			return;
		}

		// E-mail and password.
		this.email = email;
		this.password = password;

		// Validate.
		ServletContext context = pageContext.getServletContext();
		if (!validate(context)) {
			return;
		}

		// Clear in-memory password for additional security.
		if (user != null) {
			user.password = null;
		}

		// Sign the user in.
		userService.signIn(pageContext.getSession(), user);

		// Initiate game state.
		user.round++;
		user.state = 1;
		user.sceneNumber = gameService.randomSceneNumber(context);

		// Redirect.
		try {
			((HttpServletResponse) pageContext.getResponse())
					.sendRedirect("/game");
		} catch (IOException exception) {
			throw new WebException(exception);
		}
	}

	public String getEmail() {
		return email;
	}

	public String getLoginError() {
		return loginError;
	}

	private boolean validate(ServletContext context) {

		// Initiate.
		loginError = null;
		user = null;

		// Invalid e-mail?
		if (!WebUtil.isValidEmail(email)) {
			loginError = "Please enter a valid email address.";
		}

		// Invalid password?
		if (!WebUtil.isValidPassword(password)) {
			if (loginError != null) {
				loginError = "Please enter a valid email address and password.";
			} else {
				loginError = "Please enter a valid password.";
			}
		}

		// Failed initial validation?
		if (loginError != null) {
			return false;
		}

		// Get user from database.
		user = dataService.getUser(context, email);

		// Invalid e-mail or password?
		if (user == null || !password.equals(user.password)) {
			loginError = "The email address or password is not recognized.";
			return false;
		}

		// Not active?
		if (user.status != 1) {
			loginError = "You cannot sign in because the account " + email
					+ " has not been activated.";
			return false;
		}

		// Valid?
		return loginError == null;
	}
}