/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

public class Prism extends Model {

	private static final int[] indices = { 0, 2, 1, 2, 3, 1, 3, 4, 1, 4, 0, 1,
			0, 4, 2, 4, 3, 2 };

	public Prism(double w, double h, double d, float[] ambient, float[] diffuse) {
		super(3, new double[] { 0.0, 0.0, 0.0, w / 2, h, -d / 2, w, 0.0, 0.0,
				w, 0.0, -d, 0.0, 0.0, -d }, indices, ambient, diffuse);
	}
}