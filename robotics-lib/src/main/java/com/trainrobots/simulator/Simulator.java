/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.simulator;

import com.trainrobots.RoboticException;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;

public class Simulator {

	private final Layout layout;

	public Simulator(Layout layout) {
		this.layout = layout;
	}

	public Layout layout() {
		return layout;
	}

	public Position dropPosition(Position position) {
		int x = position.x();
		int y = position.y();
		for (int z = 0; z <= 7; z++) {
			Position result = new Position(x, y, z);
			if (layout.shape(result) == null) {
				return result;
			}
		}
		throw new RoboticException("Failed to find drop position.");
	}
}