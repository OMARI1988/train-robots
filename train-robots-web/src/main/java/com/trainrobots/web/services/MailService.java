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
			String smtpHost = context.getInitParameter("smtp-host");
			int smtpPort = Integer.parseInt(context
					.getInitParameter("smtp-port"));
			String smtpUser = context.getInitParameter("smtp-user");
			String smtpPassword = context.getInitParameter("smtp-password");

			// Initiate properties.
			Properties properties = new Properties();
			properties.put("mail.transport.protocol", "smtps");
			properties.put("mail.smtps.host", smtpHost);
			properties.put("mail.smtps.auth", "true");

			// Initiate session.
			Session mailSession = Session.getDefaultInstance(properties);
			Transport transport = mailSession.getTransport();

			// Create message.
			MimeMessage message = new MimeMessage(mailSession);
			message.setSubject(subject);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email));

			// UTF-8 body.
			message.setText(body, "UTF-8");
			message.saveChanges();
			message.removeHeader("Content-Transfer-Encoding");
			message.removeHeader("Content-Type");
			message.addHeader("Content-Transfer-Encoding", "base64");
			message.addHeader("Content-Type", "text/plain; charset=UTF-8");

			// Connect to SMTP server.
			transport.connect(smtpHost, smtpPort, smtpUser, smtpPassword);

			// Send message.
			transport.sendMessage(message,
					message.getRecipients(Message.RecipientType.TO));

			// Close session.
			transport.close();

		} catch (Exception exception) {
			throw new CoreException(exception);
		}
	}
}