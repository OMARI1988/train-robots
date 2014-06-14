/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import com.trainrobots.losr.Losr;
import com.trainrobots.scenes.Position;

public abstract class Observable {

	public Position referencePoint() {
		return null;
	}

	public abstract Losr toLosr();

	@Override
	public String toString() {
		return toLosr().toString();
	}
}