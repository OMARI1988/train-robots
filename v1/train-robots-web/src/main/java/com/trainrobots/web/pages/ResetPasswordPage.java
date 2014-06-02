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

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;

import com.trainrobots.core.CoreException;
import com.trainrobots.web.WebUtil;
import com.trainrobots.web.game.ResetToken;
import com.trainrobots.web.services.DataService;
import com.trainrobots.web.services.ServiceContext;

public class ResetPasswordPage {

	private final DataService dataService = ServiceContext.get().dataService();
	private String password;
	private String confirmPassword;
	private String error;

	public void initiate(PageContext pageContext, String method, String token,
			String password, String confirmPassword) {

		// Validate token.
		ServletContext context = pageContext.getServletContext();
		ResetToken resetToken = getResetToken(context, token);
		if (resetToken == null) {
			redirect(pageContext, "/signin.jsp");
			return;
		}

		// Expired?
		DateTime now = new DateTime(DateTimeZone.UTC);
		long elapsed = now.getMillis() - resetToken.requestUtc.getMillis();
		if (elapsed > DateTimeConstants.MILLIS_PER_DAY) {
			redirect(pageContext, "/signin.jsp");
			return;
		}

		// Not post back?
		if (method == null || !method.equalsIgnoreCase("POST")) {
			return;
		}

		// Form.
		this.password = password;
		this.confirmPassword = confirmPassword;

		// Validate.
		if (!validate(context)) {
			return;
		}

		// Change password.
		dataService.changePassword(context, resetToken.userId, password);

		// Redirect.
		redirect(pageContext, "/reset_password_completed.jsp");
	}

	public String getError() {
		return error;
	}

	private ResetToken getResetToken(ServletContext context, String token) {
		if (token == null || token.length() == 0) {
			return null;
		}
		return dataService.getPasswordResetToken(context, token);
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

	private void redirect(PageContext pageContext, String url) {
		try {
			((HttpServletResponse) pageContext.getResponse()).sendRedirect(url);
		} catch (IOException exception) {
			throw new CoreException(exception);
		}
	}
}