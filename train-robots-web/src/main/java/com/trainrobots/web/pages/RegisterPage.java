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
import com.trainrobots.web.services.DataService;
import com.trainrobots.web.services.ServiceContext;

public class RegisterPage {

	private final DataService dataService = ServiceContext.get().dataService();
	private String email;
	private String name;
	private String password;
	private String confirmPassword;
	private String error;

	public void initiate(PageContext pageContext, String method, String email,
			String name, String password, String confirmPassword) {

		// Not post back?
		if (method == null || !method.equalsIgnoreCase("POST")) {
			return;
		}

		// Form.
		this.email = email;
		this.name = name;
		this.password = password;
		this.confirmPassword = confirmPassword;

		// Validate.
		ServletContext context = pageContext.getServletContext();
		if (!validate(context)) {
			return;
		}

		// Create account.
		dataService.addUser(context, email, name, password);

		// Redirect.
		try {
			((HttpServletResponse) pageContext.getResponse())
					.sendRedirect("/registration_completed.jsp");
		} catch (IOException exception) {
			throw new WebException(exception);
		}
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getError() {
		return error;
	}

	private boolean validate(ServletContext context) {

		// Initiate.
		error = null;

		// Email.
		if (!WebUtil.isValidEmail(email)) {
			error = "Please enter a valid email address.";
			return false;
		}

		// Name.
		if (name == null || name.length() == 0) {
			error = "You forgot to enter your name in the game.";
			return false;
		}
		if (name.length() > 16 || name.indexOf(' ') >= 0) {
			error = "Your name has to be 16 characters or under and contain no spaces.";
			return false;
		}

		// Password.
		if (!WebUtil.isValidPassword(password)) {
			error = "Please enter a valid password.";
			return false;
		}

		// Confirm password.
		if (!password.equals(confirmPassword)) {
			error = "The passwords you entered didn't match.";
			return false;
		}

		// Check database.
		int result = dataService.validateRegistration(context, email, name);
		if (result == 1) {
			error = "An account with that email address already exists.";
			return false;
		}
		if (result == 2) {
			error = "Sorry - that name is taken. Please try a different name.";
			return false;
		}

		// Valid.
		return true;
	}
}