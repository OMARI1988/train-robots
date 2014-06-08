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

	public Detail(VisualContext context, String text) {
		super(context, '(' + text + ')', context.theme().font(), context
				.theme().detail());
	}
}