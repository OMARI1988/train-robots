/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.losr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TokenContext;

public class LosrNode implements Iterable<LosrNode> {

	private final Losr losr;
	private final String tag;
	private final List<LosrNode> children = new ArrayList<LosrNode>();
	private int tokenStart;
	private int tokenEnd;
	private LosrNode parent;

	public LosrNode(Losr losr) {

		// Tag.
		this.losr = losr;
		this.tag = losr.name();

		// Terminal?
		if (losr instanceof Terminal) {
			TokenContext context = ((Terminal) losr).context();
			this.tokenStart = context.start();
			this.tokenEnd = context.end();
		}

		// Recurse.
		for (Losr child : losr) {
			add(new LosrNode(child));
		}
	}

	public String tag() {
		return tag;
	}

	public Losr losr() {
		return losr;
	}

	public int size() {
		return children.size();
	}

	public LosrNode parent() {
		return parent;
	}

	public LosrNode get(int index) {
		return children.get(index);
	}

	public void add(LosrNode child) {
		child.parent = this;
		children.add(child);
	}

	public void remove(int index) {
		children.remove(index).parent = null;
	}

	public int tokenStart() {
		return tokenStart;
	}

	public void setTokenStart(int tokenStart) {
		this.tokenStart = tokenStart;
	}

	public int tokenEnd() {
		return tokenEnd;
	}

	public void setTokenEnd(int tokenEnd) {
		this.tokenEnd = tokenEnd;
	}

	public Iterator<LosrNode> iterator() {
		return children.iterator();
	}

	@Override
	public String toString() {
		return losr.toString();
	}
}