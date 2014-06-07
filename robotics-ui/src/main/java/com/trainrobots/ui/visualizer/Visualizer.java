/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.ui.visualizer.losr.LosrNode;
import com.trainrobots.ui.visualizer.losr.LosrTree;
import com.trainrobots.ui.visualizer.losr.Token;
import com.trainrobots.ui.visualizer.visuals.LayoutNode;
import com.trainrobots.ui.visualizer.visuals.LineNode;
import com.trainrobots.ui.visualizer.visuals.TextNode;
import com.trainrobots.ui.visualizer.visuals.VisualContext;
import com.trainrobots.ui.visualizer.visuals.VisualNode;
import com.trainrobots.ui.visualizer.visuals.VisualTree;

public class Visualizer {

	private final LosrTree tree;
	private static final Font FONT = new Font("Times New Roman", Font.PLAIN, 16);
	private static final Font FONT2 = new Font("Arial", Font.PLAIN, 16);
	private static final int HORIZONTAL_MARGIN = 10;
	private static final int VERTICAL_MARGIN = 20;
	private Map<Integer, LayoutNode> tokens = new HashMap<Integer, LayoutNode>();
	private final VisualNode canvas = new VisualNode();
	private final boolean darkTheme;

	private static final Stroke SOLID_LINE = new BasicStroke();
	private static final Stroke DASHED_LINE = new BasicStroke(1f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[] {
					1.5f, 3 }, 0);

	private static final Color DEFAULT_COLOR = Color.BLACK;
	private static final Color DEFAULT_COLOR2 = new Color(200, 200, 200);
	private static final Color EVENT_COLOR = new Color(255, 0, 0);
	private static final Color EVENT_COLOR2 = new Color(255, 165, 0);
	private static final Color SP_COLOR = new Color(0, 176, 80);
	private static final Color SP_COLOR2 = new Color(141, 244, 50);
	private static final Color ENTITY_COLOR = new Color(0, 112, 192);
	private static final Color ENTITY_COLOR2 = new Color(35, 206, 235);
	private static final Color SKIP_COLOR = new Color(120, 120, 120);

	private int lastId = 0;
	private float maxY;

	private List<LayoutNode> skipList = new ArrayList<LayoutNode>();

	public Visualizer(LosrTree tree, boolean darkTheme) {
		this.tree = tree;
		this.darkTheme = darkTheme;
	}

	public VisualTree createVisualTree(VisualContext context) {

		// Tokens.
		// int column = 0;
		for (Token token : tree.tokens()) {
			TextNode tag = new TextNode(context, token.text(),
					darkTheme ? FONT2 : FONT);
			tag.setColor(darkTheme ? DEFAULT_COLOR2 : DEFAULT_COLOR);
			// column++;
			LayoutNode layoutNode = new LayoutNode(tag);
			layoutNode.setWidth(tag.getWidth());
			if (tokens.containsKey(token.id())) {
				throw new RoboticException("Duplicate token ID " + token.id());
			}
			tokens.put(token.id(), layoutNode);
		}

		// Layout.
		LayoutNode layoutRoot = buildLayoutNode(context, tree.root());
		arrange(layoutRoot);
		maxY = layoutRoot.getHeight();

		// Pack.
		pushLeaves(0, 0, layoutRoot);
		canvas.pack();
		return new VisualTree(canvas);
	}

	private LayoutNode buildLayoutNode(VisualContext context, LosrNode losr) {

		// Node.
		String text = losr.tag();
		if (text.endsWith(":")) {
			text = text.substring(0, text.length() - 1);
		}
		Color color = darkTheme ? DEFAULT_COLOR2 : DEFAULT_COLOR;
		if (text.equals("spatial-relation")) {
			text = "sp-relation";
			color = darkTheme ? SP_COLOR2 : SP_COLOR;
		} else if (text.equals("entity")) {
			color = darkTheme ? ENTITY_COLOR2 : ENTITY_COLOR;
		} else if (text.equals("event")) {
			color = darkTheme ? EVENT_COLOR2 : EVENT_COLOR;
		} else if (text.equals("type")) {
			if (((Type) losr.losr()).type() == Types.Reference) {
				text = "reference";
			}
		}
		TextNode tag = new TextNode(context, text, darkTheme ? FONT2 : FONT);
		tag.setColor(color);
		LayoutNode result = new LayoutNode(tag);

		// Pre-terminal?
		if (losr.size() == 0) {
			for (int i = losr.tokenStart(); i <= losr.tokenEnd(); i++) {
				LayoutNode child = tokens.get(i);
				if (child == null) {
					throw new RoboticException("Failed to find token: " + i
							+ " for node '" + losr.tag() + "'.'");
				}
				if (!child.tag().getText().equals("Ø")) {
					for (int j = lastId + 1; j <= i - 1; j++) {
						LayoutNode skipped = tokens.get(j);
						if (skipped != null) {
							skipped.skip = true;
							skipped.tag().setColor(SKIP_COLOR);
							skipList.add(skipped);
						}
					}
					lastId = i;
				}
				result.add(child);
			}
		}

		// Attach.
		if (!result.hasLayoutChildren()) {
			for (LosrNode child : losr) {

				// Skip.
				if (child.tag().equals("id:")
						|| child.tag().equals("reference-id:")) {
					continue;
				}

				// Recurse.
				LayoutNode layoutNode = buildLayoutNode(context, child);
				for (LayoutNode skip : skipList) {
					result.add(skip);
				}
				skipList.clear();
				result.add(layoutNode);
			}
		}
		return result;
	}

