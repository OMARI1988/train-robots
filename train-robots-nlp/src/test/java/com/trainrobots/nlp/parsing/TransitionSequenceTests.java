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
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.nlp.chunker.Chunker;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.parser.TokenizedTree;

public class TransitionSequenceTests {

	@Test
	public void shouldExecuteTransitionSequence1() {

		validate(84, new TransitionSequence() {
			public void parse(Parser p) {
				p.shift();
				p.shift();
				p.shift();
				p.reduce(2, "entity:");
				p.reduce(2, "event:");
			}
		});
	}

	@Test
	public void shouldExecuteTransitionSequence2() {

		validate(14055, new TransitionSequence() {
			public void parse(Parser p) {
				p.shift();
				p.shift();
				p.shift();
				p.reduce(2, "entity:");
				p.shift();
				p.shift();
				p.shift();
				p.shift();
				p.reduce(3, "entity:");
				p.reduce(2, "spatial-relation:");
				p.reduce(1, "destination:");
				p.reduce(3, "event:");
			}
		});
	}

	private static void validate(int id, TransitionSequence sequence) {

		// Command.
		Command command = Corpus.getCommand(id);

		// Parse.
		List<Node> chunks = Chunker.getChunks(command.rcl);
		Parser parser = new Parser(chunks);
		sequence.parse(parser);
		Node result = parser.result();

		// Validate.
		Rcl expected = TokenizedTree.getTree(command.rcl);
		assertEquals(result, expected.toNode());
	}

	private static interface TransitionSequence {
		void parse(Parser p);
	}
}