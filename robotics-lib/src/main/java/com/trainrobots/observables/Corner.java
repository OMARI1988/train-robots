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
import com.trainrobots.scenes.Position;

public class Corner extends Observable {

	public static final Corner FrontLeft = new Corner(Indicators.Front,
			Indicators.Left, new Position(7, 7, 0));

	public static final Corner FrontRight = new Corner(Indicators.Front,
			Indicators.Right, new Position(7, 0, 0));

	public static final Corner BackLeft = new Corner(Indicators.Back,
			Indicators.Left, new Position(0, 7, 0));

	public static final Corner BackRight = new Corner(Indicators.Back,
			Indicators.Right, new Position(0, 0, 0));

	private final Indicators frontOrBack;
	private final Indicators leftOrRight;
	private final Position position;

	private Corner(Indicators frontOrBack, Indicators leftOrRight,
			Position position) {
		this.frontOrBack = frontOrBack;
		this.leftOrRight = leftOrRight;
		this.position = position;
	}

	public Indicators frontOrBack() {
		return frontOrBack;
	}

	public Indicators leftOrRight() {
		return leftOrRight;
	}

	@Override
	public Position referencePoint() {
		return position;
	}

	@Override
	public Losr toLosr() {
		return new Entity(0, 0, new ItemsArray(new Indicator(frontOrBack),
				new Indicator(leftOrRight), new Type(Types.Corner)));
	}
}