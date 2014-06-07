/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.losr;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.tokenizer.Tokenizer;
import com.trainrobots.treebank.Command;

public class LosrTree {

	private final Items<Token> tokens;
	private final LosrNode root;

	public LosrTree(Command command) {
		this(command.text(), command.losr());
	}

	public LosrTree(String text, Losr losr) {
		tokens = tokens(text);
		root = new LosrNode(losr);
	}

	public Items<Token> tokens() {
		return tokens;
	}

	public LosrNode root() {
		return root;
	}

	public Token getToken(int id) {
		for (Token token : tokens) {
			if (token.id() == id) {
				return token;
			}
		}
		throw new RoboticException("Failed to find token " + id);
	}

	private static Items<Token> tokens(String text) {
		Tokenizer tokenizer = new Tokenizer(text);
		Items<Terminal> terminals = tokenizer.tokens();
		int size = terminals.count();
		Token[] tokens = new Token[size];
		for (int i = 0; i < size; i++) {
			String token = terminals.get(i).context().text().toLowerCase();
			tokens[i] = new Token(i + 1, token);
		}
		return new ItemsArray<Token>(tokens);
	}
}