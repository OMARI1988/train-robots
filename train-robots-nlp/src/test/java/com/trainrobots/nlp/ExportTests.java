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

package com.trainrobots.nlp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.io.FileWriter;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class ExportTests {

	@Test
	@Ignore
	public void shouldExportTreematicaFile() {

		FileWriter writer = new FileWriter(
				"c:/development/treematica/data/train-robots.tm");
		writer.writeLine("## treematica");
		writer.writeLine("## format\tRcl");
		int count = 0;
		for (Command command : Corpus.getCommands()) {
			if (command.rcl != null) {
				writer.writeLine();
				writer.writeLine("## id\t" + command.id);
				List<String> tokens = getTokens(command.text);
				for (int i = 0; i < tokens.size(); i++) {
					if (i > 0) {
						writer.write('\t');
					}
					writer.write(tokens.get(i));
				}
				writer.writeLine();
				writer.writeLine(command.rcl.toString());
				count++;
			}
		}
		writer.close();
		System.out.println("Wrote: " + count + " commands");
	}

	private List<String> getTokens(String text) {
		List<String> tokens = new ArrayList<String>();
		List<Node> nodes = Tokenizer.getTokens(text).children;
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			String token = node.getValue();
			if (token.equals(".")) {
				if (tokens.size() == 0 || i == nodes.size() - 1) {
					continue;
				}
			}
			tokens.add((i + 1) + ":" + token);
		}
		return tokens;
	}
}