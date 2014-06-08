/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import com.trainrobots.ui.visualization.VisualContext;

public class Detail extends Text {

	private final Header header;

	public Detail(VisualContext context, Header header, String text) {
		super(context, '(' + text + ')', context.theme().font(), context
				.theme().detail());
		this.header = header;
	}

	public Header header() {
		return header;
	}
}