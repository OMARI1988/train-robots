/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.command;

import com.trainrobots.losr.Terminal;

public class LosrType {

	private final Terminal terminal;

	public LosrType(Terminal terminal) {
		this.terminal = terminal;
	}

	public Terminal terminal() {
		return terminal;
	}

	@Override
	public String toString() {
		return terminal.name();
	}
}