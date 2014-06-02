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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.web.game.Scene;
import com.trainrobots.web.rcl.RclLine;
import com.trainrobots.web.rcl.RclTable;
import com.trainrobots.web.services.CorpusService;
import com.trainrobots.web.services.GameService;
import com.trainrobots.web.services.ServiceContext;

public class CommandPage {

	private final CorpusService corpusService = ServiceContext.get()
			.corpusService();
	private final GameService gameService = ServiceContext.get().gameService();
	private Command command;
	private Scene scene;

	public void initiate(PageContext pageContext, String id) {

		// Load.
		loadCommand(pageContext.getServletContext(), id);

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
	}

	public int getId() {
		return command != null ? command.id : 0;
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

	public String getTreeImage() {
		if (command == null) {
			return null;
		}
		String key = String.format("%05d", command.id);
		String a = key.substring(0, 3);
		String b = key.substring(3);
		return "/static/trees/" + a + "/" + b + ".png";
	}

	public String getDescription() {

		// No command?
		if (command == null) {
			return null;
		}

		// Text.
		return command.text;
	}

	public String getRclLines() {

		// No command?
		if (command == null || command.rcl == null) {
			return null;
		}

		// Table.
		StringBuilder text = new StringBuilder();
		for (RclLine line : new RclTable(command).lines()) {
			text.append("<tr>");
			text.append("<td class=\"rcl\">");
			text.append(line.toString());
			text.append("</td>");
			text.append("<td>");
			if (line.tokens != null) {
				text.append(line.tokens);
			}
			text.append("</td>");
			text.append("</tr>");
		}
		return text.toString();
	}

	private void loadCommand(ServletContext context, String id) {

		// Parse.
		int commandNumber;
		try {
			commandNumber = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return;
		}

		// Load.
		command = corpusService.getCommand(commandNumber);
		if (command == null || command.rcl == null) {
			return;
		}
		scene = gameService.getAddCommandScene(command.sceneNumber);
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