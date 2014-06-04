/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import com.trainrobots.instructions.Instruction;

public class Scene {

	private final int id;
	private final Layout before;
	private final Layout after;
	private final Instruction instruction;

	public Scene(int id, Layout before, Layout after, Instruction instruction) {
		this.id = id;
		this.before = before;
		this.after = after;
		this.instruction = instruction;
	}

	public int id() {
		return id;
	}

	public Layout before() {
		return before;
	}

	public Layout after() {
		return after;
	}

	public Instruction instruction() {
		return instruction;
	}
}