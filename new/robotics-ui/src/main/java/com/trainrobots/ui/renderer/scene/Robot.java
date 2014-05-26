/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

import static com.trainrobots.ui.renderer.math.Matrices.rotateZ;
import static com.trainrobots.ui.renderer.math.Matrices.translate;
import static com.trainrobots.ui.renderer.scene.ModelLoader.load;

import javax.media.opengl.GL2;

import com.trainrobots.ui.renderer.math.DownhillSimplex;
import com.trainrobots.ui.renderer.math.Matrix;
import com.trainrobots.ui.renderer.math.ObjectiveFunction;
import com.trainrobots.ui.renderer.math.Vector;

public class Robot implements Element {

	private final Model base = load("base");
	private final Model pivot = load("pivot");
	private final Model arm = load("arm");
	private final Model forearm = load("forearm");;
	private final Model wrist = load("wrist");
	private final Model tarsal = load("tarsal");
	private final Model handWrist = load("hand-wrist");
	private final Model finger1 = load("finger1");
	private final Model finger2 = load("finger2");

	private Vector translation = new Vector(0, 0, 0);

	private double wristRotateZ = 90;
	private double forearmRotateZ = 90;
	private double armRotateZ = -90;
	private double pivotRotateY = 0;
	private double tarsalRotateZ = 0;
	private double handRotateX = 0;
	private double grasp = 0;

	public void translation(double x, double y, double z) {
		translation = new Vector(x, y, z);
	}

	public void setGrasp(double graph) {
		this.grasp = graph;
	}

	public void resetAngles() {
		pivotRotateY = 0;
		armRotateZ = -90;
		forearmRotateZ = 90;
		wristRotateZ = 90;
		tarsalRotateZ = 0;
		handRotateX = 0;
	}

	public void computeAngles(Vector cellCenter) {

		// Offset the target so that the hand doesn't go through board.
		Vector target = new Vector(cellCenter.x(), Math.max(0,
				cellCenter.y() + 15), cellCenter.z());

		// Square of the distance of the arm end from the target.
		ObjectiveFunction objectiveFunction = p -> armEnd(p[3], p[2], p[1],
				p[0]).multiply(new Vector(0, 0, 0)).distanceSquared(target);

		// Use simplex to compute angles.
		double[] parameters = { wristRotateZ, forearmRotateZ, armRotateZ,
				pivotRotateY };
		DownhillSimplex simplex = new DownhillSimplex(0.1, 1000);
		simplex.minimize(parameters, objectiveFunction);
		wristRotateZ = parameters[0];
		forearmRotateZ = parameters[1];
		armRotateZ = parameters[2];
		pivotRotateY = parameters[3];

		// Position tarsal so that the hand always points down.
		Vector wrist = armEnd(pivotRotateY, armRotateZ, forearmRotateZ,
				wristRotateZ).multiply(new Vector(0, 0, 0));
		tarsalRotateZ = 0;
		Vector dir = calcEndEffector().subtract(wrist).normalize();
		tarsalRotateZ = degrees(Math.acos(new Vector(0, -1, 0).dot(dir)));
		Vector rotation1 = rotateZ(tarsalRotateZ).multiply(dir);
		Vector rotation2 = rotateZ(-tarsalRotateZ).multiply(dir);
		if (rotation2.y() < rotation1.y()) {
			tarsalRotateZ = -tarsalRotateZ;
		}

		// Position hand so that it always faces the same way.
		handRotateX = pivotRotateY + 90;
	}

	public void render(GL2 gl) {
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glPushMatrix();

		gl.glTranslated(translation.x(), translation.y(), translation.z());

		base.render(gl);

		gl.glTranslated(0, 16, 0);
		gl.glRotated(-pivotRotateY, 0, 1, 0);

		pivot.render(gl);

		gl.glTranslated(12.5, 3.3, 4.5);
		gl.glRotated(-armRotateZ, 0, 0, 1);

		arm.render(gl);

		gl.glTranslated(25, -0.5, -2);
		gl.glRotated(-forearmRotateZ, 0, 0, 1);

		forearm.render(gl);

		gl.glTranslated(18.5, 0, 1.6);
		gl.glRotated(-wristRotateZ, 0, 0, 1);

		wrist.render(gl);

		gl.glTranslated(12.1, 0, -2.6);
		gl.glRotated(-tarsalRotateZ, 0, 0, 1);

		tarsal.render(gl);

		gl.glTranslated(3, 0, 0);
		gl.glRotated(-handRotateX, 1, 0, 0);

		handWrist.render(gl);

		gl.glPushMatrix();
		gl.glTranslated(4.5, (1 - grasp) * 6, 0);
		finger1.render(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(4.5, (1 - grasp) * -6, 0);
		finger2.render(gl);
		gl.glPopMatrix();

		gl.glPopMatrix();
		gl.glPopAttrib();
	}

	private Vector calcEndEffector() {
		return armEnd(pivotRotateY, armRotateZ, forearmRotateZ, wristRotateZ)
				.rotateZ(tarsalRotateZ).translate(3, 0, 0).rotateX(handRotateX)
				.translate(12, 0, 0).multiply(new Vector(0, 0, 0));
	}

	private Matrix armEnd(double pivotRotateY, double armRotateZ,
			double forearmRotateZ, double wristRotateZ) {
		return translate(translation).translate(0, 16, 0).rotateY(pivotRotateY)
				.translate(12.5, 3.3, 4.5).rotateZ(armRotateZ)
				.translate(25, -0.5, -2).rotateZ(forearmRotateZ)
				.translate(18.5, 0, 1.6).rotateZ(wristRotateZ)
				.translate(12.1, 0, -2.6);
	}

	private static double degrees(double radians) {
		return radians * 57.2957796;
	}
}