/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

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
		switch (type) {
		case TypeReference:
			text.append("type-reference");
			break;
		case TypeReferenceGroup:
			text.append("type-reference-group");
			break;
		case CubeGroup:
			text.append("cube-group");
			break;
		default:
			text.append(type.toString().toLowerCase());
			break;
		}
	}
}