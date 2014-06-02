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

import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.trainrobots.web.services.CorpusService;
import com.trainrobots.web.services.ServiceContext;

public class TreebankPage {

	private final CorpusService corpusService = ServiceContext.get()
			.corpusService();

	public void initiate(PageContext pageContext) {

		// Response.
		HttpServletResponse response = (HttpServletResponse) pageContext
				.getResponse();

		// Disable caching.
		response.setHeader("Cache-Control",
				"no-cache, no-store, must-revalidate"); // HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
		response.setDateHeader("Expires", 0); // Proxies.
	}

	public String formatWordCount() {
		return NumberFormat.getNumberInstance(Locale.US).format(
				corpusService.getWordCount());
	}

	public String formatCommandCount() {
		return NumberFormat.getNumberInstance(Locale.US).format(
				corpusService.getCommandCount());
	}

	public String getScenes() {

		// Scenes.
		StringBuilder text = new StringBuilder();
		text.append("<table border=\"0\" class=\"sceneTable\" cellpadding=\"0\" cellspacing=\"0\">");
		for (int[] sceneInfo : corpusService.getSceneInfo()) {
			text.append("<tr><td><a href='scene.jsp?id=" + sceneInfo[0] + "'>");
			text.append(String.format("%04d", sceneInfo[0]));
			text.append("</a></td><td>");
			text.append(sceneInfo[1]);
			text.append(sceneInfo[1] == 1 ? " command" : " commands");
			text.append("</td></tr>");
		}
		text.append("</table>");
		return text.toString();
	}
}