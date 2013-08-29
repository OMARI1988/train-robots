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

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.ui.io.ConfigurationWriter;

public class ConfigurationTests {

	@Test
	public void shouldSerializeConfiguration() {

		Configuration configuration = new Configuration();
		configuration.armX = 3;
		configuration.armY = 2;
		configuration.armZ = 7;
		configuration.gripperOpen = true;
		configuration.armBlock = new Block(Block.RED, Block.CUBE);

		configuration.blocks = new ArrayList<Block>();
		configuration.blocks
				.add(new Block(Block.YELLOW, Block.PYRAMID, 1, 2, 8));
		configuration.blocks.add(new Block(Block.RED, Block.CUBE, 8, 4, 3));

		assertEquals("3\t2\t7\tO\tR\t1\r\n1\t2\t8\tY\t2\r\n8\t4\t3\tR\t1",
				configuration.toString());
	}

	@Test
	@Ignore
	public void generateConfiguration() {

		List<Configuration> configurations = new ArrayList<Configuration>();
		for (int g = 1; g <= 125; g++) {
			for (int i = 1; i <= 5; i++) {

				Configuration configuration = new Configuration();
				configuration.groupNumber = g;
				configuration.imageNumber = i;
				configuration.armX = 3;
				configuration.armY = 2;
				configuration.armZ = 7;
				configuration.gripperOpen = true;
				configuration.armBlock = new Block(Block.RED, Block.CUBE);

				configuration.blocks = new ArrayList<Block>();
				configuration.blocks.add(new Block(Block.YELLOW, Block.PYRAMID,
						1, 2, 8));
				configuration.blocks.add(new Block(Block.RED, Block.CUBE, 8, 4,
						3));
				configurations.add(configuration);
			}
		}

		ConfigurationWriter.write("c:/temp/train.robo", configurations);
	}
}