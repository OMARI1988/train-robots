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

import com.trainrobots.nlp.trees.Node;

public class Tokenizer {

	private final Node tokens = new Node("Tokens");
	private final String text;
	private int position;

	private Tokenizer(String text) {
		this.text = text;
	}

	public static Node getTokens(String text) {
		Tokenizer tokenizer = new Tokenizer(text);
		tokenizer.tokenize();
		return tokenizer.tokens;
	}

	private void tokenize() {
		while (!end()) {
			Node token = readToken();
			if (token != null) {
				tokens.add(token);
			}
		}
	}

	private Node readToken() {

		// Whitespace.
		char ch = peek();
		if (whitespace(ch)) {
			return readWhitespace();
		}

		// Punctuation.
		if (punctuation(ch)) {
			return readPunctuation();
		}

		// Digits.
		if (digit(ch)) {
			return readNumber();
		}

		// Text.
		return readText();
	}

	private Node readText() {
		int index = position;
		while (!end() && !whitespace(peek()) && !punctuation(peek())) {
			position++;
		}
		String text = this.text.substring(index, position).toLowerCase();
		return new Node("Text", text);
	}

	private Node readNumber() {
		int index = position;
		while (!end() && digit(peek())) {
			position++;
		}
		String text = this.text.substring(index, position);
		return new Node("Number", text);
	}

	private Node readWhitespace() {
		while (!end() && whitespace(peek())) {
			position++;
		}
		return null;
	}

	private Node readPunctuation() {
		char ch = peek();
		String tag = "Punctuation";
		switch (ch) {
		case '.':
			tag = "End";
			break;
		case ',':
			tag = "Comma";
			break;
		case '(':
			tag = "OpenBracket";
			break;
		case ')':
			tag = "CloseBracket";
			break;
		case '-':
			tag = "Dash";
			break;
		case '/':
			tag = "ForwardSlash";
			break;
		}
		position++;
		return new Node(tag, new String(new char[] { ch }));
	}

	private static boolean punctuation(char ch) {
		return ch == '.' || ch == '(' || ch == ')' || ch == '-' || ch == ','
				|| ch == '/';
	}

	private static boolean whitespace(char ch) {
		return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
	}

	private static boolean digit(char ch) {
		return ch >= '0' && ch <= '9';
	}

	private char peek() {
		return text.charAt(position);
	}

	private boolean end() {
		return position >= text.length();
	}
}