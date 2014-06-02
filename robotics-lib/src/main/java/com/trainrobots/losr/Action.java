/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Action extends Terminal {

	private final Actions action;

	public Action(Actions action) {
		super(null);
		this.action = action;
	}

	public Action(TokenContext context, Actions action) {
		super(context);
		this.action = action;
	}

	public Actions action() {
		return action;
	}

	@Override
	public boolean equals(Losr losr) {
		return losr instanceof Action && ((Action) losr).action == action;
	}

	@Override
	protected void write(StringBuilder text) {
		text.append("(action: ");
		text.append(action.toString().toLowerCase());
		if (context != null) {
			text.append(' ');
			text.append(context);
		}
		text.append(')');
	}
}