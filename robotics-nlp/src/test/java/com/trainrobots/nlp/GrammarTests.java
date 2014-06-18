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

import org.junit.Test;

import com.trainrobots.Context;
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.nlp.grammar.Grammar;

public class GrammarTests {

	@Test
	public void shouldGetNonTerminals() {

		// Grammar.
		Grammar grammar = new Grammar(Context.treebank());

		// Entity.
		Entity entity = (Entity) grammar.nonTerminal(new ItemsArray(new Color(
				Colors.Blue), new Type(Types.Cube)));

		// Verify.
		assertThat(entity, is(new Entity(Colors.Blue, Types.Cube)));
	}
}