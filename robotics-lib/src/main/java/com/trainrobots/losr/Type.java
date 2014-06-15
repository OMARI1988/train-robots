/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Type extends Terminal {

	private Types type;

	public Type(Types type) {
		this(null, type);
	}

	public Type(TextContext context, Types type) {
		super(context);
		this.type = type;
	}

	public Types type() {
		return type;
	}

	public void type(Types type) {
		this.type = type;
	}

	@Override
	public String name() {
		return "type";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Type) {
			Type type = (Type) losr;
			return type.id == id && type.referenceId == referenceId
					&& type.type == this.type;
		}
		return false;
	}
	
	@Override
	public Type clone() {
		return new Type(context, type);
	}

	@Override
	public Type withContext(TextContext context) {
		return new Type(context, type);
	}

	@Override
	protected Object content() {
		return type;
	}
}