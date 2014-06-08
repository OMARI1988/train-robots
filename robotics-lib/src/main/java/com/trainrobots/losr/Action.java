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
	public String name() {
		return "action";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Action) {
			Action action = (Action) losr;
			return action.id == id && action.referenceId == referenceId
					&& action.action == this.action;
		}
		return false;
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(content());
	}

	private String content() {
		return action.toString().toLowerCase();
	}
}