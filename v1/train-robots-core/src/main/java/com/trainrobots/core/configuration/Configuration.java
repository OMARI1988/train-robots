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

import java.util.List;

public class Configuration {

	public int groupNumber;
	public int imageNumber;

	public List<Block> blocks;

	public int armX;
	public int armY;
	public int armZ;
	public boolean gripperOpen;

	@Override
	public String toString() {

		// Arm fields.
		StringBuilder text = new StringBuilder();
		text.append(armX);
		text.append('\t');
		text.append(armY);
		text.append('\t');
		text.append(armZ);
		text.append('\t');
		text.append(gripperOpen ? 'O' : 'C');

		// Blocks.
		for (Block block : blocks) {
			text.append("\r\n");
			text.append(block.x);
			text.append('\t');
			text.append(block.y);
			text.append('\t');
			text.append(block.z);
			text.append('\t');
			text.append(block.color);
			text.append('\t');
			text.append(block.type);
		}
		return text.toString();
	}
}