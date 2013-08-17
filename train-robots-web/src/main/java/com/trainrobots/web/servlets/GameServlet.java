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

import com.trainrobots.web.WebException;
import com.trainrobots.web.game.Scene;
import com.trainrobots.web.game.User;
import com.trainrobots.web.services.GameService;
import com.trainrobots.web.services.ServiceContext;

public class GameServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handleRequest(false, request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handleRequest(true, request, response);
	}

	private void handleRequest(boolean isPost, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.

		GameService gameService = ServiceContext.get().gameService();

		String feedback = null;

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			user = new User();
			request.getSession().setAttribute("user", user);
			user.score = 5;
			user.round = 1;
			user.state = 1;
			user.sceneNumber = gameService.randomSceneNumber();
		} else if (isPost) {

			// Bad post?
			String r = request.getParameter("round");
			String s = request.getParameter("state");
			if (!Integer.toString(user.round).equals(r)
					|| !Integer.toString(user.state).equals(s)) {
				response.sendRedirect("/lost.jsp");
				return;
			}

			// User action?
			if (user.state == 1) {

				// Vote.
				if (user.round % 4 != 0) {
					int q1;
					try {
						q1 = Integer.parseInt(request.getParameter("q1"));
					} catch (NumberFormatException exception) {
						response.sendRedirect("/lost.jsp");
						return;
					}
					if (q1 < 1 || q1 > 5) {
						response.sendRedirect("/lost.jsp");
						return;
					}
					user.state = 2;
					int expected = gameService.scene(user.sceneNumber).mark;
					if (expected == q1) {
						feedback = "<span style='color:skyblue'>+20 points!</span> You chose "
								+ q1
								+ ". That's what most players voted for as well.";
						user.score += 20;
					} else {
						feedback = "<span style='color:skyblue'>+1 point.</span> Nice try, but most players chose option "
								+ expected + ".";
						user.score++;
					}
				} else {
					// String command = request.getParameter("command");
					user.score += 20;
					user.potential += 100;
					user.state = 2;
				}

			} else if (user.state == 2) {
				// New scene.
				user.state = 1;
				user.round++;
				user.sceneNumber = gameService.randomSceneNumber();
			} else {
				response.sendRedirect("/lost.jsp");
				return;
			}
		}

		PrintWriter out = response.getWriter();
		out.print("<html>");
		out.print("<head><title>Train Robots - Game</title></head>");
		out.print("<body>");
		out.print("<form method='post'>");
		out.print("<p><i>Train Robots - Help us build the smartest robots on the web!</i></p>");
		out.print("<hr/>");
		out.print("<p>Round " + user.round + "&nbsp;&nbsp;&nbsp;&nbsp;"
				+ user.score + " points");
		if (user.potential > 0) {
			out.print(" (+" + user.potential + " potential)");
		}
		out.print("</p><hr/>");

		boolean addCommand = user.round % 4 == 0;

		if (!addCommand) {
			out.print("<p>");
			if (user.state == 1) {
				out.print("<input name=\"q1\" type=\"radio\" value=\"1\" onclick=\"form.submit();\"/>");
			}
			out.print("1. Robot should have ignored the command and not moved because the command doesn't make sense for the pictures.<br/>");
			if (user.state == 1) {
				out.print("<input name=\"q1\" type=\"radio\" value=\"2\" onclick=\"form.submit();\"/>");
			}
			out.print("2. Command was <span style='color:orange'>unclear</span> so robot made the <span style='color:orange'>wrong</span> move.<br/>");
			if (user.state == 1) {
				out.print("<input name=\"q1\" type=\"radio\" value=\"3\" onclick=\"form.submit();\"/>");
			}
			out.print("3. Command was <span style='color:orange'>unclear</span> but robot managed to make the <span style='color:skyblue'>right</span> move.<br/>");
			if (user.state == 1) {
				out.print("<input name=\"q1\" type=\"radio\" value=\"4\" onclick=\"form.submit();\"/>");
			}
			out.print("4. Command was <span style='color:skyblue'>clear</span> but robot got it <span style='color:orange'>wrong</span>.<br/>");
			if (user.state == 1) {
				out.print("<input name=\"q1\" type=\"radio\" value=\"5\" onclick=\"form.submit();\"/>");
			}
			out.print("5. <span style='color:skyblue'>Clear</span> command and robot got it <span style='color:skyblue'>right</span>.<br/>");
			out.print("</p>");

			if (user.state == 1) {
				out.print("<p style='color:skyblue'>Take a look at the pictures below. Which option from 1 to 5 is correct?<br/>Get the most points by choosing the same as other players.</p>");
			} else {
				if (feedback != null) {
					out.print("<p>" + feedback + "</p>");
				}
				out.print("<p><input name='command' type='submit' value='Continue'/></p>");
			}
		} else {
			if (user.state == 1) {
				out.print("<div style='width:700px'>");
				out.print("<p>Now its your turn to help us make the robot smarter! The robot can learn from your commands.</p>");
				out.print("<p>Look at the two pictures below and find out what's changed.</p>");
				out.print("<p>What command what you give to another human being? We want the robot to be as smart as real people. Your command can be long and complicated if it needs to be. Don't be afraid to use new words or ideas to tell the robot what to do. Be creative. We want the robot to learn real English.</p>");
				out.print("<p>You will get <span style='color:skyblue'>bonus points</span> when your command gets voted as <span style='color:skyblue'>clear</span> and <span style='color:skyblue'>correct</span> for changing from the first picture below to the second one.</p>");
				out.print("<p><input type='text' style='width:650px;'/></p>");
				out.print("<p><input name='command' type='submit' value='Save'/></p>");
				out.print("</div>");
			} else {
				out.print("<p>Thanks - your command has been saved.</p>");
				out.print("<p><span style='color:skyblue'>+20 points!</span> You've also been awarded <span style='color:skyblue'>100 potential points!</span><br/>Your potential gets converted to real points if other players think your command is good.<br/>Be careful though. Bad commands means you will lose your points.</p>");
				out.print("<p><input name='command' type='submit' value='Continue'/></p>");
			}
		}

		Scene scene = gameService.scene(user.sceneNumber);
		out.print("<hr>");
		if (!addCommand) {
			out.print("<p style='color:rgb(183, 255, 252)'>"
					+ scene.description + "</p>");
		}

		out.print("<table><tr>");
		out.print("<td><i>before</i><br/><img src=\"" + scene.image1
				+ "\"/></td>");
		out.print("<td><i>after</i><br/><img src=\"" + scene.image2
				+ "\"/></td>");
		out.print("</tr></table>");
		out.print("<input type='hidden' name='round' value='" + user.round
				+ "'/>");
		out.print("<input type='hidden' name='state' value='" + user.state
				+ "'/>");
		out.print("</form>");
		out.print("</body></html>");

		// Simulate latency.
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new WebException(e);
		}
	}
}