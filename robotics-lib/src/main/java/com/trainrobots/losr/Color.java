/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.SingleItem;

public class Color extends Terminal {

	private final Colors color;

	public Color(Colors color) {
		super(null);
		this.color = color;
	}

	public Color(TokenContext context, Colors color) {
		super(context);
		this.color = color;
	}

	public Colors color() {
		return color;
	}

	@Override
	public String name() {
		return "color";
	}

	@Override
	public Items<String> detail() {
		return new SingleItem(content());
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Color) {
			Color color = (Color) losr;
			return color.id == id && color.referenceId == referenceId
					&& color.color == this.color;
		}
		return false;
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(content());
	}

	private String content() {
		return color.toString().toLowerCase();
	}
}