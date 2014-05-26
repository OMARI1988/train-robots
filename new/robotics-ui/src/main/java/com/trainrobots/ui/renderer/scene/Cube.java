/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

public class Cube extends Model {

	private static final int[] inds = { 0, 1, 2, 3, 7, 6, 5, 4, 1, 0, 4, 5, 3,
			2, 6, 7, 5, 6, 2, 1, 7, 4, 0, 3 };

	public Cube(double w, double h, double d, float[] ambient, float[] diffuse) {
		super(false, new double[] { 0, 0, 0, 0, h, 0, 0, h, -d, 0, 0, -d, w, 0,
				0, w, h, 0, w, h, -d, w, 0, -d }, inds, ambient, diffuse);
	}
}