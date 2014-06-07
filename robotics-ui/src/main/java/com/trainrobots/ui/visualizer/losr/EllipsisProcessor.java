/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.losr;

import java.util.ArrayList;

import com.trainrobots.collections.ItemsList;

public class EllipsisProcessor {

	private final LosrTree tree;
	private final ItemsList<Token> tokens;
	private ArrayList<EllipsisInfo> ellipsisList = new ArrayList<EllipsisInfo>();
	private Token previousToken;

	public EllipsisProcessor(LosrTree tree, ItemsList<Token> tokens) {
		this.tree = tree;
		this.tokens = tokens;
	}

	public void normalize() {
		findEllipsis(tree.root());
		createEllipticalNodes();
	}

	private void findEllipsis(LosrNode node) {

		// Skip?
		if (node.tag().equals("id:") || node.tag().equals("reference-id:")) {
			return;
		}

		// Is this node a pre-terminal?
		if (node.size() == 0) {

			// Track tokens.
			if (node.tokenStart() != 0) {
				previousToken = tree.getToken(node.tokenStart());
				if (ellipsisList.size() > 0) {
					EllipsisInfo ellipsisInfo = ellipsisList.get(ellipsisList
							.size() - 1);
					if (ellipsisInfo.nextToken == null) {
						ellipsisInfo.nextToken = previousToken;
					}
				}
			}

			// Ellipsis.
			else {
				EllipsisInfo ellipsisInfo = new EllipsisInfo();
				ellipsisInfo.node = node;
				ellipsisInfo.previousToken = previousToken;
				ellipsisList.add(ellipsisInfo);
			}
			return;
		}

		// Recurse.
		for (LosrNode child : node) {
			findEllipsis(child);
		}
	}

	private void createEllipticalNodes() {
		for (EllipsisInfo info : ellipsisList) {
			int index = tokens.indexOf(info.previousToken);
			int id = getMaxId(tree) + 1;
			tokens.add(index + 1, new Token(id, "Ø"));
			info.node.setTokenStart(id);
			info.node.setTokenEnd(id);
		}
	}

	private static int getMaxId(LosrTree tree) {
		int maxId = 0;
		for (Token token : tree.tokens()) {
			if (token.id() > maxId) {
				maxId = token.id();
			}
		}
		return maxId;
	}

	private static class EllipsisInfo {
		LosrNode node;
		Token previousToken;
		Token nextToken;
	}
}