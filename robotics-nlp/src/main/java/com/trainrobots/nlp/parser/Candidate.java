/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.parser;

import com.trainrobots.losr.Losr;

public class Candidate {

	private final Losr losr;
	private double weight = 1;

	public Candidate(Node node) {
		losr = node.losr();
		normalize(node);
	}

	public Losr losr() {
		return losr;
	}

	public double weight() {
		return weight;
	}

	private void normalize(Node node) {
		if (node.weight() != null) {
			weight *= node.weight();
		}
		if (node.children() != null) {
			for (Node child : node.children()) {
				normalize(child);
			}
		}
	}
}