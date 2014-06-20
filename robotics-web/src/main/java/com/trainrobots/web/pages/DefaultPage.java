/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.web.pages;

import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import com.trainrobots.Log;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Scene;
import com.trainrobots.scenes.Scenes;
import com.trainrobots.web.Application;
import com.trainrobots.web.ChatState;
import com.trainrobots.web.InstructionWriter;

public class DefaultPage {

	private ChatState state;

	public void initiate(ServletContext context, HttpSession session) {

		// New session?
		state = ChatState.get(session);
		if (state == null) {

			// Scene.
			Application application = Application.get(context);
			Scenes scenes = application.treebank().scenes();
			Scene scene = scenes.get(new Random().nextInt(scenes.count()));
			Log.info("Creating new session for scene %d...", scene.id());

			// Session.
			Layout layout = scene.before().clone();
			session.setAttribute("chat-state", state = new ChatState(layout));
		}
	}

	public String instructions() {
		return new InstructionWriter(state.layout()).write();
	}
}