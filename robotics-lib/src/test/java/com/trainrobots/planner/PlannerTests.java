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

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.TestContext;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.losr.Colors;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Scene;
import com.trainrobots.scenes.Shape;
import com.trainrobots.treebank.Command;

public class PlannerTests {

	@Test
	@Ignore
	public void dumpShapes() {

		Map<Colors, Integer> values = new HashMap<>();
		values.put(Colors.Blue, 0);
		values.put(Colors.Cyan, 1);
		values.put(Colors.Red, 2);
		values.put(Colors.Yellow, 3);
		values.put(Colors.Green, 4);
		values.put(Colors.Magenta, 5);
		values.put(Colors.White, 6);
		values.put(Colors.Gray, 7);

		Layout layout = TestContext.treebank().scene(51).after();
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < 8; x++) {
				for (int z = 0; z < 8; z++) {
					Shape shape = layout.shape(new Position(x, y, z));
					if (shape != null) {
						System.out
								.println(String.format(
										"renderer.%s(%d, %d, %d, %d);", shape
												.type(), shape.position().x(),
										shape.position().y(), shape.position()
												.z(), values.get(shape.color())));
					}
				}
			}
		}
	}

	@Test
	@Ignore
	public void shouldFindUnreviewedCommands() {
		int i = 0;
		for (Command command : TestContext.treebank().commands()) {
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
		Command command = TestContext.treebank().command(4851);
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
		for (Command command : TestContext.treebank().commands()) {
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