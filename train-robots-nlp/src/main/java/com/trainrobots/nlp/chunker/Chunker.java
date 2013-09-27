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

package com.trainrobots.nlp.chunker;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.rcl.ActionAttribute;
import com.trainrobots.core.rcl.ColorAttribute;
import com.trainrobots.core.rcl.Rcl;
import com.trainrobots.core.rcl.TypeAttribute;

public abstract class Chunker {

	public abstract List<Token> getSequence(String text);

	public abstract void train(List<Command> commands);

	public static List<Chunk> getChunks(List<Token> tokens) {
		List<Chunk> chunks = new ArrayList<Chunk>();
		for (int i = 0; i < tokens.size(); i++) {

			// O
			Token token = tokens.get(i);
			if (token.tag.equals("O") || token.tag.equals("DET")) {
				continue;
			}

			// Sequence.
			else if (!token.tag.startsWith("B-")) {
				throw new CoreException("Invalid sequence.");
			}
			int size = 1;
			while (i + size < tokens.size()
					&& tokens.get(i + size).tag.startsWith("I-")) {
				size++;
			}
			String tag = token.tag.substring(2);
			Rcl rcl = createRcl(tag);
			rcl.setTokenStart(i + 1);
			rcl.setTokenEnd(i + size);
			i += size - 1;
		}
		return chunks;
	}

	private static Rcl createRcl(String tag) {

		// Map.
		if (tag.equals("ACT")) {
			return new ActionAttribute(null);
		}
		if (tag.equals("COLOR")) {
			return new ColorAttribute(null);
		}
		if (tag.equals("TYPE")) {
			return new TypeAttribute(null);
		}

		// No match.
		throw new CoreException("Failed to map chunk tag '" + tag + "' to RCL.");
	}
}