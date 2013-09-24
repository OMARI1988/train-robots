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

import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.IndicatorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.RclVisitor;
import com.trainrobots.core.rcl.TypeAttribute;

public class TokenAligner {

	private final Rcl rcl;
	// private final Node tokens;
	private int index = 0;

	private TokenAligner(Rcl rcl, String text) {
		this.rcl = rcl;
		// this.tokens = Tokenizer.getTokens(text);
	}

	public static void align(Rcl rcl, String text) {
		new TokenAligner(rcl, text).align();
	}

	private void align() {

		// Clear.
		clearAlignment();

		// Add.
		rcl.accept(new RclVisitor() {
			public void visit(ActionAttribute attribute) {
				index++;
				attribute.setTokenStart(index);
				attribute.setTokenEnd(index);
			}

			public void visit(ColorAttribute attribute) {
				index++;
				attribute.setTokenStart(index);
				attribute.setTokenEnd(index);
			}

			public void visit(IndicatorAttribute attribute) {
				index++;
				attribute.setTokenStart(index);
				attribute.setTokenEnd(index);
			}

			public void visit(TypeAttribute attribute) {
				index++;
				attribute.setTokenStart(index);
				attribute.setTokenEnd(index);
			}
		});
	}

	private void clearAlignment() {

		rcl.accept(new RclVisitor() {
			public void visit(ActionAttribute actionAttribute) {
				actionAttribute.setTokenStart(0);
				actionAttribute.setTokenEnd(0);
			}

			public void visit(ColorAttribute colorAttribute) {
				colorAttribute.setTokenStart(0);
				colorAttribute.setTokenEnd(0);
			}

			public void visit(IndicatorAttribute indicatorAttribute) {
				indicatorAttribute.setTokenStart(0);
				indicatorAttribute.setTokenEnd(0);
			}

			public void visit(TypeAttribute typeAttribute) {
				typeAttribute.setTokenStart(0);
				typeAttribute.setTokenEnd(0);
			}
		});
	}
}