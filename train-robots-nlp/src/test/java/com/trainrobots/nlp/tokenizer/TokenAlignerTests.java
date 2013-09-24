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

package com.trainrobots.nlp.tokenizer;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.CardinalAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.OrdinalAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.core.rcl.TypeAttribute;

public class TokenAlignerTests {

	@Test
	@Ignore
	public void shouldAlignCorpus() {
		for (Command command : Corpus.getCommands()) {
			if (command.rcl == null) {
				continue;
			}
			Rcl rcl = command.rcl.clone();
			final List<String> tokens = TokenAligner.align(rcl, command.text);
			System.out.println();
			System.out.println("C" + command.id);
			System.out.println();

			rcl.accept(new RclVisitor() {
				public void visit(ActionAttribute attribute) {
					write(attribute);
				}

				public void visit(ColorAttribute attribute) {
					write(attribute);
				}

				public void visit(IndicatorAttribute attribute) {
					write(attribute);
				}

				public void visit(TypeAttribute attribute) {
					write(attribute);
				}

				public void visit(OrdinalAttribute attribute) {
					write(attribute);
				}

				public void visit(CardinalAttribute attribute) {
					write(attribute);
				}

				private void write(Rcl rcl) {
					System.out.print(rcl);
					int start = rcl.tokenStart();
					int end = rcl.tokenEnd();
					if (start == 0) {
						System.out.println();
						return;
					}
					System.out.print(" --> [");
					for (int i = start; i <= end; i++) {
						if (i > start) {
							System.out.print(" ");
						}
						System.out.print(tokens.get(i - 1));
					}
					System.out.println("]");
				}
			});
		}
	}
}