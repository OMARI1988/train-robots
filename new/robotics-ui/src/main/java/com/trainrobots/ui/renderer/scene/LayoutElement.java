/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

import javax.media.opengl.GL2;

import com.trainrobots.scenes.Layout;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.LayoutListener;
import com.trainrobots.scenes.Shape;

public class LayoutElement implements Element {

	private final Board board = new Board();
	private final Robot robot = new Robot();
	private float rotationX = 37;
	private float rotationY = -42.5f;
	private Layout layout;

	public LayoutElement() {
		this(new Layout());
	}

	public LayoutElement(Layout layout) {
		board.translation(0, 0, 22.5);
		robot.translation(-20, 0, 0);
		layout(layout);
	}

	public void layout(Layout layout) {

		// Subscribe to events.
		this.layout = layout;
		layout.listener(new LayoutListener() {

			public void gripperPositionChanged(Position position) {
				bindToGripperPosition();
			}

			public void gripperOpenChanged(boolean open) {
				bindToGripperOpen();
			}
		});

		// Bind gripper.
		bindToGripperPosition();
		bindToGripperOpen();

		// Bind shapes.
		board.clear();
		for (Shape shape : layout.shapes()) {
			board.add(shape);
		}
	}

	public void incrementRotation(float dx, float dy) {
		rotationX += dx;
		rotationY += dy;
	}

	public void render(GL2 gl) {

		// Rotate.
		gl.glRotatef(rotationX, 1, 0, 0);
		gl.glRotatef(rotationY, 0, 1, 0);

		// Render.
		board.render(gl);
		robot.render(gl);
	}

	private void bindToGripperPosition() {

		// Update the arm iteratively to avoid awkward angles.
		Position p = layout.gripper().position();
		robot.resetAngles();
		robot.computeAngles(board.getCellCenter(p.x(), 0, 5));
		robot.computeAngles(board.getCellCenter(p.x(), p.y(), 5));
		robot.computeAngles(board.getCellCenter(p.x(), p.y(), p.z()));
		board.shadow(p);
	}

	private void bindToGripperOpen() {
		robot.setGrasp(layout.gripper().open() ? 0.6 : 0.8);
	}
}