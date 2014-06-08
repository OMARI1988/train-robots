/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Indicator extends Terminal {

	private final Indicators indicator;

	public Indicator(Indicators indicator) {
		super(null);
		this.indicator = indicator;
	}

	public Indicator(TokenContext context, Indicators indicator) {
		super(context);
		this.indicator = indicator;
	}

	public Indicators indicator() {
		return indicator;
	}

	@Override
	public String name() {
		return "indicator";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Indicator) {
			Indicator indicator = (Indicator) losr;
			return indicator.id == id && indicator.referenceId == referenceId
					&& indicator.indicator == this.indicator;
		}
		return false;
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(indicator.toString().toLowerCase());
	}
}