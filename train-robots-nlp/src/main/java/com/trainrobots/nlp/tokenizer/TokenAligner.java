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

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.nodes.Node;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.core.rcl.SpatialIndicator;
import com.trainrobots.core.rcl.TypeAttribute;

public class TokenAligner {

	private final List<String> tokens = new ArrayList<String>();
	private final List<Rcl> leaves = new ArrayList<Rcl>();
	private int tokenIndex;
	private int leafIndex;

	private TokenAligner(Rcl rcl, String text) {

		// Leaves.
		findLeaves(rcl);

		// Tokens.
		for (Node node : Tokenizer.getTokens(text).children) {
			tokens.add(node.getValue());
		}
	}

	public static List<String> align(Rcl rcl, String text) {
		TokenAligner aligner = new TokenAligner(rcl, text);
		aligner.align();
		return aligner.tokens;
	}

	private void align() {

		int leafCount = leaves.size();
		int tokenCount = tokens.size();

		for (tokenIndex = 0; tokenIndex < tokenCount; tokenIndex++) {
			if (leafIndex >= leafCount) {
				return;
			}
			Rcl leaf = leaves.get(leafIndex);

			// Skip.
			String token = tokens.get(tokenIndex);
			if (token.equals("the") || token.equals("to")) {
				continue;
			}

			// Match.
			if (isAbove(leaf) && match("top", "of")) {
				continue;
			}
			if (isAbove(leaf) && match("on", "top", "of")) {
				continue;
			}
			if (isAbove(leaf) && match("sitting", "on", "top", "of")) {
				continue;
			}
			if (isAction(leaf) && match("pick", "up")) {
				continue;
			}
			if (isColor(leaf) && match("light", "grey")) {
				continue;
			}

			// Align.
			leaf.setTokenStart(tokenIndex + 1);
			leaf.setTokenEnd(tokenIndex + 1);
			leafIndex++;
		}
	}

	private boolean isAbove(Rcl rcl) {
		if (rcl instanceof IndicatorAttribute) {
			IndicatorAttribute attribute = (IndicatorAttribute) rcl;
			if (attribute.indicator() == SpatialIndicator.above) {
				return true;
			}
		}
		return false;
	}

	private boolean isAction(Rcl rcl) {
		return rcl instanceof ActionAttribute;
	}

	private boolean isColor(Rcl rcl) {
		return rcl instanceof ColorAttribute;
	}

	private boolean match(String... tokens) {

		// Match.
		for (int i = 0; i < tokens.length; i++) {
			int j = tokenIndex + i;
			if (j >= this.tokens.size()) {
				return false;
			}
			if (!this.tokens.get(j).equals(tokens[i])) {
				return false;
			}
		}

		// Align.
		Rcl leaf = leaves.get(leafIndex);
		leaf.setTokenStart(tokenIndex + 1);
		leaf.setTokenEnd(tokenIndex + tokens.length);
		tokenIndex += tokens.length - 1;
		leafIndex++;
		return true;
	}

	private void findLeaves(Rcl rcl) {

		rcl.accept(new RclVisitor() {
			public void visit(ActionAttribute actionAttribute) {
				leaves.add(actionAttribute);
			}

			public void visit(ColorAttribute colorAttribute) {
				leaves.add(colorAttribute);
			}

			public void visit(IndicatorAttribute indicatorAttribute) {
				leaves.add(indicatorAttribute);
			}

			public void visit(TypeAttribute typeAttribute) {
				leaves.add(typeAttribute);
			}
		});

		for (Rcl leaf : leaves) {
			leaf.setTokenStart(0);
			leaf.setTokenEnd(0);
		}
	}
}