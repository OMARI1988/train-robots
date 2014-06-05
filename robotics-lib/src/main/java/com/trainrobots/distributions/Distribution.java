/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.distributions;

import com.trainrobots.scenes.Layout;

public abstract class Distribution {

	protected final Layout layout;

	protected Distribution(Layout layout) {
		this.layout = layout;
	}

	public Layout layout() {
		return layout;
	}
}