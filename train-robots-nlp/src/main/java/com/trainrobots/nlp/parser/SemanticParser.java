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

package com.trainrobots.nlp.parser;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.Action;
import com.trainrobots.core.rcl.Entity;
import com.trainrobots.core.rcl.Event;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.Sequence;
import com.trainrobots.core.rcl.Type;
import com.trainrobots.nlp.parser.grammar.Grammar;
import com.trainrobots.nlp.parser.lexicon.Lexicon;
import com.trainrobots.nlp.processor.Planner;
import com.trainrobots.nlp.scenes.WorldModel;

public class SemanticParser {

	private final Parser parser;
	private final Planner planner;
	// private final WorldModel world;
	private final boolean verbose;

	public SemanticParser(WorldModel world, Grammar grammar, Lexicon lexicon,
			List<Node> items, List<Node> tokens) {
		this(world, grammar, lexicon, items, tokens, false);
	}

	public SemanticParser(WorldModel world, Grammar grammar, Lexicon lexicon,
			List<Node> items, List<Node> tokens, boolean verbose) {
		this.parser = new Parser(grammar, lexicon, items, tokens);
		this.planner = new Planner(world);
		// this.world = world;
		this.verbose = verbose;
	}

	public Rcl parse() {

		// Parse.
		List<Node> trees = parser.parse();
		if (trees.size() == 0) {
			if (verbose) {
				System.out.println("No parse trees.");
			}
			return null;
		}

		// Map.
		List<Rcl> results = new ArrayList<Rcl>();
		for (Node tree : trees) {
			Rcl rcl;
			try {
				rcl = Rcl.fromNode(tree);
			} catch (Exception exception) {
				if (verbose) {
					System.out.println("Failed to create RCL: " + tree);
					exception.printStackTrace(System.out);
				}
				continue;
			}
			mapReferences(rcl);
			results.add(rcl);
		}

		// Validate.
		List<Rcl> valid = new ArrayList<Rcl>();
		for (Rcl rcl : results) {
			if (valid(rcl)) {
				valid.add(rcl);
			}
		}
		if (valid.size() == 0) {
			if (verbose) {
				for (Rcl rcl : results) {
					System.out.println("Not valid: " + rcl.format());
				}
			}
			return null;
		}
		if (valid.size() == 1) {
			return valid.get(0);
		}

		// Duplicates.
		if (verbose) {
			for (Rcl rcl : valid) {
				System.out.println("Validated duplicate: " + rcl.format());
			}
		}
		return null;
	}

	private boolean valid(Rcl rcl) {
		try {
			planner.getMoves(rcl);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

	private void mapReferences(Rcl rcl) {

		if (rcl instanceof Sequence) {
			Sequence sequence = (Sequence) rcl;
			if (sequence.events().size() == 2) {
				Event event1 = sequence.events().get(0);
				Event event2 = sequence.events().get(1);
				event1.actionAttribute().setAction(Action.take);
				event2.actionAttribute().setAction(Action.drop);

				Entity entity1 = event1.entity();
				Entity entity2 = event2.entity();

				if (entity2.isType(Type.reference)) {
					entity1.setId(1);
					entity2.setReferenceId(1);
				}
			}
		}
	}
}