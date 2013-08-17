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
			user.turn = 1;
			user.state = 1;
			user.sceneNumber = gameService.randomSceneNumber();
		} else if (isPost) {

			// Bad post?
			String r = request.getParameter("round");
			String t = request.getParameter("turn");
			String s = request.getParameter("state");
			if (!Integer.toString(user.round).equals(r)
					|| !Integer.toString(user.turn).equals(t)
					|| !Integer.toString(user.state).equals(s)) {
				response.sendRedirect("/lost.jsp");
				return;
			}

			// User voted?
			if (user.state == 1) {
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
				if (expected == 0) {
					feedback = "<b>+6 points!</b> You are the first player to vote ["
							+ user.sceneNumber + " -->  " + q1 + "]";
					user.score += 6;
				} else if (expected == q1) {
					feedback = "<b>+6 points!</b> You chose " + q1
							+ ". That's what most players voted for as well.";
					user.score += 6;
				} else {
					feedback = "<b>+1 point.</b> Nice try, but most players chose option "
							+ expected + ".";
					user.score++;
				}

			} else if (user.state == 2) {
				// New scene.
				user.state = 1;
				user.turn++;
				if (user.turn > 10) {
					user.round++;
					user.turn = 1;
				}
				user.sceneNumber = gameService.randomSceneNumber();
			} else {
				response.sendRedirect("/lost.jsp");
				return;
			}
		}

		PrintWriter out = response.getWriter();
		out.print("<html>");
		out.print("<head><title>Train Robots - Game</title>");
		out.print("<script type='text/javascript'>");
		out.print("function rc() {document.getElementById('voteButton').disabled=false;}");
		out.print("</script>");
		out.print("</head>");
		out.print("<body>");
		out.print("<form method='post'>");
		out.print("<p><i>Train Robots - Help us build the smartest robots on the web!</i></p>");
		out.print("<hr/>");
		out.print("<p><b>" + user.score + " points</b>");
		out.print(" | round " + user.round + " | turn " + user.turn + "</p>");
		out.print("<hr/>");
		out.print("<p style='color:green'><b>What do you think of the pictures below?</b></p>");
		out.print("<p>");
		if (user.state == 1) {
			out.print("<input name=\"q1\" type=\"radio\" value=\"1\" onclick=\"rc()\"/>");
		}
		out.print("1. Command doesn't make sense for the pictures - robot should have ignored the command and not moved.<br/>");
		if (user.state == 1) {
			out.print("<input name=\"q1\" type=\"radio\" value=\"2\" onclick=\"rc()\"/>");
		}
		out.print("2. Command was <span style='color:orange'>unclear</span> so robot made the <span style='color:orange'>wrong</span> move.<br/>");
		if (user.state == 1) {
			out.print("<input name=\"q1\" type=\"radio\" value=\"3\" onclick=\"rc()\"/>");
		}
		out.print("3. Command was <span style='color:orange'>unclear</span> but robot managed to make the <span style='color:blue'>right</span> move.<br/>");
		if (user.state == 1) {
			out.print("<input name=\"q1\" type=\"radio\" value=\"4\" onclick=\"rc()\"/>");
		}
		out.print("4. Command was <span style='color:blue'>clear</span> but robot got it <span style='color:orange'>wrong</span>.<br/>");
		if (user.state == 1) {
			out.print("<input name=\"q1\" type=\"radio\" value=\"5\" onclick=\"rc()\"/>");
		}
		out.print("5. <span style='color:blue'>Clear</span> command and robot got it <span style='color:blue'>right</span>.<br/>");
		out.print("</p>");

		if (user.state == 1) {
			out.print("<p><input id='voteButton' type='submit' value='Vote' disabled/></p>");
		} else {
			out.print("<p>" + feedback + "</p>");
			out.print("<p><input id='nextButton' type='submit' value='Next'/></p>");
		}

		Scene scene = gameService.scene(user.sceneNumber);
		out.print("<p style='color:teal'><b>Command</b> &gt; " + scene.description + "</p>");

		out.print("<table><tr>");
		out.print("<td><i>before</i><br/><img src=\"" + scene.image1 + "\"/></td>");
		out.print("<td><i>after</i><br/><img src=\"" + scene.image2 + "\"/></td>");
		out.print("</tr></table>");
		out.print("<input type='hidden' name='round' value='" + user.round
				+ "'/>");
		out.print("<input type='hidden' name='turn' value='" + user.turn
				+ "'/>");
		out.print("<input type='hidden' name='state' value='" + user.state
				+ "'/>");
		out.print("</form>");
		out.print("</body></html>");
	}
}