	private void arrange(LayoutNode node) {

		// Recurse.
		for (LayoutNode child : node.layoutChildren()) {
			arrange(child);
		}

		// Position children.
		float x = 0;
		float y = node.tag().getHeight() + VERTICAL_MARGIN;
		for (LayoutNode child : node.layoutChildren()) {
			child.setY(y);
			child.setX(x);
			x += child.getWidth() + HORIZONTAL_MARGIN;
		}

		// Not a leaf?
		if (node.hasLayoutChildren()) {

			// Tag.
			LayoutNode first = null;
			LayoutNode last = null;
			for (LayoutNode child : node.layoutChildren()) {
				if (child.skip) {
					continue;
				}
				if (first == null) {
					first = child;
				}
				last = child;
			}
			if (first != null) {
				TextNode tag = node.tag();
				float fx = first.getX() + first.tag().getX() + 0.5f
						* first.tag().getWidth();
				float lx = last.getX() + last.tag().getX() + 0.5f
						* last.tag().getWidth();
				tag.setX(0.5f * (fx + lx) - 0.5f * tag.getWidth());
			}
		}

		// Pack.
		node.pack();
	}

	private void pushLeaves(float x, float y, LayoutNode node) {

		// Offset.
		x += node.getX();
		y += node.getY();

		// Add.
		TextNode tag = node.tag();
		tag.setX(x + tag.getX());
		tag.setY(y + tag.getY());
		canvas.add(tag);

		// No children?
		if (!node.hasLayoutChildren()) {
			tag.setY(maxY - tag.getHeight());
		}

		// Recurse.
		else {

			// Tag.
			float[] p = { tag.getX() + 0.5f * tag.getWidth(), tag.getY2() + 2 };
			List<float[]> l = new ArrayList<float[]>();
			boolean terminal = false;
			for (LayoutNode child : node.layoutChildren()) {
				pushLeaves(x, y, child);
				if (!child.skip) {
					TextNode tag2 = child.tag();
					l.add(new float[] { tag2.getX() + 0.5f * tag2.getWidth(),
							tag2.getY() - 2 });
					terminal = !child.hasLayoutChildren();
				}
			}

			// Lines.
			addLines(p, l, terminal);
		}
	}

	private void addLines(float[] p, List<float[]> l, boolean terminal) {

		// Triangle?
		Color color = darkTheme ? DEFAULT_COLOR2 : DEFAULT_COLOR;
		if (terminal && l.size() >= 2) {
			float[] u = l.get(0);
			float[] v = l.get(l.size() - 1);
			{
				LineNode line = new LineNode(p[0], p[1], u[0], u[1]);
				line.setStroke(DASHED_LINE);
				line.setColor(color);
				canvas.add(line);
			}
			{
				LineNode line = new LineNode(p[0], p[1], v[0], v[1]);
				line.setStroke(DASHED_LINE);
				line.setColor(color);
				canvas.add(line);
			}
			{
				LineNode line = new LineNode(u[0], u[1], v[0], v[1]);
				line.setStroke(DASHED_LINE);
				line.setColor(color);
				canvas.add(line);
			}
			return;
		}

		// Lines.
		for (float[] q : l) {
			LineNode line = new LineNode(p[0], p[1], q[0], q[1]);
			line.setStroke(terminal ? DASHED_LINE : SOLID_LINE);
			line.setColor(color);
			canvas.add(line);
		}
	}
}