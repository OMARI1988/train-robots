/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.scene;

import javax.swing.Spring;

import com.trainrobots.NotImplementedException;

public class FractionSpring extends Spring {

	private final Spring parent;
	private final double fraction;

	public FractionSpring(Spring parent, double fraction) {
		this.parent = parent;
		this.fraction = fraction;
	}

	public static FractionSpring half(Spring spring) {
		return new FractionSpring(spring, 0.5);
	}

	@Override
	public int getValue() {
		return (int) Math.round(parent.getValue() * fraction);
	}

	@Override
	public int getPreferredValue() {
		return (int) Math.round(parent.getPreferredValue() * fraction);
	}

	@Override
	public int getMinimumValue() {
		return (int) Math.round(parent.getMinimumValue() * fraction);
	}

	@Override
	public int getMaximumValue() {
		return (int) Math.round(parent.getMaximumValue() * fraction);
	}

	@Override
	public void setValue(int val) {
		if (val != UNSET) {
			throw new NotImplementedException();
		}
	}
}