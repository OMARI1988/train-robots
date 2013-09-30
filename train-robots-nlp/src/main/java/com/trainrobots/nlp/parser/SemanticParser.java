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
		this.planner = new Planner(world);
		this.parser = new Parser(planner.grounder(), grammar, lexicon, items,
				tokens);
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
		List<Candidate> candidates = new ArrayList<Candidate>();
		for (Node tree : trees) {
			Candidate candidate;
			try {
				candidate = new Candidate(tree);
			} catch (Exception exception) {
				if (verbose) {
					System.out.println("Failed to create RCL: " + tree);
					exception.printStackTrace(System.out);
				}
				continue;
			}
			mapReferences(candidate.rcl);
			candidates.add(candidate);
		}

		// Validate.
		List<Candidate> valid = new ArrayList<Candidate>();
		for (Candidate candidate : candidates) {
			if (valid(candidate.rcl)) {
				valid.add(candidate);
			}
		}
		if (valid.size() == 0) {
			if (verbose) {
				for (Candidate candidate : candidates) {
					System.out.println("Not valid: " + candidate.rcl);
				}
			}
			return null;
		}
		if (valid.size() == 1) {
			return valid.get(0).rcl;
		}

		// Rank.
		Candidate best = null;
		boolean duplicate = false;
		for (Candidate candidate : valid) {
			if (best == null || candidate.p > best.p) {
				best = candidate;
			} else if (candidate.p == best.p) {
				duplicate = true;
			}
		}

		// Ranked?
		if (!duplicate && best != null) {
			return best.rcl;
		}

		// Duplicates?
		if (verbose) {
			for (Candidate candidate : valid) {
				System.out.println("Validated duplicate (P=" + candidate.p
						+ "): " + candidate.rcl);
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