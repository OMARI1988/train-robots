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

package com.trainrobots.nlp.scenes.moves;

import com.trainrobots.nlp.scenes.Position;

public class TakeMove implements Move {

	public TakeMove(Position from) {
		this.from = from;
	}

	public final Position from;

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TakeMove)) {
			return false;
		}
		TakeMove m = (TakeMove) object;
		return m.from.equals(from);
	}

	@Override
	public String toString() {
		StringBuilder text = new StringBuilder();
		text.append("Take: ");
		text.append(from);
		return text.toString();
	}
}