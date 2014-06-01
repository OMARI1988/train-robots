/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

public class Robotics {

	private static RoboticSystem system;

	private Robotics() {
	}

	public static RoboticSystem system() {
		if (system == null) {
			system = new RoboticSystem("../.data");
		}
		return system;
	}
}