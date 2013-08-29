/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.ui.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.trainrobots.ui.io.ConfigurationReader;

public class ConfigurationTests {

	@Test
	public void shouldReadConfiguration() {
		for (Configuration c : ConfigurationReader
				.read("../data/configuration.txt")) {
			assertTrue(c.blocks.size() >= 1);
		}
	}

	@Test
	public void shouldWriteConfiguration() {

		Configuration configuration = new Configuration();
		configuration.armX = 3;
		configuration.armY = 2;
		configuration.armZ = 7;
		configuration.gripperOpen = true;

		configuration.blocks = new ArrayList<Block>();
		configuration.blocks
				.add(new Block(Block.YELLOW, Block.PYRAMID, 1, 2, 7));
		configuration.blocks.add(new Block(Block.RED, Block.CUBE, 7, 4, 3));

		assertEquals("3\t2\t7\tO\r\n1\t2\t7\tY\t2\r\n7\t4\t3\tR\t1",
				configuration.toString());
	}
}