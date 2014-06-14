/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Indicators;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Types;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;

public class ActivePosition extends Observable {

	private final Layout layout;

	public ActivePosition(Layout layout) {
		this.layout = layout;
	}

	@Override
	public Position referencePoint() {
		return layout.gripper().position();
	}

	@Override
	public Losr toLosr() {
		return new Entity(Indicators.Active, Types.Position);
	}
}