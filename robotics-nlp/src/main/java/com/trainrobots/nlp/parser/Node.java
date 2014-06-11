/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.parser;

import com.trainrobots.collections.ItemsArray;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.factory.LosrFactory;

public class Node {

	private final Losr losr;
	private final Node[] children;
	private Double weight;

	public Node(Terminal terminal) {
		this.losr = terminal;
		this.children = null;
	}

	public Node(String tag, Node[] children) {
		Losr[] items = new Losr[children.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = children[i].losr;
		}
		losr = LosrFactory.build(0, 0, tag, new ItemsArray(items));
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