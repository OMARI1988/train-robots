/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.writers;

import java.awt.BasicStroke;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import com.trainrobots.RoboticException;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.GraphicsRenderer;
import com.trainrobots.ui.visualization.VisualContext;
import com.trainrobots.ui.visualization.VisualTree;
import com.trainrobots.ui.visualization.Visualizer;
import com.trainrobots.ui.visualization.themes.Themes;
import com.trainrobots.ui.visualization.visuals.Line;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Visual;

public class SvgWriter implements GraphicsRenderer {

	private final Command command;

	public SvgWriter(Command command) {
		this.command = command;
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
		Visualizer visualizer = new Visualizer(command, Themes.Simple);
		VisualContext visualContext = VisualContext.defaultContext();
		VisualTree visualTree = visualizer.createVisualTree(visualContext);

		// Write.
		try (PrintStream out = new PrintStream(stream)) {
			out.println("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
			write(out, 0, 0, visualTree.root());
			out.println("</svg>");
		}
	}

	private static void write(PrintStream out, float x, float y, Visual node) {

		x += node.x();
		y += node.y();

		if (node instanceof Text) {
			writeText(out, x, y, (Text) node);
		} else if (node instanceof Line) {
			writeLine(out, x, y, (Line) node);
		}

		if (node.count() > 0) {
			for (Visual child : node) {
				write(out, x, y, child);
			}
		}
	}

	private static void writeText(PrintStream out, float x, float y, Text node) {

		out.print("<text x=\"");
		out.print(x);
		out.print("\" y=\"");
		out.print(y - node.textOffsetY());
		out.print("\" font-family=\"");
		out.print(node.font().getFontName());
		out.print("\" font-size=\"");
		out.print(node.font().getSize());
		out.print("\" fill=\"");
		out.print("rgb(" + node.color().getRed() + ", "
				+ node.color().getGreen() + ", " + node.color().getBlue() + ")");
		out.print("\">");
		out.print(node.text());
		out.println("</text>");
	}

	private static void writeLine(PrintStream out, float x, float y, Line node) {

		BasicStroke stroke = (BasicStroke) node.stroke();
		float[] da = stroke.getDashArray();

		out.print("<line x1=\"");
		out.print(node.x());
		out.print("\" y1=\"");
		out.print(node.y());
		out.print("\" x2=\"");
		out.print(node.x2());
		out.print("\" y2=\"");
		out.print(node.y2());
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