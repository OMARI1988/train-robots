/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import java.util.ArrayList;
import java.util.List;

public class Frame extends Visual {

	private final Text tag;
	private final List<Frame> frames = new ArrayList<Frame>();
	private boolean skip;

	public Frame(Text tag) {
		this.tag = tag;
		add(tag);
	}

	public Text tag() {
		return tag;
	}

	public Iterable<Frame> frames() {
		return frames;
	}

	public boolean leaf() {
		return frames.size() == 0;
	}

	public void add(Frame frame) {
		super.add(frame);
		frames.add(frame);
	}

	public boolean skip() {
		return skip;
	}

	public void skip(boolean skip) {
		this.skip = skip;
	}
}