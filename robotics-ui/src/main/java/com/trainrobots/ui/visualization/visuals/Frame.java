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

	private final Visual tag;
	private final List<Frame> frames = new ArrayList<Frame>();
	private final boolean skip;

	public Frame(Visual tag, boolean skip) {
		this.tag = tag;
		this.skip = skip;
		add(tag);
	}

	public Visual tag() {
		return tag;
	}

	public boolean skip() {
		return skip;
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
}