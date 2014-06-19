/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.Context;
import com.trainrobots.scenes.Layout;

public class InstructionWriterTests {

	@Test
	public void shouldWriteInstructions() {

		// Write.
		Layout layout = Context.treebank().scene(51).after();
		InstructionWriter writer = new InstructionWriter(layout);
		String actual = writer.write();

		// Verify.
		String expected = "[[1,0,0],[2,0,7,0,4],[2,1,7,0,4],[2,4,7,0,6],[2,4,7,1,6],[2,5,7,0,6],[2,5,7,1,6],[2,0,6,0,4],[2,1,6,0,4],[2,4,6,0,6],[2,4,6,1,6],[2,5,6,0,6],[2,5,6,1,6],[2,7,5,0,7],[2,7,5,1,2],[2,7,5,2,7],[2,7,5,3,2],[2,7,5,4,7],[2,7,5,5,2],[2,7,5,6,7],[2,4,4,0,0],[2,5,4,0,0],[2,0,3,0,3],[2,0,3,1,2],[2,1,3,0,3],[2,1,3,1,2],[2,2,3,0,2],[2,2,3,1,2],[2,3,3,0,2],[2,3,3,1,2],[2,4,3,0,0],[2,4,3,1,2],[2,5,3,0,0],[2,5,3,1,2],[2,0,2,0,3],[2,0,2,1,2],[2,1,2,0,3],[2,1,2,1,2],[2,2,2,0,2],[2,2,2,1,2],[2,3,2,0,2],[2,3,2,1,2],[2,4,2,0,0],[2,4,2,1,2],[2,5,2,0,0],[2,5,2,1,2],[2,0,1,0,3],[2,1,1,0,3],[2,0,0,0,3],[2,0,0,1,2],[2,1,0,0,3],[4,0,0,7]]";
		if (!actual.equals(expected)) {
			System.out.println("EXPECTED: " + expected);
			System.out.println("ACTUAL:   " + actual);
		}
		assertThat(actual, is(expected));
	}
}