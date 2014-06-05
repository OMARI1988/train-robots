/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.planner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.Robotics;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.instructions.TakeInstruction;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;

public class PlannerTests {

	@Test
	public void shouldTranslateLosr() {

		// Translate.
		int valid = 0;
		int total = 0;
		for (Command command : Robotics.system().commands()) {
			if (command.losr() != null) {
				Scene scene = command.scene();
				Instruction expected = scene.instruction();
				if (!(expected instanceof TakeInstruction)) {
					continue;
				}
				try {
					Planner planner = new Planner(scene.before());
					Instruction actual = planner.translate(command.losr());
					if (actual.equals(expected)) {
						valid++;
					}
				} catch (Exception exception) {
					System.out.println(command.id() + " "
							+ exception.getMessage());
				}
				total++;
			}
		}

		// Diagnostics.
		System.out.println(String.format("Translated: %d / %d", valid, total));
		assertThat(valid, is(282));
		assertThat(total, is(516));
	}
}