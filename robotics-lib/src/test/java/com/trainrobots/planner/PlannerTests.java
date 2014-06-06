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
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.distributions.observable.ObservableHypothesis;
import com.trainrobots.instructions.Instruction;
import com.trainrobots.instructions.MoveInstruction;
import com.trainrobots.losr.Entity;
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
	public void shouldGetInstruction2() {
		Command command = TestContext.treebank().command(14852);
		Planner planner = new Planner(command.scene().before());
		planner.instruction(command.losr());
	}

	@Test
	public void shouldGetDistribution() {

		// Entity.
		Command command = TestContext.treebank().command(19824);
		Event event = (Event) command.losr();
		Entity entity = event.destination().entity();

		// Planner.
		Planner planner = new Planner(command.scene().before());

		// Distribution.
		ObservableDistribution distribution = planner.distribution(entity);
		for (ObservableHypothesis hypothesis : distribution) {
			System.out.println(hypothesis);
		}
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

		// Diagnostics.
		System.out.println(String.format("Instructions: %d / %d = %.2f %%",
				valid, total, 100.0 * valid / total));
		assertThat(valid, is(3387));
		assertThat(total, is(3409));
	}
}