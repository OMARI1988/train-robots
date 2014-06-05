/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.observables;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.losr.Types;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class Observables {

	private final Layout layout;
	private ItemsList<Stack> stacks;

	public Observables(Layout layout) {
		this.layout = layout;
	}

	public Items<Stack> stacks() {
		if (stacks == null) {
			stacks = new ItemsList<Stack>();
			for (Shape shape : layout.shapes()) {
				if (shape.type() == Types.Cube && shape.position().z() == 0) {
					Stack stack = stack(shape);
					if (stack != null) {
						stacks.add(stack);
						Stack headlessStack = stack.excludeHead();
						if (headlessStack != null) {
							stacks.add(headlessStack);
						}
					}
				}
			}
		}
		return stacks;
	}

	private Stack stack(Shape base) {

		// Look up.
		Stack stack = new Stack(true);
		stack.add(base);
		int x = base.position().x();
		int y = base.position().y();
		for (int z = 1; z <= 8; z++) {

			// Empty?
			Shape shape = layout.shape(new Position(x, y, z));
			if (shape == null) {

				// A stack must be at least two blocks high.
				if (z == 1) {
					return null;
				} else {
					return stack;
				}
			}

			// Add.
			else {
				stack.add(shape);
			}
		}
		return null;
	}
}