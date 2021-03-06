/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.Context;
import com.trainrobots.scenes.Scene;

public class CommandTests {

	@Test
	public void shouldReadCommands() {

		// Commands.
		Commands commands = Context.treebank().commands();
		assertThat(commands.count(), is(8970));

		// Command 25495.
		Command command = commands.command(25495);
		assertThat(command.id(), is(25495));
		assertThat(command.scene().id(), is(81));
		assertThat(
				command.text(),
				is("Lift up the red cube and place on top of the \"3 green cube\" stacked tower."));

		// Scene 879.
		Treebank treebank = Context.treebank();
		Scene scene = treebank.scene(879);
		assertThat(treebank.commands(scene).count(), is(16));
	}
}