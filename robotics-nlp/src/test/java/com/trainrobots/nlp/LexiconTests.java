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

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.TestContext;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.TextContext;
import com.trainrobots.nlp.lexicon.LexicalKey;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.treebank.Command;

public class LexiconTests {

	@Test
	@Ignore
	public void shouldDisplayMeasures() {
		FrequencyTable table = new FrequencyTable();
		for (Command command : TestContext.treebank().commands()) {
			if (command.losr() != null) {
				command.losr().visit(
						x -> {
							if (x instanceof SpatialRelation) {
								SpatialRelation sp = (SpatialRelation) x;
								if (sp.measure() != null) {
									TextContext span1 = sp.measure().span();
									TextContext span2 = sp.span();
									if (span1.start() != span2.start()) {
										String key = LexicalKey.key(
												command.tokens(), span2);
										table.add(key);
										System.out.println(command.id());
									}
								}
							}
						});
			}
		}
		for (FrequencyTable.Entry entry : table) {
			System.out.println(entry);
		}
	}

	@Test
	public void shouldGetTerminals() {

		// Lexicon.
		Lexicon lexicon = new Lexicon(TestContext.treebank());

		// Relation.
		Relation relation = lexicon.terminal(Relation.class, "on top of", null);
		assertThat(relation, is(new Relation(Relations.Above)));

		// Color.
		Color color = lexicon.terminal(Color.class, "pink", null);
		assertThat(color, is(new Color(Colors.Magenta)));

		// Lookup without specifiying type.
		Color color2 = lexicon.terminal(null, "purple", null);
		assertThat(color2, is(new Color(Colors.Magenta)));
	}
}