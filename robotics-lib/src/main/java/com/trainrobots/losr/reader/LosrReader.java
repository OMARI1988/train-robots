/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr.reader;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.losr.Losr;

public class LosrReader {

	private static final LosrFactory factory = new LosrFactory();
	private final String text;
	private int position;

	public LosrReader(String text) {
		this.text = text;
		readWhitespace();
	}

	public Losr read() {

		// (
		char ch = next();
		if (ch != '(') {
			throw new RoboticException("Expected opening bracket.");
		}

		// Name.
		String name = readName();
		if (peek() != ':') {
			throw new RoboticException("Expected semi-colon.");
		}
		position++;

		// Content.
		String content = null;
		ItemsList<Losr> children = null;
		while (peek() != ')') {

			// Whitespace.
			if (!whitespace(peek())) {
				throw new RoboticException("Expected space.");
			}
			readWhitespace();

			// Child.
			if (peek() == '(') {
				if (children == null) {
					children = new ItemsList<Losr>();
				}
				children.add(read());
			} else {
				content = readContent();
			}
		}

		// ')'
		ch = next();
		if (ch != ')') {
			throw new RoboticException("Expected closing bracket.");
		}

		// Build node.
		if (content != null) {
			return factory.build(name, content);
		}
		return factory.build(name, children);
	}

	private String readName() {
		int index = position;
		while (peek() != ':') {
			next();
		}
		return text.substring(index, position);
	}

	private String readContent() {
		int index = position;
		while (!whitespace(peek()) && peek() != ')') {
			next();
		}
		return text.substring(index, position);
	}

	private char peek() {
		return text.charAt(position);
	}

	private char next() {
		return text.charAt(position++);
	}

	private void readWhitespace() {
		while (whitespace(peek())) {
			next();
		}
	}

	private static boolean whitespace(char ch) {
		return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
	}
}