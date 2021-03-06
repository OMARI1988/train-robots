/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Indicator extends Terminal {

	private Indicators indicator;

	public Indicator(Indicators indicator) {
		super(null);
		this.indicator = indicator;
	}

	public Indicator(TextContext context, Indicators indicator) {
		super(context);
		this.indicator = indicator;
	}

	public Indicators indicator() {
		return indicator;
	}

	public void indicator(Indicators indicator) {
		this.indicator = indicator;
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
	public Indicator clone() {
		return new Indicator(context, indicator);
	}

	@Override
	public Indicator withContext(TextContext context) {
		return new Indicator(context, indicator);
	}

	@Override
	protected Object content() {
		return indicator;
	}
}