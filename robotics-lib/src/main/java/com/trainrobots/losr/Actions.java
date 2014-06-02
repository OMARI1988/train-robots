/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr;

import java.util.TreeMap;

public enum Actions {

	Move, Take, Drop;

	private static final TreeMap<String, Actions> actions = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);

	public static Actions parse(String name) {
		Actions action = actions.get(name);
		if (action == null) {
			throw new IllegalArgumentException(String.format(
					"The action '%s' is not recognized.", name));
		}
		return action;
	}

	static {
		for (Actions action : values()) {
			actions.put(action.toString(), action);
		}
	}
}