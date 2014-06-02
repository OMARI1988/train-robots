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
import com.trainrobots.losr.TokenContext;

public class LosrReader {

	private static final LosrFactory factory = new LosrFactory();
	private final String text;
	private int position;

	public LosrReader(String text) {
		this.text = text;
		skipWhitespace();
	}

	public Losr read() {

		// (
		char ch = next();
		if (ch != '(') {
			throw new RoboticException("Expected opening bracket.");
		}

		// Name.
		String name = readName();
		return readLosr(name);
	}

	private Losr readLosr(String name) {

		// ':'
		if (peek() != ':') {
			throw new RoboticException("Expected semi-colon.");
		}
		next();

		// Children.
		TokenContext context = null;
		String content = null;
		ItemsList<Losr> children = null;
		while (peek() != ')') {

			// Whitespace.
			if (!whitespace(peek())) {
				throw new RoboticException("Expected space.");
			}
			skipWhitespace();

			// Content?
			if (peek() != '(') {
				content = readContent();
			}

			// Token?
			else {
				next();
				String childName = readName();
				if (childName.equals("token")) {
					context = readContext();
				}

				// Child.
				else {
					if (children == null) {
						children = new ItemsList<Losr>();
					}
					children.add(readLosr(childName));
				}
			}
		}

		// ')'
		char ch = next();
		if (ch != ')') {
			throw new RoboticException("Expected closing bracket.");
		}

		// Build node.
		if (content != null) {
			return factory.build(context, name, content);
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

	private TokenContext readContext() {

		// ':'
		if (peek() != ':') {
			throw new RoboticException("Expected semi-colon.");
		}
		next();

		// Whitespace.
		if (!whitespace(peek())) {
			throw new RoboticException("Expected space.");
		}
		skipWhitespace();

		// Start.
		int index = position;
		while (!whitespace(peek()) && peek() != ')') {
			next();
		}
		int start = Integer.parseInt(text.substring(index, position));
		int end = start;

		// End.
		if (whitespace(peek())) {
			skipWhitespace();
			index = position;
			while (peek() != ')') {
				next();
			}
			end = Integer.parseInt(text.substring(index, position));
		}

		// ')'
		char ch = next();
		if (ch != ')') {
			throw new RoboticException("Expected closing bracket.");
		}

		// Context.
		return new TokenContext(start, end);
	}

	private char peek() {
		return text.charAt(position);
	}

	private char next() {
		return text.charAt(position++);
	}

	private void skipWhitespace() {
		while (whitespace(peek())) {
			next();
		}
	}

	private static boolean whitespace(char ch) {
		return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
	}
}