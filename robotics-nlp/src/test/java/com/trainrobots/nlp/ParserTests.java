/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.TestContext;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Marker;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.nlp.grammar.Grammar;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.nlp.losr.PartialTree;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.tagger.Tagger;
import com.trainrobots.treebank.Command;
import com.trainrobots.treebank.Treebank;

public class ParserTests {

	private final Lexicon lexicon;
	private final Grammar grammar;
	private final Tagger tagger;

	public ParserTests() {
		Treebank treebank = TestContext.treebank();
		lexicon = new Lexicon(treebank);
		grammar = new Grammar(treebank);
		tagger = new Tagger(treebank, lexicon);
	}

	@Test
	@Ignore
	public void shouldParsePartialStructure() {

		// Tree.
		Command command = TestContext.treebank().command(8703);
		Losr losr = command.losr();
		PartialTree partialTree = new PartialTree(command);

		// Partial structure.
		partialTree.remove(losr.find(1));
		partialTree.add(new Marker(new TextContext(6)));
		assertThat(partialTree.items().count(), is(6));

		// Parse.
		Parser parser = new Parser(command.scene().before(), grammar,
				partialTree.items());
		Losr result = parser.parse();

		// Final result.
		System.out.println("FINAL RESULT = " + result);
	}

	@Test
	@Ignore
	public void shouldParseCommand() {
		assertTrue(parse(1892, true));
	}

	@Test
	@Ignore
	public void shouldParseTreebank() {

		// Parse.
		int valid = 0;
		int total = 0;
		for (Command command : TestContext.treebank().commands()) {
			if (command.losr() != null) {
				if (parse(command.id())) {
					valid++;
				}
				total++;
			}
		}

		// Diagnostics.
		System.out.println(String.format("Parsed: %d / %d = %.2f %%", valid,
				total, 100.0 * valid / total));
		assertThat(valid, is(3510));
		assertThat(total, is(3698));
	}

	private boolean parse(int id) {
		return parse(id, false);
	}

	private boolean parse(int id, boolean verbose) {

		// Command.
		Command command = TestContext.treebank().command(id);

		// Parse.
		Losr losr;
		try {
			Items<Terminal> terminals = tagger.terminals(command);
			Parser parser = new Parser(command.scene().before(), grammar,
					lexicon, (Items) terminals, command.tokens(), verbose);
			losr = parser.parse();
		} catch (Exception exception) {
			System.out.println(command.id() + ": " + exception.getMessage());
			return false;
		}

		// Match?
		if (losr.equals(command.losr())) {
			return true;
		}

		// No match.
		if (verbose) {
			System.out.println();
			System.out.println("Command  : " + command.text());
			System.out.println("Expected : " + command.losr());
			System.out.println("Parsed   : " + losr);
		}
		System.out.println(command.id() + ": Single tree mismatched.");
		return false;
	}
}