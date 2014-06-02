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

package com.trainrobots.web.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import com.trainrobots.core.CoreException;

public class MailService {

	public void sendMail(ServletContext context, String email, String subject,
			String body) {

		try {

			// Configuration.
			final String smtpHost = context.getInitParameter("smtp-host");
			final int smtpPort = Integer.parseInt(context
					.getInitParameter("smtp-port"));
			final String smtpUser = context.getInitParameter("smtp-user");
			final String smtpPassword = context
					.getInitParameter("smtp-password");

			// Initiate properties.
			Properties properties = new Properties();
			properties.put("mail.smtp.host", smtpHost);
			properties.put("mail.smtp.socketFactory.port",
					Integer.toString(smtpPort));
			properties.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.port", Integer.toString(smtpPort));

			// Initiate session.
			Session session = Session.getDefaultInstance(properties,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(smtpUser,
									smtpPassword);
						}
					});

			// Message.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(smtpUser));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email));
			message.setSubject(subject);
			message.setText(body);

			// Send.
			Transport.send(message);

		} catch (Exception exception) {
			throw new CoreException(exception);
		}
	}
}