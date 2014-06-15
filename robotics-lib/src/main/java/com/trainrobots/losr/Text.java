/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Text extends Terminal {

	protected final String text;

	public Text(String text) {
		this(null, text);
	}

	public Text(TextContext context, String text) {
		super(context);
		this.text = text;
	}

	public String text() {
		return text;
	}

	@Override
	public String name() {
		return "text";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Text) {
			Text text = (Text) losr;
			return text.id == id && text.referenceId == referenceId
					&& text.text.equals(this.text);
		}
		return false;
	}

	@Override
	public Text clone() {
		return new Text(context, text);
	}

	@Override
	public Text withContext(TextContext context) {
		return new Text(context, text);
	}

	@Override
	protected Object content() {
		return text;
	}
}