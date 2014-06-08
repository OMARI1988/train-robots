/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization;

import static com.trainrobots.ui.Resources.resource;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.TestContext;
import com.trainrobots.losr.Losr;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.visualization.themes.Themes;
import com.trainrobots.ui.visualization.writers.PngWriter;
import com.trainrobots.ui.visualization.writers.SvgWriter;

public class VisualizerTests {

	@Test
	public void shouldRenderPng() {

		// Render.
		Command command = TestContext.treebank().command(22473);
		byte[] data = new PngWriter(command).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-22473.png")));
	}

	@Test
	public void shouldRenderPngWithEllipsis() {

		// Render.
		Command command = TestContext.treebank().command(13013);
		byte[] data = new PngWriter(command).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-13013.png")));
	}

	@Test
	public void shouldRenderPngWithSkippedTokens() {

		// Render.
		Command command = TestContext.treebank().command(23991);
		byte[] data = new PngWriter(command).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-23991.png")));
	}

	@Test
	public void shouldRenderDarkPng() {

		// Render.
		Command command = TestContext.treebank().command(22473);
		byte[] data = new PngWriter(command, Themes.Dark).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-22473-dark.png")));
	}

	@Test
	public void shouldRenderSvg() {

		// Render.
		Command command = TestContext.treebank().command(22473);
		byte[] data = new SvgWriter(command).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-22473.svg")));
	}

	@Test
	public void shouldRenderSvgWithEllipsis() {

		// Render.
		Command command = TestContext.treebank().command(13013);
		byte[] data = new SvgWriter(command).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-13013.svg")));
	}

	@Test
	public void shouldRenderPartialTree() {

		// Render.
		Command command = TestContext.treebank().command(16549);
		PartialTree partialTree = new PartialTree(command.tokens());
		partialTree.add(Losr.parse("(color: red (token: 4))"));
		byte[] data = new PngWriter(partialTree, Themes.Detail).renderToArray();

		// Verify.
		assertThat(data, is(resource("partial-16549.png")));
	}
}