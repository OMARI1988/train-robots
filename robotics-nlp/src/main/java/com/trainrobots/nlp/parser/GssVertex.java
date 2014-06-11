/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.parser;

import java.util.ArrayList;
import java.util.List;

public class GssVertex {

	private final int id;
	private final Node node;
	private final List<GssVertex> parents = new ArrayList<GssVertex>();

	public GssVertex(int id, Node node) {
		this.id = id;
		this.node = node;
	}

	public int id() {
		return id;
	}

	public Node node() {
		return node;
	}

	public List<GssVertex> parents() {
		return parents;
	}

	@Override
	public String toString() {
		return node.toString();
	}
}