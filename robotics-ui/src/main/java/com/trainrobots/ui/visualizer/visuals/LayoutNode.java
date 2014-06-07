/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.visuals;

import java.util.ArrayList;
import java.util.List;

public class LayoutNode extends VisualNode {

	private final TextNode tag;
	private final List<LayoutNode> layoutChildren = new ArrayList<LayoutNode>();

	public boolean skip;

	public LayoutNode(TextNode tag) {
		this.tag = tag;
		add(tag);
	}

	public TextNode tag() {
		return tag;
	}

	public Iterable<LayoutNode> layoutChildren() {
		return layoutChildren;
	}

	public boolean hasLayoutChildren() {
		return layoutChildren.size() != 0;
	}

	public void add(LayoutNode node) {
		super.add(node);
		layoutChildren.add(node);
	}
}