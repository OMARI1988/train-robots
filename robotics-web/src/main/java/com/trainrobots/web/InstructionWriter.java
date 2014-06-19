/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.web;

import java.util.HashMap;
import java.util.Map;

import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Types;
import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Shape;

public class InstructionWriter {

	private static Map<Colors, Integer> colors = new HashMap<>();
	private final StringBuilder text = new StringBuilder();
	private final Layout layout;

	static {
		colors.put(Colors.Blue, 0);
		colors.put(Colors.Cyan, 1);
		colors.put(Colors.Red, 2);
		colors.put(Colors.Yellow, 3);
		colors.put(Colors.Green, 4);
		colors.put(Colors.Magenta, 5);
		colors.put(Colors.White, 6);
		colors.put(Colors.Gray, 7);
	}

	public InstructionWriter(Layout layout) {
		this.layout = layout;
	}

	public String write() {

		// Highlight.
		Position gripper = layout.gripper().position();
		highlight(gripper);

		// Shapes.
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < 8; x++) {
				for (int z = 0; z < 8; z++) {
					Shape shape = layout.shape(new Position(x, y, z));
					if (shape != null) {
						shape(shape);
					}
				}
			}
		}

		// Gripper.
		gripper(gripper);
		text.append(']');
		return text.toString();
	}

	private void highlight(Position position) {
		start();
		text.append("[1,");
		text.append(position.x());
		text.append(',');
		text.append(position.y());
		text.append(']');
	}

	private void shape(Shape shape) {
		start();
		text.append(shape.type() == Types.Cube ? "[2," : "[3,");
		Position position = shape.position();
		text.append(position.x());
		text.append(',');
		text.append(position.y());
		text.append(',');
		text.append(position.z());
		text.append(',');
		text.append(colors.get(shape.color()));
		text.append(']');
	}

	private void gripper(Position position) {
		start();
		text.append("[4,");
		text.append(position.x());
		text.append(',');
		text.append(position.y());
		text.append(',');
		text.append(position.z());
		text.append(']');
	}

	private void start() {
		text.append(text.length() == 0 ? '[' : ',');
	}
}