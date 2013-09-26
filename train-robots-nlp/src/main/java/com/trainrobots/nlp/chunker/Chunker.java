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

public abstract class Chunker {

	public abstract List<Token> getSequence(String text);

	public abstract void train(List<Command> commands);

	public static List<Chunk> getChunks(List<Token> tokens) {
		List<Chunk> chunks = new ArrayList<Chunk>();
		for (int i = 0; i < tokens.size(); i++) {

			// O
			Token token = tokens.get(i);
			if (token.tag.equals("O") || token.tag.equals("DET")) {
				// chunks.add(new Chunk(token.tag, new String[] { token.token
				// }));
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
			String[] list = new String[size];
			for (int j = 0; j < size; j++) {
				list[j] = tokens.get(i + j).token;
			}
			chunks.add(new Chunk(token.tag.substring(2), list));
			i += size - 1;
		}
		return chunks;
	}
}