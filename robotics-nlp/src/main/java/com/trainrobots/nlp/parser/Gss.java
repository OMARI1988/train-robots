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

public class Gss {

	private final List<GssVertex> vertices = new ArrayList<GssVertex>();

	public List<GssVertex> vertices() {
		return vertices;
	}

	public GssVertex add(Node node) {
		GssVertex vertex = new GssVertex(vertices.size() + 1, node);
		vertices.add(vertex);
		return vertex;
	}
}