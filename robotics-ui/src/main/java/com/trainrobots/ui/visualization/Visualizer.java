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
import java.util.List;

import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.visualization.themes.Theme;
import com.trainrobots.ui.visualization.visuals.Frame;
import com.trainrobots.ui.visualization.visuals.Line;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Visual;
import com.trainrobots.ui.visualization.visuals.VisualTree;

public class Visualizer {

	private static final int HORIZONTAL_FRAME_MARGIN = 10;
	private static final int VERTICAL_FRAME_MARGIN = 20;
	private static final Stroke SOLID_LINE = new BasicStroke();
	private static final Stroke DASHED_LINE = new BasicStroke(1f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[] {
					1.5f, 3 }, 0);

	private final List<Frame> skipList = new ArrayList<Frame>();
	private final Visual canvas = new Visual();
	private final Command command;
	private final Theme theme;
	private int lastId;

	public Visualizer(Command command, Theme theme) {
		this.command = command;
		this.theme = theme;
	}

	public VisualTree createVisualTree(VisualContext context) {

		// Layout.
		Frame frame = buildFrame(context, command.losr());

		// Trailing skipped tokens.
		for (int i = lastId + 1; i <= command.tokens().count(); i++) {
			frame.add(skippedToken(context, i));
		}

		// Arrange.
		arrange(frame);
		pushLeaves(0, 0, frame.height(), frame);
		canvas.pack();
		return new VisualTree(canvas);
	}

	private Frame buildFrame(VisualContext context, Losr losr) {

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
		} else if (losr instanceof Type
				&& ((Type) losr).type() == Types.Reference) {
			text = "reference";
		}
		Text tag = new Text(context, text, theme.font(), color);
		Frame frame = new Frame(tag);

		// Terminal?
		if (losr instanceof Terminal) {
			Terminal terminal = (Terminal) losr;

			// Ellipsis.
			if (terminal.context() == null) {
				frame.add(buildToken(context, "Ø"));
			}

			// Add tokens.
			else {
				int tokenStart = terminal.context().start();
				int tokenEnd = terminal.context().end();
				for (int i = tokenStart; i <= tokenEnd; i++) {

					// Skipped tokens.
					for (int j = lastId + 1; j <= i - 1; j++) {
						skipList.add(skippedToken(context, j));
					}

					// Add token.
					frame.add(buildToken(context, i));
					lastId = i;
				}
			}
		}

		// Non-terminal.
		else {
			for (Losr child : losr) {
				Frame childFrame = buildFrame(context, child);
				for (Frame skip : skipList) {
					frame.add(skip);
				}
				skipList.clear();
				frame.add(childFrame);
			}
		}
		return frame;
	}

	private Frame skippedToken(VisualContext context, int id) {
		Frame token = buildToken(context, id);
		token.skip(true);
		token.tag().color(theme.skip());
		return token;
	}

	private Frame buildToken(VisualContext context, int id) {
		return buildToken(context, command.tokens().get(id - 1).context()
				.text().toLowerCase());
	}

	private Frame buildToken(VisualContext context, String text) {
		Text tag = new Text(context, text, theme.font(), theme.foreground());
		Frame frame = new Frame(tag);
		frame.width(tag.width());
		return frame;
	}

	private void arrange(Frame frame) {

		// Recurse.
		for (Frame child : frame.frames()) {
			arrange(child);
		}

		// Position children.
		float x = 0;
		float y = frame.tag().height() + VERTICAL_FRAME_MARGIN;
		for (Frame child : frame.frames()) {
			child.x(x);
			child.y(y);
			x += child.width() + HORIZONTAL_FRAME_MARGIN;
		}

		// Not a leaf?
		if (!frame.leaf()) {

			// Extent.
			Frame first = null;
			Frame last = null;
			for (Frame child : frame.frames()) {
				if (!child.skip()) {
					if (first == null) {
						first = child;
					}
					last = child;
				}
			}

			// Position tag.
			if (first != null) {
				Text tag = frame.tag();
				float fx = first.x() + first.tag().x() + 0.5f
						* first.tag().width();
				float lx = last.x() + last.tag().x() + 0.5f
						* last.tag().width();
				tag.x(0.5f * (fx + lx) - 0.5f * tag.width());
			}
		}

		// Pack.
		frame.pack();
	}

	private void pushLeaves(float x, float y, float maxY, Frame frame) {

		// Offset.
		x += frame.x();
		y += frame.y();

		// Add.
		Text tag = frame.tag();
		tag.x(x + tag.x());
		tag.y(y + tag.y());
		canvas.add(tag);

		// Leaf?
		if (frame.leaf()) {
			tag.y(maxY - tag.height());
		}

		// Recurse.
		else {

			// Tag.
			float[] p = { tag.x() + 0.5f * tag.width(), tag.y2() + 2 };
			List<float[]> l = new ArrayList<float[]>();
			boolean terminal = false;
			for (Frame child : frame.frames()) {
				pushLeaves(x, y, maxY, child);
				if (!child.skip()) {
					Text tag2 = child.tag();
					l.add(new float[] { tag2.x() + 0.5f * tag2.width(),
							tag2.y() - 2 });
					terminal = child.leaf();
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