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

package com.trainrobots.core.rcl.generation;

import com.trainrobots.core.rcl.Color;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Type;

public class Generator {

	private final StringBuilder text = new StringBuilder();

	@Override
	public String toString() {
		return text.toString();
	}

	public void generate(Entity entity) {
		if (entity.colors() != null) {
			for (Color color : entity.colors()) {
				write(color);
			}
		}
		write(entity.type());
	}

	private void write(Color color) {
		write(color.toString().toLowerCase());
	}

	private void write(Type type) {
		write(type.toString().toLowerCase());
	}

	private void write(String text) {
		if (this.text.length() > 0) {
			this.text.append(' ');
		}
		this.text.append(text);
	}
}