/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization;

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
import com.trainrobots.ui.visualization.losr.LosrNode;
import com.trainrobots.ui.visualization.losr.LosrTree;
import com.trainrobots.ui.visualization.losr.Token;
import com.trainrobots.ui.visualization.themes.Theme;
import com.trainrobots.ui.visualization.themes.Themes;
import com.trainrobots.ui.visualization.visuals.Line;
import com.trainrobots.ui.visualization.visuals.LosrVisual;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Visual;

public class Visualizer {

	private final LosrTree tree;
	private static final Font FONT = new Font("Times New Roman", Font.PLAIN, 16);
	private static final Font FONT2 = new Font("Arial", Font.PLAIN, 16);
	private static final int HORIZONTAL_MARGIN = 10;
	private static final int VERTICAL_MARGIN = 20;
	private Map<Integer, LosrVisual> tokens = new HashMap<Integer, LosrVisual>();
	private final Visual canvas = new Visual();
	private final Theme theme;

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

	private List<LosrVisual> skipList = new ArrayList<LosrVisual>();

	public Visualizer(LosrTree tree, Theme theme) {
		this.tree = tree;
		this.theme = theme;
	}

	public VisualTree createVisualTree(VisualContext context) {

		// Tokens.
		for (Token token : tree.tokens()) {
			Text tag = new Text(context, token.text(),
					theme == Themes.Dark ? FONT2 : FONT,
					theme == Themes.Dark ? DEFAULT_COLOR2 : DEFAULT_COLOR);
			LosrVisual losrVisual = new LosrVisual(tag);
			losrVisual.width(tag.width());
			if (tokens.containsKey(token.id())) {
				throw new RoboticException("Duplicate token ID " + token.id());
			}
			tokens.put(token.id(), losrVisual);
		}

		// Layout.
		LosrVisual root = buildLosrVisual(context, tree.root());
		arrange(root);
		maxY = root.height();

		// Pack.
		pushLeaves(0, 0, root);
		canvas.pack();
		return new VisualTree(canvas);
	}

	private LosrVisual buildLosrVisual(VisualContext context, LosrNode losr) {

		// Node.
		String text = losr.tag();
		Color color = theme == Themes.Dark ? DEFAULT_COLOR2 : DEFAULT_COLOR;
		if (text.equals("spatial-relation")) {
			text = "sp-relation";
			color = theme == Themes.Dark ? SP_COLOR2 : SP_COLOR;
		} else if (text.equals("entity")) {
			color = theme == Themes.Dark ? ENTITY_COLOR2 : ENTITY_COLOR;
		} else if (text.equals("event")) {
			color = theme == Themes.Dark ? EVENT_COLOR2 : EVENT_COLOR;
		} else if (text.equals("type")) {
			if (((Type) losr.losr()).type() == Types.Reference) {
				text = "reference";
			}
		}
		Text tag = new Text(context, text, theme == Themes.Dark ? FONT2 : FONT,
				color);
		LosrVisual result = new LosrVisual(tag);

		// Pre-terminal?
		if (losr.count() == 0) {
			for (int i = losr.tokenStart(); i <= losr.tokenEnd(); i++) {
				LosrVisual child = tokens.get(i);
				if (child == null) {
					throw new RoboticException("Failed to find token: " + i
							+ " for node '" + losr.tag() + "'.'");
				}
				if (!child.tag().text().equals("Ø")) {
					for (int j = lastId + 1; j <= i - 1; j++) {
						LosrVisual skipped = tokens.get(j);
						if (skipped != null) {
							skipped.skip = true;
							skipped.tag().color(SKIP_COLOR);
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
				LosrVisual losrVisual = buildLosrVisual(context, child);
				for (LosrVisual skip : skipList) {
					result.add(skip);
				}
				skipList.clear();
				result.add(losrVisual);
			}
		}
		return result;
	}

	private void arrange(LosrVisual losrVisual) {

		// Recurse.
		for (LosrVisual child : losrVisual.losrChildren()) {
			arrange(child);
		}

		// Position children.
		float x = 0;
		float y = losrVisual.tag().height() + VERTICAL_MARGIN;
		for (LosrVisual child : losrVisual.losrChildren()) {
			child.y(y);
			child.x(x);
			x += child.width() + HORIZONTAL_MARGIN;
		}

		// Not a leaf?
		if (losrVisual.hasLayoutChildren()) {

			// Tag.
			LosrVisual first = null;
			LosrVisual last = null;
			for (LosrVisual child : losrVisual.losrChildren()) {
				if (child.skip) {
					continue;
				}
				if (first == null) {
					first = child;
				}
				last = child;
			}
			if (first != null) {
				Text tag = losrVisual.tag();
				float fx = first.x() + first.tag().x() + 0.5f
						* first.tag().width();
				float lx = last.x() + last.tag().x() + 0.5f
						* last.tag().width();
				tag.x(0.5f * (fx + lx) - 0.5f * tag.width());
			}
		}

		// Pack.
		losrVisual.pack();
	}

	private void pushLeaves(float x, float y, LosrVisual losrVisual) {

		// Offset.
		x += losrVisual.x();
		y += losrVisual.y();

		// Add.
		Text tag = losrVisual.tag();
		tag.x(x + tag.x());
		tag.y(y + tag.y());
		canvas.add(tag);

		// No children?
		if (!losrVisual.hasLayoutChildren()) {
			tag.y(maxY - tag.height());
		}

		// Recurse.
		else {

			// Tag.
			float[] p = { tag.x() + 0.5f * tag.width(), tag.y2() + 2 };
			List<float[]> l = new ArrayList<float[]>();
			boolean terminal = false;
			for (LosrVisual child : losrVisual.losrChildren()) {
				pushLeaves(x, y, child);
				if (!child.skip) {
					Text tag2 = child.tag();
					l.add(new float[] { tag2.x() + 0.5f * tag2.width(),
							tag2.y() - 2 });
					terminal = !child.hasLayoutChildren();
				}
			}

			// Lines.
			addLines(p, l, terminal);
		}
	}

	private void addLines(float[] p, List<float[]> l, boolean terminal) {

		// Triangle?
		Color color = theme == Themes.Dark ? DEFAULT_COLOR2 : DEFAULT_COLOR;
		if (terminal && l.size() >= 2) {
			float[] u = l.get(0);
			float[] v = l.get(l.size() - 1);
			canvas.add(new Line(p[0], p[1], u[0], u[1], color, DASHED_LINE));
			canvas.add(new Line(p[0], p[1], v[0], v[1], color, DASHED_LINE));
			canvas.add(new Line(u[0], u[1], v[0], v[1], color, DASHED_LINE));
			return;
		}

		// Lines.
		for (float[] q : l) {
			Stroke stroke = terminal ? DASHED_LINE : SOLID_LINE;
			canvas.add(new Line(p[0], p[1], q[0], q[1], color, stroke));
		}
	}
}