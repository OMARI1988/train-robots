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

package com.trainrobots.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trainrobots.web.game.User;
import com.trainrobots.web.services.ServiceContext;

public class GameServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			user = new User();
			request.getSession().setAttribute("user", user);
			user.score = 1;
			user.round = 1;
			user.turn = 1;
		} else {
			user.score++;
			user.turn++;
			if (user.turn > 10) {
				user.round++;
				user.turn = 1;
			}
		}

		PrintWriter out = response.getWriter();
		out.print("<html>");
		out.print("<head><title>Train Robots - Game</title></head>");
		out.print("<body>");
		out.print("<p>SCORE = " + user.score + "</p>");
		out.print("<hr/>");
		out.print("<p><b>Round " + user.round + " | Turn " + user.turn
				+ "</b></p>");
		out.print("<p>What do you think of the pictures below?</p>");
		out.print("<p>");
		out.print("<input name=\"q1\" type=\"radio\" value=\"1\"/>1. Command doesn't make sense for the pictures - robot should have ignored the command and not moved.<br/>");
		out.print("<input name=\"q1\" type=\"radio\" value=\"2\"/>2. Command was <span style='color:orange'>unclear</span> so robot made the <span style='color:orange'>wrong</span> move.<br/>");
		out.print("<input name=\"q1\" type=\"radio\" value=\"3\"/>3. Command was <span style='color:orange'>unclear</span> but robot managed to make the <span style='color:blue'>right</span> move.<br/>");
		out.print("<input name=\"q1\" type=\"radio\" value=\"4\"/>4. Command was <span style='color:blue'>clear</span> but robot got it <span style='color:orange'>wrong</span>.<br/>");
		out.print("<input name=\"q1\" type=\"radio\" value=\"5\"/>5. <span style='color:blue'>Clear</span> command and robot got it <span style='color:blue'>right</span>.<br/>");
		out.print("</p>");
		out.print("<p><a href=\"/game\">NEXT</a></p>");

		String r1 = ServiceContext.get().imageService().random();
		String r2 = ServiceContext.get().imageService().random();

		out.print("<table><tr>");
		out.print("<td><img src=\"static/" + r1 + "\"/></td>");
		out.print("<td><img src=\"static/" + r2 + "\"/></td>");
		out.print("</tr></table>");

		out.print("</body></html>");
	}
}