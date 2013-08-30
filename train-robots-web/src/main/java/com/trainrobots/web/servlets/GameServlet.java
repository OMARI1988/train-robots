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

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trainrobots.web.game.Scene;
import com.trainrobots.web.game.User;
import com.trainrobots.web.services.DataService;
import com.trainrobots.web.services.GameService;
import com.trainrobots.web.services.ServiceContext;
import com.trainrobots.web.services.UserService;

public class GameServlet extends HttpServlet {

	private final UserService userService = ServiceContext.get().userService();
	private final GameService gameService = ServiceContext.get().gameService();
	private final DataService dataService = ServiceContext.get().dataService();
	private static final String[] OPTIONS;

	static {
		OPTIONS = new String[] {
				"The command uses inappropriate words (spam) and should be removed.",
				"The command was <span class='negative'>unclear</span> so the robot made the <span class='negative'>wrong</span> move (e.g. before and after images were confused).",
				"The command was <span class='negative'>unclear</span> but the robot still made the <span class='positive'>right</span> move (e.g. spelling or other mistakes).",
				"The command was <span class='positive'>clear</span> but the robot got it <span class='negative'>wrong</span>.",
				"The command was <span class='positive'>clear</span> and the robot got it <span class='positive'>right</span>." };
	}

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

		// Not signed in?
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("/signin.jsp");
			return;
		}

		// Superseded?
		if (user.superseded) {
			userService.signOut(request.getSession());
			response.sendRedirect("/forced_signout.jsp");
			return;
		}

		// Admin.
		boolean admin = gameService.isAdmin(user.email);

		// Form.
		String command = request.getParameter("command");
		String error = null;

		// Handle post.
		ServletContext context = request.getSession().getServletContext();
		String feedback = null;
		if (isPost) {

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

				// Judge.
				if (!gameService.isAddCommandRound(user.email, user.round)) {
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
					int expected = user.scene.expectedOption;
					if (admin) {
						feedback = "Saved mark as option " + q1 + ".";
						if (expected != 0) {
							feedback = " Previously marked as " + expected
									+ ".";
						}
						dataService.markCommand(context, user.scene.rateUserId,
								user.scene.rateRound, q1);
					} else {
						if (expected == q1) {
							feedback = "<span class='positive'>+20 points!</span> You chose "
									+ q1
									+ ". That's what most players voted for as well.";
							user.score += 20;
						} else {
							feedback = "<span class='positive'>+1 point.</span> Nice try, but most players chose option "
									+ expected + ".";
							user.score++;
						}
						dataService.addRound(context, user.userId, user.round,
								user.score, user.potential,
								user.scene.sceneNumber, expected, q1,
								request.getRemoteAddr(), null,
								user.scene.rateUserId, user.scene.rateRound);
					}
				}

				// Add command.
				else {

					// Valid commmand?
					if (isValidCommand(command)) {
						user.score += 20;
						user.potential += 100;
						user.state = 2;
						dataService.addRound(context, user.userId, user.round,
								user.score, user.potential,
								user.scene.sceneNumber, 0, 0,
								request.getRemoteAddr(), command, 0, 0);
					} else {
						error = "Please enter a valid command.";
					}
				}

			} else if (user.state == 2) {
				// New scene.
				user.state = 1;
				user.round++;
				user.scene = gameService.assignScene(context, user.email,
						user.round);
			} else {
				response.sendRedirect("/lost.jsp");
				return;
			}
		}

		// Write.
		writePage(context, response, user, feedback, command, error, admin);
	}

	private void writePage(ServletContext context,
			HttpServletResponse response, User user, String feedback,
			String command, String error, boolean admin) throws IOException {

		// Disable caching.
		response.setContentType("text/html");
		response.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.

		// Start.
		PrintWriter out = response.getWriter();
		out.println("<html>");
		writeHeader(out);
		out.println("<body>");
		out.println("<form id='gameForm' method='post' action='#play'>");
		out.println("<h1>train robots</h1><p class='tagline'>help teach robots to become smart as humans</p>");

		// Score.
		out.println("<p id='play'>");
		if (admin) {
			out.println("Admin");
		} else {
			out.println("<span class='positive'>Round " + user.round
					+ "</span>&nbsp;&nbsp;&nbsp;" + user.score + " points");
		}
		out.println("</p>");

		// Judge?
		boolean addCommand = gameService.isAddCommandRound(user.email,
				user.round);
		if (!addCommand) {
			writeQuestions(out, user.state);
			if (user.state == 1) {
				out.println("<p class='info'>Find the changes between the two pictures below. Which option from 1 to 5 is best?<br />Get the most points by choosing the same answer as other players.</p>");
			} else {
				if (feedback != null) {
					out.println("<p class='info'>" + feedback + "</p>");
				}
				out.println("<p class='info'><input class='formButton' type='submit' value='Continue'/></p>");
			}
		} else {

			// Add command.
			out.println("<div class='add'>");
			if (user.state == 1) {
				out.println("<p class='d'>Now its your turn to help us make the robot smarter! The robot can learn from your commands.</p>");
				out.println("<p class='d'>What command would you give to another human being? We want the robot to be as smart as real people. Your command can be long and complicated if this makes it clearer, but short commands are better. Don't be afraid to use new words or ideas to tell the robot what to do. Be creative. We want the robot to learn real English.</p>");
				out.println("<p class='d'>You will get <span class='positive'>bonus points</span> when your command gets voted as <span class='positive'>clear</span> and <span class='positive'>correct</span> by other players, for changing from the first picture below to the second one.</p>");
				out.println("<p class='d'>Look at the two pictures below and find out what's changed, then enter a suitable command. Ignore arm movements and focus on the block that has changed position.</p>");
				out.print("<p class='d'><textarea class='textBox' rows='3' type='text' name='command' style='width:650px;'/>");
				if (command != null) {
					out.print(command);
				}
				out.print("</textarea></p>");
				if (error != null) {
					out.println("<p class='error'>" + error + "</p>");
				}
				out.println("<p class='d'><input class='formButton' type='submit' value='Save'/></p>");
			} else {
				out.println("<p class='d'>Thanks - your command has been saved.</p>");
				out.println("<p class='d'><span class='positive'>+20 points!</span> You've also been awarded <span class='positive'>100 potential points!</span><br/></p>");
				out.println("<p class='d'>Your potential gets converted to real points if other players think your command is good.<br/>Be careful though. Bad or unclear commands means you could lose points.</p>");
				out.println("<p class='d'><input class='formButton' name='command' type='submit' value='Continue'/></p>");
			}
			out.println("</div>");
		}

		// Command.
		Scene scene = user.scene;
		if (!addCommand) {
			out.println("<p id='command'>" + scene.command + "</p>");
		}

		// Scene.
		out.println("<table id='scene' cellspacing='0' cellpadding='2'>");
		out.println("<tr><td class='left-image'><p class='move'>Before the command</p> <img src='"
				+ getImage(scene.fromGroup, scene.fromImage)
				+ "' width='325' height='350'/></td>");
		out.println("<td><img src='/images/right-arrow.png' style='margin-left:20px; margin-right:20px;'/></td>");
		out.println("<td class='right-image'><p class='move'>After the command	</p> <img src='"
				+ getImage(scene.toGroup, scene.toImage)
				+ "' width='325' height='350' /></td></tr>");
		out.println("</table>");

		// Hidden values.
		out.println("<input type='hidden' name='round' value='" + user.round
				+ "'/>");
		out.println("<input type='hidden' name='state' value='" + user.state
				+ "'/>");

		// End.
		out.println("</form>");

		// Hints.
		out.println("<p id='hints'>Hints</p>");
		out.println("<p class='tip'>");
		out.println("Give directions from the perspective of the robot. Left and right should be used from the robots point of view, not from your point of view.");
		out.println("Back means closer to the robot's pedestal. Forward means away from the robot's pedestal.");
		out.println("</p>");
		out.println("<p class='tip'>");
		out.println("Try not to confuse the before and after images. The right image is the final image we want to get to.");
		out.println("</p>");
		out.println("<p class='tip'>");
		out.println("Don't use map directions (north, east, south, west) and don't use grid co-ordinates (A3, B5). Instead, explain the destination location relative to surrounding blocks. Be as specific as possible. Imagine you are explaining the command to your friend over the telephone.");
		out.println("</p>");

		// Links.
		out.println("<table class='links' cellspacing='0' cellpadding='0'>");
		out.println("<tr>");
		out.println("<td><img src='/images/home-medium.png'/></td>");
		out.println("<td><a href='/'>home</a></td>");
		out.println("</tr>");
		out.println("<tr>");
		out.println("<td><img src='/images/contact-medium.png'/></td>");
		out.println("<td><a href='/signout.jsp'>sign out</a></td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("</body></html>");
	}

	private String getImage(int groupNumber, int imageNumber) {
		StringBuilder text = new StringBuilder();
		text.append("/static/g");
		text.append(groupNumber);
		text.append("/x");
		text.append(imageNumber);
		text.append(".png");
		return text.toString();
	}

	private void writeQuestions(PrintWriter out, int state) {
		out.println("<table id='questions' cellspacing='0' cellpadding='0'>");
		for (int i = 0; i < 5; i++) {
			out.println("<tr>");
			if (state == 1) {
				out.println("<td>");
				out.println("<input name=\"q1\" type=\"radio\" value=\""
						+ (i + 1) + "\" onclick=\"chooseOption();\"/>");
				out.println("</td>");
			}
			out.println("<td class='num'>");
			out.println("<span>" + (i + 1) + "</span>");
			out.println("</td>");
			out.println("<td>");
			out.println(OPTIONS[i]);
			out.println("</td>");
			out.println("</tr>");
		}
		out.println("</table>");
	}

	private void writeHeader(PrintWriter out) {
		out.println("<head>");
		out.println("<meta http-equiv='Content-Type' content='text/html;charset=UTF-8' />");
		out.println("<link href='css/main.css' type='text/css' rel='stylesheet' />");
		out.println("<style type='text/css'>");
		out.println("p#play {");
		out.println("color: rgb(141, 244, 50);");
		out.println("font-size: 12pt;");
		out.println("font-weight: bold;");
		out.println("margin-top: 2.5em;");
		out.println("}");
		out.println("p#hints {");
		out.println("color: white;");
		out.println("font-size: 12pt;");
		out.println("font-weight: bold;");
		out.println("margin-top: 2.5em;");
		out.println("}");
		out.println("p.info {");
		out.println("color: rgb(200, 200, 200);");
		out.println("font-size: 12pt;");
		out.println("line-height: 16pt;");
		out.println("}");
		out.println("p.tip {");
		out.println("color: rgb(200, 200, 200);");
		out.println("font-size: 12pt;");
		out.println("line-height: 16pt;");
		out.println("width: 700px;");
		out.println("}");
		out.println("table#questions {");
		out.println("color: rgb(200, 200, 200);");
		out.println("font-size: 12pt;");
		out.println("line-height: 16pt;");
		out.println("}");
		out.println("table#questions td {");
		out.println("vertical-align: top;");
		out.println("}");
		out.println("td.num {");
		out.println("color: white;");
		out.println("padding-left: 0.2em;");
		out.println("padding-right: 0.7em;");
		out.println("}");
		out.println("span.positive {");
		out.println("color: white;");
		out.println("}");
		out.println("span.negative {");
		out.println("color: orange;");
		out.println("}");
		out.println("div.add {");
		out.println("width: 700px;");
		out.println("margin-bottom: 3em;");
		out.println("}");
		out.println("p#command {");
		out.println("color: white;");
		out.println("font-size: 12pt;");
		out.println("margin-top: 3em;");
		out.println("margin-bottom: 1.5em;");
		out.println("width: 630px;");
		out.println("line-height: 16pt;");
		out.println("}");
		out.println("table#scene {");
		out.println("background: black;");
		out.println("}");
		out.println("p.move {");
		out.println("color: rgb(141, 244, 50);");
		out.println("font-size: 12pt;");
		out.println("font-weight: bold;");
		out.println("margin: 0.2em;");
		out.println("margin-left: 0.4em;");
		out.println("}");
		out.println("p.d {");
		out.println("color: rgb(200, 200, 200);");
		out.println("font-size: 12pt;");
		out.println("line-height: 16pt;");
		out.println("}");
		out.println("p.error {");
		out.println("color: orange;");
		out.println("font-size: 12pt;");
		out.println("line-height: 16pt;");
		out.println("}");
		out.println("table.links {");
		out.println("margin-top: 3em;");
		out.println("}");
		out.println("table.links td {");
		out.println("padding-top: 0.4em;");
		out.println("padding-right: 0.4em;");
		out.println("font-size: 12pt;");
		out.println("}");
		out.println("</style>");
		out.println("<script type='text/javascript'>");
		out.println("function chooseOption()");
		out.println("{");
		out.println("var x = document.getElementsByName('q1');");
		out.println("for (var i = 0; i < x.length; i++){");
		out.println("x[i].readonly = true;");
		out.println("}");
		out.println("document.getElementById('gameForm').submit();");
		out.println("}");
		out.println("</script>");
		out.println("<title>Train Robots - Game</title>");
		out.println("</head>");
	}

	private static boolean isValidCommand(String command) {

		// Not specified?
		if (command == null) {
			return false;
		}

		// Must be at least two words.
		String text = command.trim();
		if (text.length() == 0) {
			return false;
		}
		return text.indexOf(' ') > 1;
	}
}