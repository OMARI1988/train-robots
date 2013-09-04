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

package com.trainrobots.nlp.generation;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.nlp.NlpException;
import com.trainrobots.nlp.trees.Node;

public class Generator {

	private final StringBuilder text = new StringBuilder();

	public static String generate(Node node) {
		Generator generator = new Generator();
		generator.generateNode(node);
		return generator.text.toString();
	}

	private void generateNode(Node node) {

		// Object.
		if (node.hasTag("Object")) {
			generateObject(node);
			return;
		}

		// Command.
		if (node.hasTag("Command")) {
			generateCommand(node);
			return;
		}

		// Unknown.
		throw new NlpException("Invalid node: " + node);
	}

	private void generateCommand(Node node) {

		// Find attributes.
		Node actionNode = null;
		Node argumentNode = null;
		List<Node> attributes = new ArrayList<Node>();
		for (Node child : node.children) {
			if (child.hasTag("Action")) {
				actionNode = child;
			} else if (child.hasTag("Arg")) {
				argumentNode = child;
			} else {
				attributes.add(child);
			}
		}

		// Action.
		if (actionNode == null) {
			throw new NlpException("Action not specified: " + node);
		}
		write(actionNode.getValue());

		// Argument.
		if (argumentNode == null) {
			throw new NlpException("Argument not specified: " + node);
		}
		generateNode(argumentNode.getSingleChild());
	}

	private void generateObject(Node node) {

		// Pronoun?
		if (node.isPreTerminal() && node.children.get(0).tag.charAt(0) == '@') {
			write("it");
			return;
		}

		// Find attributes.
		Node classNode = null;
		Node stateNode = null;
		List<Node> attributes = new ArrayList<Node>();
		for (Node child : node.children) {
			if (child.hasTag("Class")) {
				classNode = child;
			} else if (child.hasTag("State")) {
				stateNode = child;
			} else {
				attributes.add(child);
			}
		}

		// State.
		if (stateNode != null) {
			String state = stateNode.getValue();
			if (state.equals("definite")) {
				write("the");
			} else {
				throw new NlpException("Invalid state: " + stateNode);
			}
		}

		// Write attributes.
		for (Node attribute : attributes) {
			write(attribute.getValue());
		}

		// Class.
		if (classNode == null) {
			throw new NlpException("Class not specified: " + node);
		}
		write(classNode.getValue());
	}

	private void write(String text) {
		if (this.text.length() > 0) {
			this.text.append(' ');
		}
		this.text.append(text);
	}
}