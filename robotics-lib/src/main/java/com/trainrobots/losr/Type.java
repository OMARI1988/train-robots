/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Type extends Terminal {

	private final Types type;

	public Type(Types type) {
		super(null);
		this.type = type;
	}

	public Type(TokenContext context, Types type) {
		super(context);
		this.type = type;
	}

	public Types type() {
		return type;
	}

	@Override
	public boolean equals(Losr losr) {
		return losr instanceof Type && ((Type) losr).type == type;
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("type");
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(type.toString().toLowerCase());
	}
}