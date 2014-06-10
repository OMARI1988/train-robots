/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

public class Relation extends Terminal {

	private Relations relation;

	public Relation(Relations relation) {
		super(null);
		this.relation = relation;
	}

	public Relation(TextContext context, Relations relation) {
		super(context);
		this.relation = relation;
	}

	public Relations relation() {
		return relation;
	}

	public void relation(Relations relation) {
		this.relation = relation;
	}

	@Override
	public String name() {
		return "relation";
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Relation) {
			Relation relation = (Relation) losr;
			return relation.id == id && relation.referenceId == referenceId
					&& relation.relation == this.relation;
		}
		return false;
	}

	@Override
	public Relation withContext(TextContext context) {
		return new Relation(context, relation);
	}

	@Override
	protected Object content() {
		return relation;
	}
}