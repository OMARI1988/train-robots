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

package com.trainrobots.core.configuration;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.io.FileReader;

public class ConfigurationReader {

	private ConfigurationReader() {
	}

	public static List<Configuration> read(String filename) {

		// Open file.
		FileReader reader = new FileReader(filename, "UTF-8");
		List<Configuration> configurations = new ArrayList<Configuration>();

		// Read.
		String line;
		Configuration c = null;
		while ((line = reader.readLine()) != null) {
			if (line.length() == 0) {
				if (c != null) {
					configurations.add(c);
					c = null;
				}
			} else {
				if (c == null) {
					c = new Configuration();
					readHeader(c, line);
				} else if (c.blocks == null) {
					c.blocks = new ArrayList<Block>();
					readArm(c, line);
				} else {
					readBlock(c, line);
				}
			}
		}

		// Last configuration.
		if (c != null) {
			configurations.add(c);
		}

		// Close file.
		reader.close();
		return configurations;
	}

	private static void readHeader(Configuration c, String line) {
		String[] items = line.split("\t");
		c.groupNumber = Integer.parseInt(items[0]);
		c.imageNumber = Integer.parseInt(items[1]);
	}

	private static void readArm(Configuration c, String line) {
		String[] items = line.split("\t");
		c.armX = Integer.parseInt(items[0]);
		c.armY = Integer.parseInt(items[1]);
		c.armZ = Integer.parseInt(items[2]);
		c.gripperOpen = items[3].equals("O");
	}

	private static void readBlock(Configuration c, String line) {
		String[] items = line.split("\t");
		Block b = new Block();
		b.x = Integer.parseInt(items[0]);
		b.y = Integer.parseInt(items[1]);
		b.z = Integer.parseInt(items[2]);
		b.color = items[3].charAt(0);
		b.type = Integer.parseInt(items[4]);
		c.blocks.add(b);
	}
}