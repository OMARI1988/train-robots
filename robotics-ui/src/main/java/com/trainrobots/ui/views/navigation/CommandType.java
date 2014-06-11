/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.navigation;

import java.awt.Color;

import com.trainrobots.treebank.Command;

public enum CommandType {

	Ignore(Color.GRAY),
	Warning(new Color(255, 69, 0)),
	Valid(new Color(0, 200, 0)),
	NotAnnotated(Color.BLACK);

	private final Color color;

	private CommandType(Color color) {
		this.color = color;
	}

	public Color color() {
		return color;
	}

	public static CommandType of(Command command) {
		if (command.comment() != null && command.comment().startsWith("ignore")) {
			return Ignore;
		}
		if (command.comment() != null) {
			return Warning;
		}
		if (command.losr() != null) {
			return Valid;
		}
		return NotAnnotated;
	}
}