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

package com.trainrobots.web.rcl;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;

public class RclTableTests {

	@Test
	@Ignore
	public void shouldBuildRclTable() {
		for (Command command : Corpus.getCommands()) {
			if (command.rcl == null) {
				continue;
			}
			RclTable table = new RclTable(command);
			System.out.println();
			for (RclLine line : table.lines()) {
				System.out.print("[" + line + "]");
				if (line.tokens != null) {
					System.out.print(" // " + line.tokens);
				}
				System.out.println();
			}
		}
	}
}