/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import com.trainrobots.ui.visualization.VisualContext;

public class Token extends Text {

	private final int id;

	public Token(VisualContext context, int id, String text, boolean skip) {
		super(context, text, context.theme().font(), skip ? context.theme()
				.skip() : context.theme().foreground());
		this.id = id;
	}

	public int id() {
		return id;
	}
}