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

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.nlp.parser.grammar.EllipsisRule;
import com.trainrobots.nlp.parser.grammar.Grammar;
import com.trainrobots.nlp.parser.grammar.ProductionRule;

public class GrammarTests {

	@Test
	@Ignore
	public void shouldDeriveGrammar() {

		Grammar grammar = Grammar.goldGrammar();

		for (ProductionRule rule : grammar.productionRules()) {
			System.out.println("Production: " + rule + "\t" + rule.count);
		}

		for (EllipsisRule rule : grammar.ellipsisRules()) {
			System.out.println("Ellipsis: " + rule + "\t" + rule.count);
		}
	}
}