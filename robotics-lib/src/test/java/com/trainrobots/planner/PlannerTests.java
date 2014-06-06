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

import com.trainrobots.TestContext;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.instructions.MoveInstruction;
import com.trainrobots.losr.Event;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;

public class PlannerTests {

	@Test
	public void shouldGetInstruction() {

		// Event.
		Command command = TestContext.treebank().command(4436);
		Event event = (Event) command.losr();

		// Planner.
		Planner planner = new Planner(command.scene().before());

		// Instruction.
		assertThat(planner.instruction(event), is(new MoveInstruction(
				new Position(1, 6, 1), new Position(7, 0, 0))));
	}

	@Test
	public void shouldGetInstructions() {

		// Translate.
		int valid = 0;
		int total = 0;
		for (Command command : TestContext.treebank().commands()) {
			if (command.losr() != null) {
				Scene scene = command.scene();
				Instruction expected = scene.instruction();
				try {
					Planner planner = new Planner(scene.before());
					Instruction actual = planner.instruction(command.losr());
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

		// Verify.
		if (valid != total) {
			System.out.println(String.format("Instructions: %d / %d = %.2f %%",
					valid, total, 100.0 * valid / total));
		}
		assertThat(valid, is(3408));
		assertThat(total, is(3408));
	}
}