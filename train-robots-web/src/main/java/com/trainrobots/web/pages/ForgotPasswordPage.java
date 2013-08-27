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
import com.trainrobots.web.services.MailService;
import com.trainrobots.web.services.ServiceContext;

public class ForgotPasswordPage {

	private final DataService dataService = ServiceContext.get().dataService();
	private final MailService mailService = ServiceContext.get().mailService();
	private String email;
	private String error;

	public void initiate(PageContext pageContext, String method, String email) {

		// Not post back?
		if (method == null || !method.equalsIgnoreCase("POST")) {
			return;
		}

		// Form.
		this.email = email;

		// Validate.
		ServletContext context = pageContext.getServletContext();
		if (!validate(context)) {
			return;
		}

		// Generate reset token.
		String token = dataService.addPasswordResetToken(context, email);
		if (token == null) {
			error = "Sorry - that email account is not registered.";
			return;
		}

		// Send mail with reset token.
		sendMail(context, email, token);

		// Redirect.
		try {
			((HttpServletResponse) pageContext.getResponse())
					.sendRedirect("/forgot_password_completed.jsp");
		} catch (IOException exception) {
			throw new WebException(exception);
		}
	}

	public String getEmail() {
		return email;
	}

	public String getError() {
		return error;
	}

	private void sendMail(ServletContext context, String email, String token) {

		StringBuilder body = new StringBuilder();
		body.append("Hello,\r\n\r\n");
		body.append("To reset your Train Robots game password, open this link in your browser:\r\n\r\n");
		body.append("http://trainrobots.com/reset_password.jsp?token=" + token
				+ "\r\n\r\n");
		body.append("Once you've changed your password you can then sign in and start playing!\r\n\r\n");

		String subject = "Reset your Train Robots game password";
		mailService.sendMail(context, email, subject, body.toString());
	}

	private boolean validate(ServletContext context) {

		// Initiate.
		error = null;

		// Email.
		if (!WebUtil.isValidEmail(email)) {
			error = "Please enter a valid email address.";
			return false;
		}

		// Valid.
		return true;
	}
}