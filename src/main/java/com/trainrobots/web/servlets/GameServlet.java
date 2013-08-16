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
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trainrobots.web.services.ServiceContext;

public class GameServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.

		PrintWriter out = response.getWriter();
		out.print("<html>");
		out.print("<head><title>Train Robots - Game</title></head>");
		out.print("<body>");
		out.print("<p>What do you think?</p><p><a href=\"/game\">NEXT</a></p>");

		String r1 = ServiceContext.get().imageService().random();
		String r2 = ServiceContext.get().imageService().random();

		out.print("<table><tr>");
		out.print("<td><img src=\"static/" + r1 + "\"/></td>");
		out.print("<td><img src=\"static/" + r2 + "\"/></td>");
		out.print("</tr></table>");

		out.print("</body></html>");
	}
}