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

package com.trainrobots.core.corpus;

public class Enhancement {

	private Enhancement() {
	}

	private static String[] descriptions = { "No enhancement", "row/column",
			"lowest/highest/tallest/shortest", "near",
			"supplemantary cardinality", "complex measure", "between", "other",
			"complex stack description", "implict proximity", "opposite",
			"edge", "furthest", "exclude surface layer", "stack availability",
			"free/exposed/available", "only/unique", "multiple destinations",
			"existence (there is...)", "complex block group",
			"adjectives/compound noun phrase" };

	public static String[] getDesriptions() {
		return descriptions;
	}
}