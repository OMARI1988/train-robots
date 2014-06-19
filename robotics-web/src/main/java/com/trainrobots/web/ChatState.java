/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.web;

import javax.servlet.http.HttpSession;

import com.trainrobots.scenes.Layout;

public class ChatState {

	private Layout layout;

	public ChatState(Layout layout) {
		this.layout = layout;
	}

	public static ChatState get(HttpSession session) {
		return (ChatState) session.getAttribute("chat-state");
	}

	public Layout layout() {
		return layout;
	}

	public void layout(Layout layout) {
		this.layout = layout;
	}
}