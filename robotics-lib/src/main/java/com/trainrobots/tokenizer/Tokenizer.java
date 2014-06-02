/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.tokenizer;

import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Ordinal;
import com.trainrobots.losr.Symbol;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.Text;
import com.trainrobots.losr.TokenContext;

public class Tokenizer {

	private final ItemsList<Terminal> tokens = new ItemsList<Terminal>();
	private final String text;
	private int position;

	public Tokenizer(String text) {
		this.text = text;
	}

	public Items<Terminal> tokens() {
		while (!end()) {
			Terminal token = readToken();
			if (token != null) {
				tokens.add(token);
			}
		}
		return tokens;
	}

	private Terminal readToken() {

		// Whitespace.
		char ch = peek();
		if (whitespace(ch)) {
			return readWhitespace();
		}

		// Symbol.
		if (symbol(ch)) {
			return readSymbol();
		}

		// Digits.
		if (digit(ch)) {
			return readNumber();
		}

		// Text.
		return readText();
	}

	private Text readText() {
		int index = position;
		while (!end() && !whitespace(peek()) && !symbol(peek())) {
			position++;
		}
		return new Text(context(), text.substring(index, position));
	}

	private Terminal readNumber() {

		// Number.
		int index = position;
		while (!end() && digit(peek())) {
			position++;
		}
		int value = Integer.parseInt(text.substring(index, position));

		// Suffix?
		if (hasOrdinalSuffix(value)) {
			position += 2;
			return new Ordinal(context(), value);
		}

		// Cardinal.
		return new Cardinal(context(), value);
	}

	private boolean hasOrdinalSuffix(int value) {
		if (!canRead(2)) {
			return false;
		}
		char ch = peek();
		char ch2 = peek(2);
		switch (value) {
		case 1:
			return ch == 's' && ch2 == 't';
		case 2:
			return ch == 'n' && ch2 == 'd';
		case 3:
			return ch == 'r' && ch2 == 'd';
		default:
			return ch == 't' && ch2 == 'h';
		}
	}

	private Terminal readWhitespace() {
		while (!end() && whitespace(peek())) {
			position++;
		}
		return null;
	}

	private Terminal readSymbol() {
		char ch = peek();
		position++;
		return new Symbol(context(), ch);
	}

	private TokenContext context() {
		return new TokenContext(tokens.size() + 1);
	}

	private static boolean symbol(char ch) {
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

	private boolean canRead(int count) {
		return position + count <= text.length();
	}

	private boolean end() {
		return position >= text.length();
	}
}