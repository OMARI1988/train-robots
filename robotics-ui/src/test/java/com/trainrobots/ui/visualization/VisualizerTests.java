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

import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.nlp.losr.Ellipsis;
import com.trainrobots.nlp.losr.EllipticalContext;
import com.trainrobots.nlp.losr.PartialTree;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.UiContext;
import com.trainrobots.ui.visualization.themes.Themes;
import com.trainrobots.ui.visualization.writers.PngWriter;
import com.trainrobots.ui.visualization.writers.SvgWriter;

public class VisualizerTests {

	@Test
	public void shouldRenderSvg() {

		// Render.
		Command command = UiContext.treebank().command(22473);
		byte[] data = new SvgWriter(command).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-22473.svg")));
	}

	@Test
	public void shouldRenderSvgWithEllipsis() {

		// Render.
		Command command = UiContext.treebank().command(13013);
		byte[] data = new SvgWriter(command).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-13013.svg")));
	}

	@Test
	public void shouldRenderPng() {

		// Render.
		Command command = UiContext.treebank().command(22473);
		byte[] data = new PngWriter(command).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-22473.png")));
	}

	@Test
	public void shouldRenderDarkPng() {

		// Render.
		Command command = UiContext.treebank().command(22473);
		byte[] data = new PngWriter(command, Themes.Dark).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-22473-dark.png")));
	}

	@Test
	public void shouldRenderEllipsis() {

		// Render.
		Command command = UiContext.treebank().command(13013);
		byte[] data = new PngWriter(command).renderToArray();

		// Verify.
		assertThat(data, is(resource("losr-13013.png")));
	}

	@Test
	public void shouldRenderPartialTree() {

		// Render.
		Command command = UiContext.treebank().command(16549);
		PartialTree partialTree = new PartialTree(command.tokens());
		partialTree.add(Losr.parse("(color: white (token: 3))"));
		byte[] data = new PngWriter(partialTree, Themes.Detail).renderToArray();

		// Verify.
		assertThat(data, is(resource("partial-16549.png")));
	}

	@Test
	public void shouldRenderPartialEllipsis1() {

		// Render.
		Command command = UiContext.treebank().command(4674);
		PartialTree partialTree = new PartialTree(command.tokens());
		partialTree.add(new Ellipsis(6));
		partialTree.add(Losr.parse("(color: green (token: 11))"));
		partialTree.add(Losr.parse("(relation: above (token: 7 9))"));
		partialTree.add(Losr.parse("(action: drop (token: 6))"));
		byte[] data = new PngWriter(partialTree, Themes.Detail).renderToArray();

		// Verify.
		assertThat(data, is(resource("partial-4674-1.png")));
	}

	@Test
	public void shouldRenderPartialEllipsis2() {

		// Render.
		Command command = UiContext.treebank().command(4674);
		PartialTree partialTree = new PartialTree(command.tokens());
		partialTree.add(Losr.parse("(action: take (token: 1))"));
		partialTree.add(Losr.parse("(color: blue (token: 3))"));
		partialTree.add(Losr.parse("(type: cube (token: 4))"));
		partialTree.add(Losr.parse("(relation: above (token: 7 9))"));
		partialTree.add(Losr.parse("(action: drop (token: 6))"));
		partialTree.add(Losr.parse("(color: green (token: 11))"));
		partialTree.add(Losr.parse("(type: cube (token: 12))"));
		partialTree.add(new Ellipsis(6));
		byte[] data = new PngWriter(partialTree, Themes.Detail).renderToArray();

		// Verify.
		assertThat(data, is(resource("partial-4674-2.png")));
	}

	@Test
	public void shouldRenderPartialEllipsis3() {

		// Render.
		Command command = UiContext.treebank().command(4674);
		PartialTree partialTree = new PartialTree(command.tokens());
		partialTree.add(Losr.parse("(action: take (token: 1))"));
		partialTree.add(Losr.parse("(color: blue (token: 3))"));
		partialTree.add(Losr.parse("(type: cube (token: 4))"));
		partialTree.add(Losr.parse("(relation: above (token: 7 9))"));
		partialTree.add(Losr.parse("(action: drop (token: 6))"));
		partialTree.add(Losr.parse("(color: green (token: 11))"));
		partialTree.add(Losr.parse("(type: cube (token: 12))"));
		Ellipsis ellipsis = new Ellipsis(6);
		partialTree.add(ellipsis);
		partialTree.remove(ellipsis);
		partialTree.add(new Type(new EllipticalContext(6), Types.Reference));
		byte[] data = new PngWriter(partialTree, Themes.Detail).renderToArray();

		// Verify.
		assertThat(data, is(resource("partial-4674-3.png")));
	}
}