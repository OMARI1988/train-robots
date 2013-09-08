/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.nlp.scenes;

import java.util.ArrayList;
import java.util.List;

public class Stack {

	private final List<Shape> shapes = new ArrayList<Shape>();

	public void add(Shape shape) {
		shapes.add(shape);
	}

	public boolean allHaveColor(Color color) {
		for (Shape shape : shapes) {
			if (shape.color != color) {
				return false;
			}
		}
		return true;
	}

	public Shape top() {
		return shapes.get(shapes.size() - 1);
	}
}