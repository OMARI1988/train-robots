/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.trainrobots.scenes.Position;

public class PositionTests {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void shouldPackPositions() {
		for (int x = -5; x < 15; x++) {
			for (int y = -5; y < 15; y++) {
				for (int z = -5; z < 15; z++) {
					Position p = new Position(x, y, z);
					assertThat(p.x(), is(x));
					assertThat(p.y(), is(y));
					assertThat(p.z(), is(z));
				}
			}
		}
	}

	@Test
	public void shouldComparePositions() {
		Position p1 = new Position(4, 5, 6);
		Position p2 = new Position(4, 5, 6);
		Position p3 = new Position(4, 3, 6);

		assertThat(p1, is(p1));
		assertThat(p1, is(p2));
		assertThat(p1, is(not(p3)));
	}

	@Test
	public void shouldFormatPositions() {
		assertThat(new Position(-1, -1, -1).toString(), is("-1 -1 -1"));
		assertThat(new Position(0, 0, 0).toString(), is("0 0 0"));
		assertThat(new Position(4, 5, 6).toString(), is("4 5 6"));
		assertThat(new Position(21, -22, 23).toString(), is("21 -22 23"));
	}

	@Test
	public void shouldParsePositions() {
		assertThat(Position.parse("-1 -1 -1"), is(new Position(-1, -1, -1)));
		assertThat(Position.parse("0 0 0"), is(new Position(0, 0, 0)));
		assertThat(Position.parse("4 5 6"), is(new Position(4, 5, 6)));
		assertThat(Position.parse("21 -22 23"), is(new Position(21, -22, 23)));
	}

	@Test
	public void shouldThrowForInvalidPosition() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("The position '0 0 -200' is out of range.");
		new Position(0, 0, -200);
	}

	@Test
	public void shouldThrowForInvalidParse1() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Failed to parse null position.");
		Position.parse(null);
	}

	@Test
	public void shouldThrowForInvalidParse2() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("The position '' is not valid.");
		Position.parse("");
	}

	@Test
	public void shouldThrowForInvalidParse3() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("The position 'foo' is not valid.");
		Position.parse("foo");
	}

	@Test
	public void shouldThrowForInvalidParse4() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("The position '2 3' is not valid.");
		Position.parse("2 3");
	}

	@Test
	public void shouldThrowForInvalidParse5() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("The position '2 3 4 ' is not valid.");
		Position.parse("2 3 4 ");
	}

	@Test
	public void shouldThrowForInvalidParse6() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("The position '2 3 4 5' is not valid.");
		Position.parse("2 3 4 5");
	}

	@Test
	public void shouldThrowForInvalidParse7() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("The position '200 1 5' is out of range.");
		Position.parse("200 1 5");
	}

	@Test
	public void shouldThrowForInvalidParse8() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("The position ' 2 3 4' is not valid.");
		Position.parse(" 2 3 4");
	}
}