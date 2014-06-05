/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import com.trainrobots.collections.ItemsArray;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Indicators;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;

public class Corner extends Observable {

	public static final Corner FrontLeft = new Corner(Indicators.Front,
			Indicators.Left);

	public static final Corner FrontRight = new Corner(Indicators.Front,
			Indicators.Right);

	public static final Corner BackLeft = new Corner(Indicators.Back,
			Indicators.Left);

	public static final Corner BackRight = new Corner(Indicators.Back,
			Indicators.Right);

	private final Indicators frontOrBack;
	private final Indicators leftOrRight;

	private Corner(Indicators frontOrBack, Indicators leftOrRight) {
		this.frontOrBack = frontOrBack;
		this.leftOrRight = leftOrRight;

	}

	public Indicators frontOrBack() {
		return frontOrBack;
	}

	public Indicators leftOrRight() {
		return leftOrRight;
	}

	@Override
	public Losr toLosr() {
		return new Entity(0, 0, null, new ItemsArray<Indicator>(new Indicator(
				frontOrBack), new Indicator(leftOrRight)), null, new Type(
				Types.Corner), null);
	}
}