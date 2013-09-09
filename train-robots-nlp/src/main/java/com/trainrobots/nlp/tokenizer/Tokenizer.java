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

	private final Node tokens = new Node("tokens:");
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
		return new Node("text:", text);
	}

	private Node readNumber() {

		// Number.
		int index = position;
		while (!end() && digit(peek())) {
			position++;
		}
		String text = this.text.substring(index, position);

		// Suffix?
		if (hasOrdinalSuffix(text)) {
			position += 2;
			return new Node("ordinal:", text);
		}

		// Cardinal.
		return new Node("cardinal:", text);
	}

	private boolean hasOrdinalSuffix(String text) {
		if (!canRead(2)) {
			return false;
		}
		char ch = peek();
		char ch2 = peek(2);
		if (text.equals("1")) {
			return ch == 's' && ch2 == 't';
		}
		if (text.equals("2")) {
			return ch == 'n' && ch2 == 'd';
		}
		if (text.equals("3")) {
			return ch == 'r' && ch2 == 'd';
		}
		return ch == 't' && ch2 == 'h';
	}

	private Node readWhitespace() {
		while (!end() && whitespace(peek())) {
			position++;
		}
		return null;
	}

	private Node readPunctuation() {
		char ch = peek();
		String tag = "punctuation:";
		switch (ch) {
		case '.':
		case '!':
		case '?':
			tag = "end:";
			break;
		case ',':
			tag = "comma:";
			break;
		case '(':
			tag = "open-bracket:";
			break;
		case ')':
			tag = "close-bracket:";
			break;
		case '-':
			tag = "dash:";
			break;
		case '/':
			tag = "forward-slash:";
			break;
		}
		position++;
		return new Node(tag, new String(new char[] { ch }));
	}

	private static boolean punctuation(char ch) {
		return ch == '.' || ch == '(' || ch == ')' || ch == '-' || ch == ','
				|| ch == '/' || ch == '!' || ch == '?';
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

	private char peek(int count) {
		return text.charAt(position + count - 1);
	}

	public boolean canRead(int count) {
		return position + count <= text.length();
	}

	private boolean end() {
		return position >= text.length();
	}
}