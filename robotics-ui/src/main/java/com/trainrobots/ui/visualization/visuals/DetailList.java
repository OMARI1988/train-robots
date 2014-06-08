/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import com.trainrobots.collections.ItemsList;
import com.trainrobots.ui.visualization.VisualContext;

public class DetailList extends ItemsList<Detail> {

	private final VisualContext context;
	private final Header header;

	public DetailList(VisualContext context, Header header) {
		this.context = context;
		this.header = header;
	}

	public void add(String text) {
		add(new Detail(context, header, text));
	}
}