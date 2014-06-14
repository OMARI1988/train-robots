/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.scenes.Position;

public class Weights {

	private Weights() {
	}

	public static int left(Position position) {
		return position.y();
	}

	public static int right(Position position) {
		return 7 - position.y();
	}

	public static int front(Position position) {
		return position.x();
	}

	public static int back(Position position) {
		return 7 - position.x();
	}

	public static int low(Position position) {
		return 7 - position.z();
	}

	public static int high(Position position) {
		return position.z();
	}

	public static int center(Position position) {
		return (position.x() == 3 || position.x() == 4)
				&& (position.y() == 3 || position.y() == 4) ? 1 : 0;
	}
}