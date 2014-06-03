/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.planner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.Robotics;
import com.trainrobots.collections.Items;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;

public class PlannerTests {

	@Test
	public void shouldTranslateLosr() {

		// Translate.
		int valid = 0;
		int total = 0;
		Planner planner = new Planner();
		for (Command command : Robotics.system().commands()) {
			if (command.losr() != null) {
				Scene scene = command.scene();
				Layout before = scene.before();
				Layout after = scene.after();
				Instruction actual = planner.translate(before, command.losr());
				Instruction expected = planner.instruction(before, after);
				if (actual.equals(expected)) {
					valid++;
				}
				total++;
			}
		}

		// Diagnostics.
		System.out.println(String.format("Translated: %d / %d", valid, total));
	}

	@Test
	public void shouldInferInstructions() {
		for (Scene scene : Robotics.system().scenes()) {
			if (hasLosr(scene)) {
				Planner planner = new Planner();
				Instruction instruction = planner.instruction(scene.before(),
						scene.after());
				assertThat(instruction, is(not(nullValue())));
			}
		}
	}

	private boolean hasLosr(Scene scene) {
		Items<Command> commands = Robotics.system().commands(scene);
		if (commands == null) {
			return false;
		}
		for (Command command : commands) {
			if (command.losr() != null) {
				return true;
			}
		}
		return false;
	}
}