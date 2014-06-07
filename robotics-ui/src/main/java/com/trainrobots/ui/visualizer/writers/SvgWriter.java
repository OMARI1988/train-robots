/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualizer.writers;

import java.awt.BasicStroke;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import com.trainrobots.RoboticException;
import com.trainrobots.ui.GraphicsRenderer;
import com.trainrobots.ui.visualizer.Visualizer;
import com.trainrobots.ui.visualizer.losr.LosrTree;
import com.trainrobots.ui.visualizer.visuals.LineNode;
import com.trainrobots.ui.visualizer.visuals.TextNode;
import com.trainrobots.ui.visualizer.visuals.VisualContext;
import com.trainrobots.ui.visualizer.visuals.VisualNode;
import com.trainrobots.ui.visualizer.visuals.VisualTree;

public class SvgWriter implements GraphicsRenderer {

	private final LosrTree tree;
	private final boolean darkTheme;

	public SvgWriter(LosrTree tree) {
		this(tree, false);
	}

	public SvgWriter(LosrTree tree, boolean darkTheme) {
		this.tree = tree;
		this.darkTheme = darkTheme;
	}

	public void renderToFile(String filename) {
		try (FileOutputStream stream = new FileOutputStream(filename)) {
			writeSvg(stream);
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	public byte[] renderToArray() {
		try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
			writeSvg(stream);
			return stream.toByteArray();
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	private void writeSvg(OutputStream stream) {

		// Visual tree.
		Visualizer visualizer = new Visualizer(tree, darkTheme);
		VisualContext visualContext = VisualContext.defaultContext();
		VisualTree visualTree = visualizer.createVisualTree(visualContext);

		// Write.
		try (PrintStream out = new PrintStream(stream)) {
			out.println("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
			write(out, 0, 0, visualTree.getRoot());
			out.println("</svg>");
		}
	}

	private static void write(PrintStream out, float x, float y, VisualNode node) {

		x += node.getX();
		y += node.getY();

		if (node instanceof TextNode) {
			writeText(out, x, y, (TextNode) node);
		} else if (node instanceof LineNode) {
			writeLine(out, x, y, (LineNode) node);
		}

		if (node.getChildCount() > 0) {
			for (VisualNode child : node) {
				write(out, x, y, child);
			}
		}
	}

	private static void writeText(PrintStream out, float x, float y,
			TextNode node) {

		out.print("<text x=\"");
		out.print(x);
		out.print("\" y=\"");
		out.print(y - node.getTextOffsetY());
		out.print("\" font-family=\"");
		out.print(node.getFont().getFontName());
		out.print("\" font-size=\"");
		out.print(node.getFont().getSize());
		out.print("\" fill=\"");
		out.print("rgb(" + node.getColor().getRed() + ", "
				+ node.getColor().getGreen() + ", " + node.getColor().getBlue()
				+ ")");
		out.print("\">");
		out.print(node.getText());
		out.println("</text>");
	}

	private static void writeLine(PrintStream out, float x, float y,
			LineNode node) {

		BasicStroke stroke = (BasicStroke) node.getStroke();
		float[] da = stroke.getDashArray();

		out.print("<line x1=\"");
		out.print(node.getX());
		out.print("\" y1=\"");
		out.print(node.getY());
		out.print("\" x2=\"");
		out.print(node.getX2());
		out.print("\" y2=\"");
		out.print(node.getY2());
		out.print("\" stroke-width=\"");
		out.print(stroke.getLineWidth());
		out.print("\" stroke=\"");
		out.print("black");
		if (da != null) {
			out.print("\" stroke-dasharray=\"");
			out.print(da[0]);
			out.print(", ");
			out.print(da[1]);
		}
		out.println("\"/>");
	}
}