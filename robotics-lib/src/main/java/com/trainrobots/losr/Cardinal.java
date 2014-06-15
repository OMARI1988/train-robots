/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Cardinal extends Terminal {

	private int value;

	public Cardinal(int value) {
		this(null, value);
	}

	public Cardinal(TextContext context, int value) {
		super(context);
		this.value = value;
	}

	public int value() {
		return value;
	}

	public void value(int value) {
		this.value = value;
	}

	@Override
	public String name() {
		return "cardinal";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Cardinal) {
			Cardinal cardinal = (Cardinal) losr;
			return cardinal.id == id && cardinal.referenceId == referenceId
					&& cardinal.value == value;
		}
		return false;
	}

	@Override
	public Cardinal clone() {
		return new Cardinal(context, value);
	}

	@Override
	public Cardinal withContext(TextContext context) {
		return new Cardinal(context, value);
	}

	@Override
	protected Object content() {
		return value;
	}
}