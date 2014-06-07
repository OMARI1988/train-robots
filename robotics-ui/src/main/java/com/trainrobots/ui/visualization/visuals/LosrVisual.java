/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import java.util.ArrayList;
import java.util.List;

public class LosrVisual extends Visual {

	private final Text tag;
	private final List<LosrVisual> losrChildren = new ArrayList<LosrVisual>();

	public boolean skip;

	public LosrVisual(Text tag) {
		this.tag = tag;
		add(tag);
	}

	public Text tag() {
		return tag;
	}

	public Iterable<LosrVisual> losrChildren() {
		return losrChildren;
	}

	public boolean hasLayoutChildren() {
		return losrChildren.size() != 0;
	}

	public void add(LosrVisual visual) {
		super.add(visual);
		losrChildren.add(visual);
	}
}