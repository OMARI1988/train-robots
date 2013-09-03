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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.trainrobots.web.WebException;
import com.trainrobots.web.WebUtil;
import com.trainrobots.web.game.Command;
import com.trainrobots.web.game.Options;
import com.trainrobots.web.game.Scene;
import com.trainrobots.web.game.User;
import com.trainrobots.web.services.DataService;
import com.trainrobots.web.services.GameService;
import com.trainrobots.web.services.ServiceContext;

public class CommandPage {

	private final DataService dataService = ServiceContext.get().dataService();
	private final GameService gameService = ServiceContext.get().gameService();
	private Scene scene;

	public void initiate(PageContext pageContext, String u, String r) {

		// Load.
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && gameService.isAdmin(user.email)) {
			loadScene(pageContext.getServletContext(), u, r);
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
				throw new WebException(exception);
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

	public String getDescription() {

		// No scene?
		if (scene == null) {
			return null;
		}

		// Option.
		StringBuilder text = new StringBuilder();
		int option = scene.expectedOption;
		text.append("<p class='option'>");
		text.append(option > 0 ? Options.get(option) : "Unmarked");
		text.append("</p>");

		// Text.
		text.append("<p class='text'>");
		text.append(scene.command);
		text.append("</p>");

		// Info.
		text.append("<p class='info'>");
		text.append(scene.gameName);
		text.append(" (");
		text.append(scene.email);
		text.append(") - ");
		text.append(WebUtil.formatTime(scene.timeUtc));
		text.append("</p>");
		return text.toString();
	}

	private void loadScene(ServletContext context, String u, String r) {

		// User ID.
		int userId;
		try {
			userId = Integer.parseInt(u);
		} catch (NumberFormatException e) {
			return;
		}

		// Round.
		int round;
		try {
			round = Integer.parseInt(r);
		} catch (NumberFormatException e) {
			return;
		}

		// Load.
		Command command = dataService.getCommand(context, userId, round);
		if (command == null || command.command == null) {
			return;
		}

		// Scene.
		Scene s = gameService.getAddCommandScene(command.sceneNumber);
		scene = new Scene();
		scene.sceneNumber = s.sceneNumber;
		scene.fromGroup = s.fromGroup;
		scene.toGroup = s.toGroup;
		scene.fromImage = s.fromImage;
		scene.toImage = s.toImage;
		scene.command = command.command;
		scene.expectedOption = command.commandMark;
		scene.rateUserId = userId;
		scene.rateRound = round;
		scene.timeUtc = command.timeUtc;
		scene.email = command.email;
		scene.gameName = command.gameName;
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