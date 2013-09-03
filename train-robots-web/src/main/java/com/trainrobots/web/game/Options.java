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

package com.trainrobots.web.game;

public class Options {

	private static final String[] OPTIONS = new String[] {
			"<span class='negative'>Bad command</span> - Inappropriate words (e.g. 'north'), or spam.",
			"<span class='negative'>Bad command</span> - Serious spelling or grammar mistakes or too many missing words.",
			"<span class='negative'>Bad command</span> - Before and after images were confused (wrong way around).",
			"<span class='negative'>Bad command</span> - Move was described incorrectly (bad directions or not using robot's viewpoint).",
			"<span class='negative'>Bad command</span> - Command was not specific enough and could be misinterpreted.",
			"<span class='positive'>Good command</span> - Accurately describes the move (although may not be a linguistic masterpiece)." };

	private Options() {
	}

	public static String get(int number) {
		return OPTIONS[number - 1];
	}
}