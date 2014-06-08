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

public class Type extends Terminal {

	private final Types type;

	public Type(Types type) {
		this(null, type);
	}

	public Type(TokenContext context, Types type) {
		super(context);
		this.type = type;
	}

	public Types type() {
		return type;
	}

	@Override
	public String name() {
		return "type";
	}

	@Override
	public Items<String> detail() {
		return new SingleItem(content());
	}

	@Override
	public boolean equals(Losr losr) {
		if (losr instanceof Type) {
			Type type = (Type) losr;
			return type.id == id && type.referenceId == referenceId
					&& type.type == this.type;
		}
		return false;
	}

	@Override
	protected void writeContent(StringBuilder text) {
		text.append(content());
	}

	private String content() {
		switch (type) {
		case TypeReference:
			return "type-reference";
		case TypeReferenceGroup:
			return "type-reference-group";
		case CubeGroup:
			return "cube-group";
		}
		return type.toString().toLowerCase();
	}
}