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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.trainrobots.web.WebException;
import com.trainrobots.web.game.Scene;
import com.trainrobots.web.game.User;
import com.trainrobots.web.services.GameService;
import com.trainrobots.web.services.ServiceContext;

public class ScenePage {

	private final GameService gameService = ServiceContext.get().gameService();
	private Scene scene;

	public void initiate(PageContext pageContext, String id) {

		// Load.
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && gameService.isAdmin(user.email)) {
			loadScene(id);
		}

		// Redirect.
		if (scene == null) {
			try {
				((HttpServletResponse) pageContext.getResponse())
						.sendRedirect("/lost.jsp");
			} catch (IOException exception) {
				throw new WebException(exception);
			}
		}
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

	private void loadScene(String id) {

		// Parse.
		int sceneNumber;
		try {
			sceneNumber = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			return;
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