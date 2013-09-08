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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.trainrobots.core.CoreException;
import com.trainrobots.web.WebUtil;
import com.trainrobots.web.game.Command;
import com.trainrobots.web.game.Options;
import com.trainrobots.web.game.Scene;
import com.trainrobots.web.game.User;
import com.trainrobots.web.services.DataService;
import com.trainrobots.web.services.GameService;
import com.trainrobots.web.services.ServiceContext;

public class ScenePage {

	private final DataService dataService = ServiceContext.get().dataService();
	private final GameService gameService = ServiceContext.get().gameService();
	private Scene scene;
	private List<Command> commands;

	public void initiate(PageContext pageContext, String id) {

		// Load.
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && gameService.isAdmin(user.email)) {
			loadScene(id);
		}

		// Response.
		HttpServletResponse response = (HttpServletResponse) pageContext
				.getResponse();

		// Redirect.
		if (scene == null) {
			try {
				response.sendRedirect("/lost.jsp");
				return;
			} catch (IOException exception) {
				throw new CoreException(exception);
			}
		}

		// Disable caching.
		response.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.

		// Load commands.
		commands = dataService.getSceneCommands(
				pageContext.getServletContext(), scene.sceneNumber);
	}

	public int getSceneNumber() {
		return scene != null ? scene.sceneNumber : -1;
	}

	public String getImage1() {
		if (scene == null) {
			return null;
		}
		return getImage(scene.fromGroup, scene.fromImage);
	}

	public String getImage2() {
		if (scene == null) {
			return null;
		}
		return getImage(scene.toGroup, scene.toImage);
	}

	public String getCommands() {

		// No scene?
		if (scene == null) {
			return null;
		}

		// Commands.
		int lastOption = -1;
		StringBuilder text = new StringBuilder();
		for (Command command : commands) {

			// Option.
			int option = command.commandMark;
			if (option != lastOption) {
				text.append("<p class='option'>");
				text.append(option > 0 ? Options.get(option) : "Unmarked");
				text.append("</p>");
				lastOption = option;
			}

			// Text.
			text.append("<p class='text'><a href='command.jsp?u="
					+ command.userId + "&r=" + command.round + "'>");
			text.append(command.command);
			text.append("</a></p>");

			// Info.
			text.append("<p class='info'>");
			text.append(command.gameName);
			text.append(" (");
			text.append(command.email);
			text.append(") - ");
			text.append(WebUtil.formatTime(command.timeUtc));
			text.append("</p>");
		}
		return text.toString();
	}

	private void loadScene(String id) {

		// Parse.
		int sceneNumber;
		try {
			sceneNumber = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			sceneNumber = 1 + (int) (Math.random() * 1000);
		}

		// Load.
		scene = gameService.getAddCommandScene(sceneNumber);
	}

	private static String getImage(int groupNumber, int imageNumber) {
		StringBuilder text = new StringBuilder();
		text.append("/static/g");
		text.append(groupNumber);
		text.append("/x");
		text.append(imageNumber);
		text.append(".png");
		return text.toString();
	}
}