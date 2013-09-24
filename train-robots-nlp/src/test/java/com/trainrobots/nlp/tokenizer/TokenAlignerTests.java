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
import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.CardinalAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.OrdinalAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.core.rcl.TypeAttribute;

public class TokenAlignerTests {

	private int count;

	@Test
	@Ignore
	public void shouldFindMisalignment() {
		for (Command command : Corpus.getCommands()) {
			if (command.rcl == null) {
				continue;
			}
			Rcl rcl = command.rcl;
			final List<Node> tokens = Tokenizer.getTokens(command.text).children;
			final int id = command.id;

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
					int start = rcl.tokenStart();
					int end = rcl.tokenEnd();
					if (start == 0) {
						return;
					}
					String keyword = rcl.toNode().getSingleLeaf();
					StringBuilder text = new StringBuilder();
					for (int i = start; i <= end; i++) {
						if (i > start) {
							text.append(" ");
						}
						text.append(tokens.get(i - 1).getValue());
					}
					write(keyword, text.toString());
				}

				private void write(String keyword, String text) {
					if (keyword.equals(text)) {
						return;
					}

					if (keyword.equals("above")) {
						if (text.equals("on top of") || text.equals("on")
								|| text.equals("placed on")
								|| text.equals("sitting on top of")
								|| text.equals("over") || text.equals("onto")
								|| text.equals("top of")
								|| text.equals("on the top of")
								|| text.equals("placed on top of")
								|| text.equals("sat on the top of")
								|| text.equals("located on top of")
								|| text.equals("atop") || text.equals("at")
								|| text.equals("on top") || text.equals("top")
								|| text.equals("on the top")
								|| text.equals("in top of")
								|| text.equals("over the top")
								|| text.equals("at the top of")
								|| text.equals("onto the top of")) {
							return;
						}
					}

					if (keyword.equals("left")) {
						if (text.equals("left of")
								|| text.equals("on the left side of")
								|| text.equals("on the left side on")
								|| text.equals("placed on the left side of")
								|| text.equals("left side")
								|| text.equals("on the left of")
								|| text.equals("to the left")
								|| text.equals("on the left")
								|| text.equals("in the left of")) {
							return;
						}
					}

					if (keyword.equals("right")) {
						if (text.equals("right of")
								|| text.equals("on the right side of")
								|| text.equals("on the right side on")
								|| text.equals("placed on the right side of")
								|| text.equals("right side")
								|| text.equals("on the right of")
								|| text.equals("to the right")
								|| text.equals("on the right")
								|| text.equals("in the right of")) {
							return;
						}
					}

					if (keyword.equals("leftmost")) {
						if (text.equals("left most")
								|| text.equals("most left")) {
							return;
						}
					}

					if (keyword.equals("rightmost")) {
						if (text.equals("right most")
								|| text.equals("most right")) {
							return;
						}
					}

					if (keyword.equals("nearest")) {
						if (text.equals("closest") || text.equals("closer")
								|| text.equals("near")
								|| text.equals("placed closest to")
								|| text.equals("closest to")) {
							return;
						}
					}

					if (keyword.equals("backward")) {
						if (text.equals("back") || text.equals("backwards")) {
							return;
						}
					}

					if (keyword.equals("within")) {
						if (text.equals("in") || text.equals("at")
								|| text.equals("into")
								|| text.equals("placed in")) {
							return;
						}
					}

					if (keyword.equals("board")) {
						if (text.equals("floor") || text.equals("ground")) {
							return;
						}
					}

					if (keyword.equals("edge")) {
						if (text.equals("border")) {
							return;
						}
					}

					if (keyword.equals("corner")) {
						if (text.equals("edge")) {
							return;
						}
					}

					if (keyword.equals("center")) {
						if (text.equals("centre")) {
							return;
						}
					}

					if (keyword.equals("adjacent")) {
						if (text.equals("next")
								|| text.equals("located next to")
								|| text.equals("beside")) {
							return;
						}
					}

					if (keyword.equals("take") || keyword.equals("move")
							|| keyword.equals("drop")) {
						if (text.equals("pick up") || text.equals("pick")
								|| text.equals("grab") || text.equals("move")
								|| text.equals("lift") || text.equals("remove")
								|| text.equals("hold") || text.equals("pickup")
								|| text.equals("place")
								|| text.equals("transfer")
								|| text.equals("put") || text.equals("take")
								|| text.equals("move") || text.equals("drop")
								|| text.equals("shift")
								|| text.equals("grab hold of")
								|| text.equals("lower")) {
							return;
						}
					}

					if (keyword.equals("prism")) {
						if (text.equals("pyramid")
								|| text.equals("tetrahedron")
								|| text.equals("triangle")
								|| text.equals("block") || text.equals("cube")) {
							return;
						}
					}

					if (keyword.equals("cube")) {
						if (text.equals("block") || text.equals("brick")
								|| text.equals("box") || text.equals("cub")
								|| text.equals("square")) {
							return;
						}
					}

					if (keyword.equals("reference")) {
						if (text.equals("it") || text.equals("in")
								|| text.equals("that") || text.equals("this")) {
							return;
						}
					}

					if (keyword.equals("type-reference")) {
						if (text.equals("one")) {
							return;
						}
					}

					if (keyword.equals("tile")) {
						if (text.equals("cell") || text.equals("cells")
								|| text.equals("square")
								|| text.equals("squares")
								|| text.equals("space")
								|| text.equals("spaces") || text.equals("step")
								|| text.equals("steps") || text.equals("place")
								|| text.equals("places")) {
							return;
						}
					}

					if (keyword.equals("gray")) {
						if (text.equals("grey") || text.equals("dark grey")) {
							return;
						}
					}

					if (keyword.equals("magenta")) {
						if (text.equals("pink") || text.equals("purple")) {
							return;
						}
					}

					if (keyword.equals("cyan")) {
						if (text.equals("turquoise") || text.equals("blue")
								|| text.equals("light blue")
								|| text.equals("sky blue")
								|| text.equals("blue - sky")
								|| text.equals("blue sky")) {
							return;
						}
					}

					if (keyword.equals("blue")) {
						if (text.equals("dark blue")) {
							return;
						}
					}

					if (keyword.equals("green")) {
						if (text.equals("greed")) {
							return;
						}
					}

					if (keyword.equals("white")) {
						if (text.equals("light grey") || text.equals("grey")) {
							return;
						}
					}

					if (keyword.equals("1")) {
						if (text.equals("one")) {
							return;
						}
					}

					if (keyword.equals("2")) {
						if (text.equals("two")) {
							return;
						}
					}

					if (keyword.equals("3")) {
						if (text.equals("three")) {
							return;
						}
					}

					if (keyword.equals("4")) {
						if (text.equals("four")) {
							return;
						}
					}

					if (keyword.equals("5")) {
						if (text.equals("five")) {
							return;
						}
					}

					if (keyword.equals("front")) {
						if (text.equals("top") || text.equals("far")
								|| text.equals("in front of")
								|| text.equals("on front of")
								|| text.equals("placed in front of")) {
							return;
						}
					}

					if (keyword.equals("back")) {
						if (text.equals("bottom") || text.equals("botton")) {
							return;
						}
					}

					if (keyword.equals("forward")) {
						if (text.equals("towards the top")
								|| text.equals("to the top")
								|| text.equals("above")) {
							return;
						}
					}

					if (keyword.equals("robot")) {
						if (text.equals("you")) {
							return;
						}
					}

					if (keyword.equals("cube-group")) {
						if (text.equals("block") || text.equals("cubes")
								|| text.equals("blocks")) {
							return;
						}
					}

					if (keyword.equals("stack")) {
						if (text.equals("tower")
								|| text.equals("parallelipiped")
								|| text.equals("block tower")
								|| text.equals("cube stack")
								|| text.equals("combination tower")
								|| text.equals("slab")
								|| text.equals("stack of blocks")
								|| text.equals("pillar")) {
							return;
						}
					}

					if (keyword.equals("individual")) {
						if (text.equals("single")) {
							return;
						}
					}

					System.out.println(++count + ") " + id + "\t" + keyword
							+ "\t" + text);
				}
			});
		}
	}
}