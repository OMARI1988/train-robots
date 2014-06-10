/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.losr;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Path;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.treebank.Command;

public class PartialTree {

	private final Command command;
	private final Items<Terminal> tokens;
	private final ItemsList<Losr> items = new ItemsList<Losr>();

	public PartialTree(Command command) {
		this.command = command;
		this.tokens = command.tokens();
		if (command.losr() != null) {
			items.add(command.losr());
		}
	}

	public PartialTree(Items<Terminal> tokens) {
		this.command = null;
		this.tokens = tokens;
	}

	public Command command() {
		return command;
	}

	public Items<Terminal> tokens() {
		return tokens;
	}

	public Items<Losr> items() {
		return items;
	}

	public void add(Losr item) {

		// Add ellipsis?
		int size = items.count();
		Integer after = after(item);
		if (after != null) {

			// Preserve sort order.
			TextContext previousSpan = null;
			for (int i = 0; i < size; i++) {
				Losr nextItem = items.get(i);
				TextContext nextSpan = nextItem.span();
				if ((previousSpan == null || after >= previousSpan.end())
						&& after < nextSpan.start()) {
					items.add(i, item);
					commit();
					return;
				}
				previousSpan = nextSpan;
			}

			// Add to end?
			if (previousSpan == null || after >= previousSpan.end()) {
				items.add(item);
				commit();
				return;
			}
			throw new RoboticException("Can't add ellipsis.");
		}

		// Non-terminal?
		if (!(item instanceof Terminal)) {
			for (Losr child : item) {
				removeTopLevelItem(child, false);
			}
		}

		// Preserve sort order.
		TextContext newSpan = item.span();
		TextContext previousSpan = null;
		size = items.count();
		for (int i = 0; i < size; i++) {

			// Next is an elliptical node.
			Losr nextItem = items.get(i);
			after = after(nextItem);
			if (after != null) {
				if (newSpan.start() == after) {
					items.add(i, item);
					commit();
					return;
				}
				continue;
			}

			// Next is not an elliptical node.
			TextContext nextSpan = nextItem.span();
			if ((previousSpan == null || newSpan.start() > previousSpan.end())
					&& newSpan.end() < nextSpan.start()) {
				items.add(i, item);
				commit();
				return;
			}
			previousSpan = nextSpan;
		}

		// Add to end?
		if (previousSpan == null || newSpan.start() > previousSpan.end()) {
			items.add(item);
			commit();
			return;
		}

		// Failed.
		throw new RoboticException("Can't add overlapping items.");
	}

	public void remove(Losr item) {

		// Find path.
		int size = items.count();
		for (int i = 0; i < size; i++) {
			Losr existing = items.get(i);
			Path path = existing.path(item);
			if (path != null) {

				// Remove path.
				for (Losr element : path) {
					removeTopLevelItem(element, true);
				}
				commit();
				return;
			}
		}
	}

	private void removeTopLevelItem(Losr item, boolean replaceWithChildren) {
		int size = items.count();
		for (int i = 0; i < size; i++) {

			// Match?
			Losr existing = items.get(i);
			if (existing == item) {

				// Remove.
				items.remove(i);

				// Add children.
				if (replaceWithChildren) {
					for (Losr child : item) {
						items.add(i++, child);
					}
				}
				return;
			}
		}
	}

	private static Integer after(Losr item) {
		if (item instanceof Ellipsis) {
			return ((Ellipsis) item).after();
		} else if (item instanceof Terminal) {
			Terminal terminal = (Terminal) item;
			if (terminal.context() instanceof EllipticalContext) {
				return ((EllipticalContext) terminal.context()).after();
			}
		}
		return null;
	}

	private void commit() {

		// Update command.
		if (items.size() == 1 && command != null) {
			command.losr(items.get(0));
		}
	}
}