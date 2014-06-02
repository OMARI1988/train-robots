/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Relation extends Terminal {

	private final Relations relation;

	public Relation(Relations relation) {
		super(null);
		this.relation = relation;
	}

	public Relation(TokenContext context, Relations relation) {
		super(context);
		this.relation = relation;
	}

	public Relations relation() {
		return relation;
	}

	@Override
	public boolean equals(Losr losr) {
		return losr instanceof Relation && ((Relation) losr).relation == relation;
	}

	@Override
	protected void writeName(StringBuilder text) {
		text.append("relation");
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(relation.toString().toLowerCase());
	}
}