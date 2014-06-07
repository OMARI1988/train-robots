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
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.visualization.themes.Theme;
import com.trainrobots.ui.visualization.visuals.Line;
import com.trainrobots.ui.visualization.visuals.LosrVisual;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Visual;
import com.trainrobots.ui.visualization.visuals.VisualTree;

public class Visualizer {

	private static final int HORIZONTAL_MARGIN = 10;
	private static final int VERTICAL_MARGIN = 20;
	private static final Stroke SOLID_LINE = new BasicStroke();
	private static final Stroke DASHED_LINE = new BasicStroke(1f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[] {
					1.5f, 3 }, 0);

	private final Map<Integer, LosrVisual> tokens = new HashMap<Integer, LosrVisual>();
	private final List<LosrVisual> skipList = new ArrayList<LosrVisual>();
	private final Visual canvas = new Visual();
	private final Command command;
	private final Theme theme;
	private int lastId;

	public Visualizer(Command command, Theme theme) {
		this.command = command;
		this.theme = theme;
	}

	public VisualTree createVisualTree(VisualContext context) {

		// Tokens.
		for (Terminal token : command.tokens()) {
			buildTokenVisual(context, token.context().text().toLowerCase());
		}

		// Layout.
		LosrVisual root = buildLosrVisual(context, command.losr());
		arrange(root);

		// Pack.
		pushLeaves(0, 0, root.height(), root);
		canvas.pack();
		return new VisualTree(canvas);
	}

	private LosrVisual buildLosrVisual(VisualContext context, Losr losr) {

		// Tag.
		String text = losr.name();
		Color color = theme.foreground();
		if (losr instanceof SpatialRelation) {
			text = "sp-relation";
			color = theme.spatialRelation();
		} else if (losr instanceof Entity) {
			color = theme.entity();
		} else if (losr instanceof Event) {
			color = theme.event();
		} else if (losr instanceof Type) {
			if (((Type) losr).type() == Types.Reference) {
				text = "reference";
			}
		}
		Text tag = new Text(context, text, theme.font(), color);
		LosrVisual result = new LosrVisual(tag);

		// Terminal.
		if (losr instanceof Terminal) {
			Terminal terminal = (Terminal) losr;

			// Ellipsis.
			if (terminal.context() == null) {
				result.add(buildTokenVisual(context, "Ø"));
			}

			// Token.
			else {
				int tokenStart = terminal.context().start();
				int tokenEnd = terminal.context().end();
				for (int i = tokenStart; i <= tokenEnd; i++) {
					LosrVisual child = tokens.get(i);
					for (int j = lastId + 1; j <= i - 1; j++) {
						LosrVisual skipped = tokens.get(j);
						if (skipped != null) {
							skipped.skip(true);
							skipped.tag().color(theme.skip());
							skipList.add(skipped);
						}
					}
					lastId = i;
					result.add(child);
				}
			}
		}

		// Attach.
		if (!result.hasLosrChildren()) {
			for (Losr child : losr) {
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

	private LosrVisual buildTokenVisual(VisualContext context, String text) {
		Text tag = new Text(context, text, theme.font(), theme.foreground());
		LosrVisual losrVisual = new LosrVisual(tag);
		losrVisual.width(tag.width());
		tokens.put(tokens.size() + 1, losrVisual);
		return losrVisual;
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
		if (losrVisual.hasLosrChildren()) {

			// Tag.
			LosrVisual first = null;
			LosrVisual last = null;
			for (LosrVisual child : losrVisual.losrChildren()) {
				if (child.skip()) {
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

	private void pushLeaves(float x, float y, float maxY, LosrVisual losrVisual) {

		// Offset.
		x += losrVisual.x();
		y += losrVisual.y();

		// Add.
		Text tag = losrVisual.tag();
		tag.x(x + tag.x());
		tag.y(y + tag.y());
		canvas.add(tag);

		// No children?
		if (!losrVisual.hasLosrChildren()) {
			tag.y(maxY - tag.height());
		}

		// Recurse.
		else {

			// Tag.
			float[] p = { tag.x() + 0.5f * tag.width(), tag.y2() + 2 };
			List<float[]> l = new ArrayList<float[]>();
			boolean terminal = false;
			for (LosrVisual child : losrVisual.losrChildren()) {
				pushLeaves(x, y, maxY, child);
				if (!child.skip()) {
					Text tag2 = child.tag();
					l.add(new float[] { tag2.x() + 0.5f * tag2.width(),
							tag2.y() - 2 });
					terminal = !child.hasLosrChildren();
				}
			}

			// Lines.
			addLines(p, l, terminal);
		}
	}

	private void addLines(float[] p, List<float[]> l, boolean terminal) {

		// Triangle?
		Color color = theme.foreground();
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