/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.SingleItem;

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
	public String name() {
		return "relation";
	}

	@Override
	public Items<String> detail() {
		return new SingleItem(content());
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
	protected void writeContent(StringBuilder text) {
		text.append(content());
	}

	private String content() {
		return relation.toString().toLowerCase();
	}
}