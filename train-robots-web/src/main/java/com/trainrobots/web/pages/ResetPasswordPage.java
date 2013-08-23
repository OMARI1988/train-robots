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

public class ResetPasswordPage {

	//private final DataService dataService = ServiceContext.get().dataService();
	private String password;
	private String confirmPassword;
	private String error;

	public void initiate(PageContext pageContext, String method, String token,
			String password, String confirmPassword) {

		// TODO: VERIFY TOKEN!

		// Not post back?
		if (method == null || !method.equalsIgnoreCase("POST")) {
			this.error = "GET REQUEST WITH TOKEN = [" + token + "]";
			return;
		}

		// Form.
		this.password = password;
		this.confirmPassword = confirmPassword;

		// Validate.
		ServletContext context = pageContext.getServletContext();
		if (!validate(context)) {
			return;
		}

		// TODO: RESET PASSWORD

		// Redirect.
		try {
			((HttpServletResponse) pageContext.getResponse())
					.sendRedirect("/reset_password_completed.jsp");
		} catch (IOException exception) {
			throw new WebException(exception);
		}
	}

	public String getError() {
		return error;
	}

	private boolean validate(ServletContext context) {

		// Initiate.
		error = null;

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

		// Valid.
		return true;
	}
}