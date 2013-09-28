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

package com.trainrobots.nlp.parsing;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclType;
import com.trainrobots.nlp.chunker.Chunker;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.parser.TokenizedTree;

public class ParserTests {

	@Test
	public void shouldParse1() {

		// Command.
		Command command = Corpus.getCommand(84);

		// Parse.
		List<Rcl> chunks = Chunker.getChunks(command.rcl);
		Parser parser = new Parser(chunks);
		parser.shift();
		parser.shift();
		parser.shift();
		parser.reduce(2, RclType.Entity);
		parser.reduce(2, RclType.Event);
		Rcl rcl = parser.rcl();

		// Validate.
		Rcl expected = TokenizedTree.getTree(command.rcl);
		assertEquals(rcl.toNode(), expected.toNode());
	}
}