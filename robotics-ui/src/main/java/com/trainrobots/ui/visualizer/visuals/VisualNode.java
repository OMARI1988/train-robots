/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.visuals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VisualNode implements Iterable<VisualNode> {

	protected float x;
	protected float y;
	protected float width;
	protected float height;
	private List<VisualNode> children;

	public void setX(float x) {
		this.x = x;
	}

	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getWidth() {
		return width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getHeight() {
		return height;
	}

	public float getX2() {
		return x + width;
	}

	public float getY2() {
		return y + height;
	}

	public void add(VisualNode child) {
		if (children == null) {
			children = new ArrayList<VisualNode>();
		}
		children.add(child);
	}

	public int getChildCount() {
		return children != null ? children.size() : 0;
	}

	public VisualNode getChild(int index) {
		return children.get(index);
	}

	public Iterator<VisualNode> iterator() {
		return children.iterator();
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
		for (VisualNode child : children) {

			// Child bounding box.
			float x1 = child.x;
			float y1 = child.y;
			float x2 = child.getX2();
			float y2 = child.getY2();

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
		for (VisualNode child : children) {
			child.x -= minX;
			child.y -= minY;
		}
	}

	public void centerContentHorizontally() {
		if (children != null) {
			for (VisualNode child : children) {
				child.x = 0.5f * (width - child.width);
			}
		}
	}

	public void render(VisualContext context, float x, float y) {
	}
}