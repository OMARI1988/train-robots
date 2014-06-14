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

import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.TestContext;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.nlp.grammar.Grammar;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.nlp.tagger.Tagger;
import com.trainrobots.planner.Planner;
import com.trainrobots.scenes.Layout;
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
	public void shouldParseCommand() {
		assertTrue(parse(4073, true));
	}

	@Test
	@Ignore
	public void shouldParseNewCommands() {

		Set<Integer> ignoreList = new HashSet<>();
		ignoreList.add(99);
		ignoreList.add(3111);
		ignoreList.add(4211);
		ignoreList.add(8460);
		ignoreList.add(9305);
		ignoreList.add(21444);
		ignoreList.add(23568);
		ignoreList.add(24528);

		for (Command command : TestContext.treebank().commands()) {
			if (!ignoreList.contains(command.id()) && command.losr() == null) {
				try {

					// Parse.
					Layout layout = command.scene().before();
					Items<Terminal> terminals = tagger.terminals(command);
					Parser parser = new Parser(layout, grammar, lexicon,
							(Items) terminals, command.tokens(), false);
					Losr losr = parser.parse(false);

					// Validate.
					Planner planner = new Planner(layout);
					if (!planner.instruction(losr).equals(
							command.scene().instruction())) {
						continue;
					}
				} catch (Exception exception) {
					continue;
				}
				System.out.println(command.id());
			}
		}
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
		assertThat(valid, is(3714));
		assertThat(total, is(3945));
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