/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer;

import static com.trainrobots.ui.Resources.resource;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.scenes.Layout;
import com.trainrobots.ui.UiContext;
import com.trainrobots.ui.renderer.scene.LayoutElement;

public class RendererTests {

	@Test
	public void shouldRenderPng() {

		// Render.
		Layout layout = UiContext.treebank().layout(251);
		Buffer buffer = new Buffer(new LayoutElement(layout), 325, 350);
		byte[] data = buffer.renderToArray();
		buffer.close();

		// Verify.
		assertThat(data, is(resource("layout-251.png")));
	}
}