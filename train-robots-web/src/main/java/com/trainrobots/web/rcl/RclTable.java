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

package com.trainrobots.web.rcl;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.nodes.Node;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class RclTable {

	private RclLine line;
	private final List<RclLine> lines = new ArrayList<RclLine>();
	private final List<Node> tokens;

	public RclTable(Command command) {
		tokens = Tokenizer.getTokens(command.text).children;
		Node node = command.rcl.toNode();
		line = new RclLine();
		lines.add(line);
		format(node, 0);
	}

	public List<RclLine> lines() {
		return lines;
	}

	private void format(Node node, int level) {

		// Leaf.
		if (node.isLeaf()) {
			for (int i = 0; i < level * 2; i++) {
				line.append("&nbsp;");
			}
			line.append(node.tag);
			return;
		}

		// Non-leaf.
		for (int i = 0; i < level * 2; i++) {
			line.append("&nbsp;");
		}
		if (node.isChain()) {
			writeChain(node);
		} else {
			line.append('(');
			line.append(node.tag);
			for (int i = 0; i < node.children.size(); i++) {
				Node child = node.children.get(i);
				if (i == 0 && child.isLeaf()) {
					line.append("&nbsp;");
					line.append(child.tag);
				} else {
					lines.add(line = new RclLine());
					format(child, level + 1);
				}
			}
			line.append(')');
		}
	}

	private void writeChain(Node node) {
		if (!node.isLeaf()) {
			line.append('(');
		}
		if (node.tag != null) {
			line.append(node.tag);
		}
		if (!node.isLeaf()) {
			for (Node child : node.children) {
				if (child.tag != null && child.tag.equals("token:")) {
					align(child);
					continue;
				}
				line.append("&nbsp;");
				writeChain(child);
			}
			line.append(')');
		}
	}

	private void align(Node node) {
		if (line.tokens != null) {
			throw new CoreException("Duplicate RCL world alignment.");
		}
		List<Node> list = node.children;
		int tokenStart = Integer.parseInt(list.get(0).tag);
		int tokenEnd = tokenStart;
		if (list.size() == 2) {
			tokenEnd = Integer.parseInt(list.get(1).tag);
		}
		StringBuilder result = new StringBuilder();
		for (int i = tokenStart; i <= tokenEnd; i++) {
			if (result.length() > 0) {
				result.append("&nbsp;");
			}
			result.append(tokens.get(i - 1).children.get(0).tag);
		}
		line.tokens = result.toString();
	}
}