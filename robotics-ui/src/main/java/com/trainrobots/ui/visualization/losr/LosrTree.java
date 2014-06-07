/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.losr;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.collections.ItemsList;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.tokenizer.Tokenizer;
import com.trainrobots.treebank.Command;

public class LosrTree {

	private final ItemsList<Token> tokens = new ItemsList<Token>();
	private final LosrNode root;

	public LosrTree(Command command) {
		this(command.text(), command.losr());
	}

	public LosrTree(String text, Losr losr) {

		// Tokens.
		Tokenizer tokenizer = new Tokenizer(text);
		Items<Terminal> terminals = tokenizer.tokens();
		int size = terminals.count();
		for (int i = 0; i < size; i++) {
			String token = terminals.get(i).context().text().toLowerCase();
			tokens.add(new Token(i + 1, token));
		}

		// Root.
		root = new LosrNode(losr);

		// Normalize.
		new EllipsisProcessor(this, tokens).normalize();
	}

	public Items<Token> tokens() {
		return tokens;
	}

	public LosrNode root() {
		return root;
	}

	public Token token(int id) {
		for (Token token : tokens) {
			if (token.id() == id) {
				return token;
			}
		}
		throw new RoboticException("Failed to find token " + id);
	}
}