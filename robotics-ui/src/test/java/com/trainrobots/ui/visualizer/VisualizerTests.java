/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer;

import static com.trainrobots.ui.Resources.resource;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.TestContext;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.visualizer.losr.LosrTree;

public class VisualizerTests {

	@Test
	public void shouldRenderPng() {

		// Render.
		Command command = TestContext.treebank().command(22473);
		LosrTree tree = new LosrTree(command);
		byte[] data = new PngWriter(tree).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-22473.png")));
	}

	@Test
	public void shouldRenderDarkPng() {

		// Render.
		Command command = TestContext.treebank().command(22473);
		LosrTree tree = new LosrTree(command);
		byte[] data = new PngWriter(tree, true).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-22473-dark.png")));
	}
}