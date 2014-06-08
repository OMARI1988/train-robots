/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.treebank.Command;

public class PartialTree {

	private final Items<Terminal> tokens;
	private final ItemsList<Losr> items = new ItemsList<Losr>();

	public PartialTree(Command command) {
		this(command.tokens());
		if (command.losr() != null) {
			add(command.losr());
		}
	}

	public PartialTree(Items<Terminal> tokens) {
		this.tokens = tokens;
	}

	public Items<Terminal> tokens() {
		return tokens;
	}

	public Items<Losr> items() {
		return items;
	}

	public void add(Losr item) {

		// First item?
		int size = items.count();
		if (size == 0) {
			items.add(item);
			return;
		}

		// Preserve sort order.
		TextContext newSpan = item.span();
		TextContext previousSpan = null;
		for (int i = 0; i < size; i++) {
			TextContext nextSpan = items.get(i).span();
			if ((previousSpan == null || newSpan.start() > previousSpan.end())
					&& newSpan.end() < nextSpan.start()) {
				items.add(i, item);
				return;
			}
			previousSpan = nextSpan;
		}

		// Add to end?
		if (newSpan.start() > previousSpan.end()) {
			items.add(item);
			return;
		}

		// Failed.
		throw new RoboticException("Can't add overlapping items.");
	}
}