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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.web.game.Scene;
import com.trainrobots.web.services.CorpusService;
import com.trainrobots.web.services.GameService;
import com.trainrobots.web.services.ServiceContext;

public class ScenePage {

	private final CorpusService corpusService = ServiceContext.get()
			.corpusService();
	private final GameService gameService = ServiceContext.get().gameService();
	private Scene scene;
	private List<Command> commands;

	public void initiate(PageContext pageContext, String id) {

		// Load.
		loadScene(id);

		// Response.
		HttpServletResponse response = (HttpServletResponse) pageContext
				.getResponse();

		// Load commands.
		if (scene != null) {
			commands = corpusService.getCommands(scene.sceneNumber);
		}

		// Redirect.
		if (scene == null || commands == null || commands.size() == 0) {
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

	public String formatCommandsHeader() {

		// No commands?
		if (scene == null || commands == null || commands.size() == 0) {
			return "No commands";
		}

		// Format.
		int size = commands.size();
		return size + (size == 1 ? " command" : " commands");
	}

	public String getCommands() {

		// No commands?
		if (scene == null || commands == null || commands.size() == 0) {
			return null;
		}

		// Commands.
		StringBuilder text = new StringBuilder();
		text.append("<table border=\"0\" class=\"commandTable\" cellpadding=\"0\" cellspacing=\"0\">");
		for (Command command : commands) {
			text.append("<tr><td><a href='command.jsp?id=" + command.id + "'>");
			text.append(String.format("%05d", command.id));
			text.append("</a></td><td>");
			text.append(command.text);
			text.append("</td></tr>");
		}
		text.append("</table>");
		return text.toString();
	}

	private void loadScene(String id) {

		// Parse.
		int sceneNumber;
		try {
			sceneNumber = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			do {
				sceneNumber = 1 + (int) (Math.random() * 1000);
			} while (corpusService.getCommands(sceneNumber) == null);
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