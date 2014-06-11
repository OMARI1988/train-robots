/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.grammar;

public class EllipsisRule {

	private final String before;
	private final String tag;
	private final String after;

	public EllipsisRule(String before, String tag, String after) {
		this.before = before;
		this.tag = tag;
		this.after = after;
	}

	public String before() {
		return before;
	}

	public String tag() {
		return tag;
	}

	public String after() {
		return after;
	}
}