/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.trainrobots.ui.visualization.VisualContext;

public class Visual implements Iterable<Visual> {

	protected float x;
	protected float y;
	protected float width;
	protected float height;
	private List<Visual> children;

	public void x(float x) {
		this.x = x;
	}

	public float x() {
		return x;
	}

	public void y(float y) {
		this.y = y;
	}

	public float y() {
		return y;
	}

	public void width(float width) {
		this.width = width;
	}

	public float width() {
		return width;
	}

	public void height(float height) {
		this.height = height;
	}

	public float height() {
		return height;
	}

	public float x2() {
		return x + width;
	}

	public float y2() {
		return y + height;
	}

	public void add(Visual child) {
		if (children == null) {
			children = new ArrayList<Visual>();
		}
		children.add(child);
	}

	public int count() {
		return children != null ? children.size() : 0;
	}

	public Visual get(int index) {
		return children.get(index);
	}

	public Iterator<Visual> iterator() {
		return children.iterator();
	}

	public <T> T find(Class<T> type, float fx, float fy, float x, float y) {

		// Offset.
		x += this.x;
		y += this.y;

		// In bounds?
		if (getClass() == type && fx >= x && fx <= x + width && fy >= y
				&& fy <= y + height) {
			return (T) this;
		}

		// Recurse.
		int size = count();
		for (int i = 0; i < size; i++) {
			T match = get(i).find(type, fx, fy, x, y);
			if (match != null) {
				return match;
			}
		}
		return null;
	}

	public void pack() {

		// No children?
		if (children == null) {
			return;
		}

		// Initiate.
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;

		// Children.
		for (Visual child : children) {

			// Child bounding box.
			float x1 = child.x;
			float y1 = child.y;
			float x2 = child.x2();
			float y2 = child.y2();

			// Extent.
			if (x1 < minX) {
				minX = x1;
			}
			if (y1 < minY) {
				minY = y1;
			}
			if (x2 > maxX) {
				maxX = x2;
			}
			if (y2 > maxY) {
				maxY = y2;
			}
		}

		// Set bounding box.
		x = 0;
		y = 0;
		width = maxX - minX;
		height = maxY - minY;

		// Update children.
		for (Visual child : children) {
			child.x -= minX;
			child.y -= minY;
		}
	}

	public void render(VisualContext context, float x, float y) {
	}
}