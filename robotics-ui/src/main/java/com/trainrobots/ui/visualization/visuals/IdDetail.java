/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import com.trainrobots.ui.visualization.VisualContext;

public class IdDetail extends Detail {

	public IdDetail(VisualContext context, Header header, int id) {
		super(context, header, "id: " + id);
	}
}