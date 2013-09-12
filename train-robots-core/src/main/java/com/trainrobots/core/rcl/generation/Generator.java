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

import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.Color;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.SpatialRelation;
import com.trainrobots.core.rcl.Type;

public class Generator {

	private final StringBuilder text = new StringBuilder();

	@Override
	public String toString() {
		return text.toString();
	}

	public void generate(Entity entity) {

		// Determiner.
		write("the");

		// Indicators.
		if (entity.indicators() != null) {
			for (SpatialIndicator indicator : entity.indicators()) {
				write(indicator);
			}
		}

		// Colors.
		if (entity.colors() != null) {
			for (Color color : entity.colors()) {
				write(color);
			}
		}

		// Type.
		write(entity.type());
	}

	public void generate(Event event) {
		write(event.action());
		generate(event.entity());
		if (event.destinations() != null) {
			for (SpatialRelation destination : event.destinations()) {
				generate(destination);
			}
		}
	}

	public void generate(SpatialRelation relation) {
		write(relation.indicator());
		generate(relation.entity());
	}

	private void write(Action action) {
		write(action.toString());
	}

	private void write(Color color) {
		write(color.toString());
	}

	private void write(Type type) {
		write(type.toString());
	}

	private void write(SpatialIndicator indicator) {
		write(indicator.toString());
	}

	private void write(String text) {
		if (this.text.length() > 0) {
			this.text.append(' ');
		}
		this.text.append(text);
	}
}