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

public class LosrParser {

	private static final LosrFactory factory = new LosrFactory();
	private final String text;
	private int position;

	public LosrParser(String text) {
		this.text = text;
		skipWhitespace();
	}

	public Losr parse() {

		// (
		char ch = next();
		if (ch != '(') {
			throw new RoboticException("Expected opening bracket.");
		}

		// Node.
		return readNode(readName());
	}

	private Losr readNode(String name) {

		// ':'
		readSemiColon();

		// Children.
		int id = 0;
		int referenceId = 0;
		TokenContext context = null;
		String content = null;
		ItemsList<Losr> children = null;
		while (peek() != ')') {

			// Whitespace.
			readWhitespace();

			// Content.
			if (peek() != '(') {
				content = readContent();
				continue;
			}

			// ID.
			next();
			String childName = readName();
			if (childName.equals("id")) {
				id = readId();
				continue;
			}

			// Reference ID.
			if (childName.equals("reference-id")) {
				referenceId = readId();
				continue;
			}

			// Token.
			if (childName.equals("token")) {
				context = readContext();
				continue;
			}

			// Child.
			if (children == null) {
				children = new ItemsList<Losr>();
			}
			children.add(readNode(childName));
		}

		// ')'
		readClosingBracket();

		// Build node.
		if (content != null) {
			return factory.build(context, name, content);
		}
		return factory.build(id, referenceId, name, children);
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

	private int readId() {

		// ':'
		readSemiColon();

		// Whitespace.
		readWhitespace();

		// Start.
		int index = position;
		while (peek() != ')') {
			next();
		}
		int id = Integer.parseInt(text.substring(index, position));

		// ')'
		readClosingBracket();

		// ID.
		return id;
	}

	private TokenContext readContext() {

		// ':'
		readSemiColon();

		// Whitespace.
		readWhitespace();

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
		readClosingBracket();

		// Context.
		return new TokenContext(start, end);
	}

	private void readClosingBracket() {
		char ch = next();
		if (ch != ')') {
			throw new RoboticException("Expected closing bracket.");
		}
	}

	private void readSemiColon() {
		if (peek() != ':') {
			throw new RoboticException("Expected semi-colon.");
		}
		next();
	}

	private void readWhitespace() {
		if (!whitespace(peek())) {
			throw new RoboticException("Expected whitespace.");
		}
		skipWhitespace();
	}

	private void skipWhitespace() {
		while (whitespace(peek())) {
			next();
		}
	}

	private char peek() {
		return text.charAt(position);
	}

	private char next() {
		return text.charAt(position++);
	}

	private static boolean whitespace(char ch) {
		return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
	}
}