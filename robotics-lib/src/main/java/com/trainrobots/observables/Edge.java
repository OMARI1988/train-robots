/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Indicators;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Types;
import com.trainrobots.scenes.Position;

public class Edge extends Observable {

	public static final Edge Left = new Edge(Indicators.Left);
	public static final Edge Right = new Edge(Indicators.Right);
	public static final Edge Front = new Edge(Indicators.Front);
	public static final Edge Back = new Edge(Indicators.Back);

	private final Indicators indicator;

	private Edge(Indicators indicator) {
		this.indicator = indicator;

	}

	public Indicators indicator() {
		return indicator;
	}

	@Override
	public Losr toLosr() {
		return new Entity(indicator, Types.Edge);
	}

	public boolean supports(Position position) {
		switch (indicator) {
		case Left:
			return position.y() == 7;
		case Right:
			return position.y() == 0;
		case Front:
			return position.x() == 7;
		case Back:
			return position.x() == 0;
		}
		throw new RoboticException("Invalid edge.");
	}
}