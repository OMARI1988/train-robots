/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.scenes.Scene;

public class Command {

	private final int id;
	private final Scene scene;
	private final String text;
	private final Items<Terminal> tokens;
	private Losr losr;
	private String comment;

	public Command(int id, Scene scene, String text, Items<Terminal> tokens) {
		this.id = id;
		this.scene = scene;
		this.text = text;
		this.tokens = tokens;
	}

	public int id() {
		return id;
	}

	public Scene scene() {
		return scene;
	}

	public String text() {
		return text;
	}

	public Items<Terminal> tokens() {
		return tokens;
	}

	public Losr losr() {
		return losr;
	}

	public void losr(Losr losr) {
		this.losr = losr;
	}

	public String comment() {
		return comment;
	}

	public void comment(String comment) {
		this.comment = comment;
	}
}