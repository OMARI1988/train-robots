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

package com.trainrobots.nlp.chunker;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.CardinalAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.OrdinalAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.core.rcl.TypeAttribute;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class GoldSequence {

	private final Command command;
	private final List<Token> tokens = new ArrayList<Token>();

	public GoldSequence(Command command) {

		this.command = command;

		for (Node node : Tokenizer.getTokens(command.text).children) {
			tokens.add(new Token(node.getValue(), "O"));
		}

		command.rcl.recurse(new RclVisitor() {

			public void visit(Rcl parent, ActionAttribute attribute) {
				write(attribute, "ACT");
			}

			public void visit(Rcl parent, ColorAttribute attribute) {
				write(attribute, "COLOR");
			}

			public void visit(Rcl parent, IndicatorAttribute attribute) {
				write(attribute, parent instanceof Entity ? "IND" : "REL");
			}

			public void visit(Rcl parent, TypeAttribute attribute) {
				write(attribute, "TYPE");
			}

			public void visit(Rcl parent, OrdinalAttribute attribute) {
				write(attribute, "ORD");
			}

			public void visit(Rcl parent, CardinalAttribute attribute) {
				write(attribute, "CARD");
			}

			private void write(Rcl rcl, String tag) {

				int start = rcl.tokenStart();
				int end = rcl.tokenEnd();
				if (start == 0) {
					return;
				}
				for (int i = start; i <= end; i++) {
					tokens.get(i - 1).tag = i == start ? "B-" + tag : "I-"
							+ tag;
				}
			}
		});
	}

	public Command command() {
		return command;
	}

	public List<Token> tokens() {
		return tokens;
	}
}