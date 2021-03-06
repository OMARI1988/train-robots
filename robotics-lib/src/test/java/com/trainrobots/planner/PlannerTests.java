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

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.Context;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.scenes.Scene;
import com.trainrobots.treebank.Command;

public class PlannerTests {

	@Test
	@Ignore
	public void shouldFindUnreviewedCommands() {
		int i = 0;
		for (Command command : Context.treebank().commands()) {
			if (command.losr() != null || command.scene().instruction() == null) {
				continue;
			}
			if (command.comment() == null) {
				System.out.println(++i + " | " + command.id() + " | "
						+ command.text());
			}
		}
	}

	@Test
	@Ignore
	public void shouldGetInstruction() {

		// Planner.
		Command command = Context.treebank().command(4851);
		Planner planner = new Planner(command.scene().before());

		// Instruction.
		Instruction expected = command.scene().instruction();
		assertThat(planner.instruction(command.losr()), is(expected));
	}

	@Test
	public void shouldGetInstructions() {

		// Translate.
		int valid = 0;
		int total = 0;
		for (Command command : Context.treebank().commands()) {
			if (command.losr() != null) {
				Scene scene = command.scene();
				Instruction expected = scene.instruction();
				try {
					Planner planner = new Planner(scene.before());
					Instruction actual = planner.instruction(command.losr());
					if (actual.equals(expected)) {
						valid++;
					} else {
						System.out.println(String.format(
								"%d: Expected %s. Actual %s.", command.id(),
								expected, actual));
					}
				} catch (Exception exception) {
					System.out.println(command.id() + ": "
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
		assertThat(valid, is(4850));
		assertThat(total, is(4850));
	}
}