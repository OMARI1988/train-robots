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

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.io.FileWriter;

public class CorpusTests {

	@Test
	public void shouldLoadCorpus() {
		assertEquals(8970, Corpus.getCommands().size());
	}

	@Test
	@Ignore
	public void shouldExportCorpus() {

		FileWriter writer = new FileWriter("c:/temp/corpus.txt");

		writer.writeLine("// This file is part of Train Robots (www.TrainRobots.com).");
		writer.writeLine("// Copyright (C) Kais Dukes, 2013. Contact: kais@kaisdukes.com.");
		writer.writeLine("// Published: 19 Sep 2013.");
		writer.writeLine("//");
		writer.writeLine("// This file has five lines per command:");
		writer.writeLine("//");
		writer.writeLine("// Line 1: A numeric command ID.");
		writer.writeLine("// Line 2: A numeric scene ID (from scenes.txt).");
		writer.writeLine("// Line 3: The natural language text.");
		writer.writeLine("// Line 4: The annotated robot control language.");
		writer.writeLine("// Line 5: A blank separating line.");
		int count = 0;
		for (Command command : Corpus.getCommands()) {
			if (command.rcl != null) {
				writer.writeLine();
				writer.writeLine(command.id);
				writer.writeLine(command.sceneNumber);
				writer.writeLine(command.text);
				writer.writeLine(command.rcl.toString());
				count++;
			}
		}
		writer.close();
		System.out.println("Wrote: " + count + " commands");
	}
}