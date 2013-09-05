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

package com.trainrobots.nlp.trees;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.nlp.commands.Command;
import com.trainrobots.nlp.commands.Corpus;
import com.trainrobots.nlp.io.FileReader;
import com.trainrobots.nlp.io.FileWriter;
import com.trainrobots.nlp.semantics.Translator;

public class TranslatorTests {

	@Test
	@Ignore
	public void shouldTranslateCorpus() {

		// Files.
		FileReader reader = new FileReader("../data/parses.txt");
		FileWriter writer = new FileWriter("c:/temp/csr.txt");

		// Process.
		String line;
		int i = 0;
		while ((line = reader.readLine()) != null) {

			// Command.
			Command command = Corpus.getCommands().get(i++);
			writer.writeLine("// Scene " + command.sceneNumber + ": "
					+ command.text);
			writer.writeLine();

			// Translation.
			Node node = Node.fromString(line);
			writer.writeLine(Translator.translate(node).format());
			writer.writeLine();
			writer.writeLine();
		}

		// Close.
		reader.close();
		writer.close();
	}
}