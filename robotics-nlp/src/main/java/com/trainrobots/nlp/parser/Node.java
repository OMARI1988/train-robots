/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.parser;

import com.trainrobots.losr.Losr;

public class Node {

	private final Losr losr;
	private final Node[] children;
	private Double weight;

	public Node(Losr losr) {
		this(losr, null);
	}

	public Node(Losr losr, Node[] children) {
		this.losr = losr;
		this.children = children;
	}

	public Losr losr() {
		return losr;
	}

	public Node[] children() {
		return children;
	}

	public Double weight() {
		return weight;
	}

	public void weight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return losr.toString();
	}
}