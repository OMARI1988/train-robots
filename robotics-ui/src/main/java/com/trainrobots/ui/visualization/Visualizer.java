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

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Terminal;
import com.trainrobots.ui.visualization.visuals.Detail;
import com.trainrobots.ui.visualization.visuals.Frame;
import com.trainrobots.ui.visualization.visuals.Header;
import com.trainrobots.ui.visualization.visuals.Line;
import com.trainrobots.ui.visualization.visuals.Token;
import com.trainrobots.ui.visualization.visuals.Visual;
import com.trainrobots.ui.visualization.visuals.VisualTree;

public class Visualizer {

	private static final int HORIZONTAL_FRAME_MARGIN = 10;
	private static final int VERTICAL_FRAME_MARGIN = 20;
	private static final Stroke SOLID_LINE = new BasicStroke();
	private static final Stroke DASHED_LINE = new BasicStroke(1f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[] {
					1.5f, 3 }, 0);

	private final PartialTree partialTree;

	private final List<Frame> skipList = new ArrayList<Frame>();
	private final Visual canvas = new Visual();
	private int lastId;

	public Visualizer(PartialTree partialTree) {
		this.partialTree = partialTree;
	}

	public VisualTree createVisualTree(VisualContext context) {

		// Partial tree.
		Visual root = new Visual();
		for (Losr item : partialTree.items()) {
			Frame frame = frame(context, item);
			for (Frame skip : skipList) {
				root.add(skip);
			}
			skipList.clear();
			root.add(frame);
		}

		// Trailing skipped tokens.
		int tokenCount = partialTree.tokens().count();
		for (int i = lastId + 1; i <= tokenCount; i++) {
			root.add(token(context, i, true));
		}

		// Arrange.
		arrange(root);

		// Push leaves.
		for (Visual child : root) {
			pushLeaves(context, 0, 0, root.height(), (Frame) child);
		}

		// Pack.
		canvas.pack();
		return new VisualTree(canvas);
	}

	private Frame frame(VisualContext context, Losr losr) {

		// Tag.
		Visual tag = tag(context, losr);
		Frame frame = new Frame(tag, false);

		// Terminal?
		if (losr instanceof Terminal) {
			Terminal terminal = (Terminal) losr;

			// Ellipsis.
			if (terminal.context() == null) {
				frame.add(token(context, 0, "\u00D8", false));
			}

			// Add tokens.
			else {
				int tokenStart = terminal.context().start();
				int tokenEnd = terminal.context().end();
				for (int i = tokenStart; i <= tokenEnd; i++) {

					// Skipped tokens.
					for (int j = lastId + 1; j <= i - 1; j++) {
						skipList.add(token(context, j, true));
					}

					// Add token.
					frame.add(token(context, i, false));
					lastId = i;
				}
			}
		}

		// Non-terminal.
		else {
			for (Losr child : losr) {
				Frame childFrame = frame(context, child);
				for (Frame skip : skipList) {
					frame.add(skip);
				}
				skipList.clear();
				frame.add(childFrame);
			}
		}
		return frame;
	}

	private Visual tag(VisualContext context, Losr losr) {

		// Header.
		Header header = Header.from(context, losr);

		// Details?
		Items<Detail> details = header.details();
		if (details == null || details.count() == 0) {
			return header;
		}

		// Stack vertically.
		Visual tag = new Visual();
		tag.add(header);
		float y = header.height();
		for (Detail detail : details) {
			tag.add(detail);
			detail.y(y);
			y += detail.height();
		}

		// Center horizontally.
		tag.pack();
		int size = tag.count();
		for (int i = 0; i < size; i++) {
			Visual visual = tag.get(i);
			visual.x(0.5f * (tag.width() - visual.width()));
		}
		return tag;
	}

	private Frame token(VisualContext context, int id, boolean skip) {
		String text = partialTree.tokens().get(id - 1).context().text();
		return token(context, id, text.toLowerCase(), skip);
	}

	private Frame token(VisualContext context, int id, String text, boolean skip) {
		Token token = new Token(context, id, text, skip);
		Frame frame = new Frame(token, skip);
		frame.width(token.width());
		return frame;
	}

	private void arrange(Visual root) {

		// Recurse.
		for (Visual child : root) {
			arrange((Frame) child);
		}

		// Position children.
		float x = 0;
		for (Visual child : root) {
			child.x(x);
			x += child.width() + HORIZONTAL_FRAME_MARGIN;
		}
		root.pack();
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
				Visual tag = frame.tag();
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

	private void pushLeaves(VisualContext context, float x, float y,
			float maxY, Frame frame) {

		// Offset.
		x += frame.x();
		y += frame.y();

		// Add to canvas.
		Visual tag = frame.tag();
		tag.x(x + tag.x());
		tag.y(y + tag.y());
		int size = tag.count();
		if (size == 0) {
			canvas.add(tag);
		} else {
			for (int i = 0; i < size; i++) {
				Visual child = tag.get(i);
				child.x(tag.x() + child.x());
				child.y(tag.y() + child.y());
				canvas.add(child);
			}
		}

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
				pushLeaves(context, x, y, maxY, child);
				if (!child.skip()) {
					Visual tag2 = child.tag();
					l.add(new float[] { tag2.x() + 0.5f * tag2.width(),
							tag2.y() - 2 });
					terminal = child.leaf();
				}
			}

			// Lines.
			addLines(context.theme().foreground(), p, l, terminal);
		}
	}

	private void addLines(Color color, float[] p, List<float[]> l,
			boolean terminal) {

		// Triangle?
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