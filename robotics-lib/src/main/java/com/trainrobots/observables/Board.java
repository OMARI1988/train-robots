/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Types;

public class Board extends Observable {

	public static final Board board() {
		return new Board();
	}

	private Board() {

	}

	@Override
	public Losr toLosr() {
		return new Entity(Types.Board);
	}
}