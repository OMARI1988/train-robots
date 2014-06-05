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

public class Region extends Observable {

	public static final Region Left = new Region(Indicators.Left);
	public static final Region Right = new Region(Indicators.Right);
	public static final Region Front = new Region(Indicators.Front);
	public static final Region Back = new Region(Indicators.Back);
	public static final Region Center = new Region(Indicators.Center);

	private final Indicators indicator;

	private Region(Indicators indicator) {
		this.indicator = indicator;

	}

	public Indicators indicator() {
		return indicator;
	}

	@Override
	public Losr toLosr() {
		return new Entity(indicator, Types.Region);
	}
}