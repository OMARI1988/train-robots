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
import com.trainrobots.ui.visualizer.writers.PngWriter;
import com.trainrobots.ui.visualizer.writers.SvgWriter;

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
	public void shouldRenderPngWithEllipsis() {

		// Render.
		Command command = TestContext.treebank().command(13013);
		LosrTree tree = new LosrTree(command);
		byte[] data = new PngWriter(tree).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-13013.png")));
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

	@Test
	public void shouldRenderSvg() {

		// Render.
		Command command = TestContext.treebank().command(22473);
		LosrTree tree = new LosrTree(command);
		byte[] data = new SvgWriter(tree).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-22473.svg")));
	}

	@Test
	public void shouldRenderSvgWithEllipsis() {

		// Render.
		Command command = TestContext.treebank().command(13013);
		LosrTree tree = new LosrTree(command);
		byte[] data = new SvgWriter(tree).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-13013.svg")));
	}
}