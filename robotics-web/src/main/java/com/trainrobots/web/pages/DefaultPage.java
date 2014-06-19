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

import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Scene;
import com.trainrobots.scenes.Scenes;
import com.trainrobots.web.Application;
import com.trainrobots.web.InstructionWriter;

public class DefaultPage {

	private Application application;
	private Scene scene;

	public void initiate(ServletContext context) {
		this.application = Application.get(context);
		Scenes scenes = application.treebank().scenes();
		scene = scenes.get(new Random().nextInt(scenes.count()));
	}

	public Scene scene() {
		return scene;
	}

	public String instructions() {
		Layout layout = scene.before();
		return new InstructionWriter(layout).write();
	}
}