/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Types;
import com.trainrobots.scenes.Shape;

public class FormatTests {

	@Test
	public void shouldFormatShape() {
		assertThat(new Shape(Types.Prism, Colors.Blue, null).toString(),
				is("(entity: (color: blue) (type: prism))"));
	}

	@Test
	public void shouldFormatEdge() {
		assertThat(Edge.Front.toString(),
				is("(entity: (indicator: front) (type: edge))"));
	}

	@Test
	public void shouldFormatRegion() {
		assertThat(Region.Left.toString(),
				is("(entity: (indicator: left) (type: region))"));
	}

	@Test
	public void shouldFormatCorner() {
		assertThat(
				Corner.BackRight.toString(),
				is("(entity: (indicator: back) (indicator: right) (type: corner))"));
	}

	@Test
	public void shouldFormatBoard() {
		assertThat(Board.board().toString(), is("(entity: (type: board))"));
	}

	@Test
	public void shouldFormatRobot() {
		assertThat(Robot.robot().toString(), is("(entity: (type: robot))"));
	}
}