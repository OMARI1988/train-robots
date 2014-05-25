/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer;

import static com.trainrobots.ui.renderer.math.Matrices.rotateZ;
import static com.trainrobots.ui.renderer.math.Matrices.translate;
import static com.trainrobots.ui.renderer.models.Model.load;

import javax.media.opengl.GL2;

import com.trainrobots.ui.renderer.math.DownhillSimplex;
import com.trainrobots.ui.renderer.math.ObjectiveFunction;
import com.trainrobots.ui.renderer.math.Vector;
import com.trainrobots.ui.renderer.models.Model;

public class Robot {

	private final Model base = load("base");
	private final Model pivot = load("pivot");
	private final Model arm = load("arm");
	private final Model forearm = load("forearm");;
	private final Model wrist = load("wrist");
	private final Model tarsal = load("tarsal");
	private final Model hand_wrist = load("hand-wrist");
	private final Model finger1 = load("finger1");
	private final Model finger2 = load("finger2");
	private Vector translate = new Vector(0.0, 0.0, 0.0);
	private double wrist_z_rotation = 90.0;
	private double forearm_z_rotation = 90.0;
	private double arm_z_rotation = -90.0;
	private double pivot_y_rotation = 0.0;
	private double tarsal_z_rotation = 0.0;
	private double hand_x_rotation = 0.0;
	private double grasp = 0.0;

	public void setTranslate(double tx, double ty, double tz) {
		translate = new Vector(tx, ty, tz);
	}

	public void setGrasp(double g) {
		grasp = g;
	}

	private Vector calcEndEffector() {
		return translate(translate).translate(0.0, 16.0, 0.0)
				.rotateY(pivot_y_rotation).translate(12.5, 3.3, 4.5)
				.rotateZ(arm_z_rotation).translate(25.0, -0.5, -2.0)
				.rotateZ(forearm_z_rotation).translate(18.5, 0.0, 1.6)
				.rotateZ(wrist_z_rotation).translate(12.1, 0.0, -2.6)
				.rotateZ(tarsal_z_rotation).translate(3.0, 0.0, 0)
				.rotateX(hand_x_rotation).translate(12.0, 0.0, 0.0)
				.multiply(new Vector(0, 0, 0));
	}

	private static double degrees(double radians) {
		return radians * 57.2957796;
	}

	public void resetAngles() {
		pivot_y_rotation = 0.0;
		arm_z_rotation = -90.0;
		forearm_z_rotation = 90.0;
		wrist_z_rotation = 90.0;
		tarsal_z_rotation = 0.0;
		hand_x_rotation = 0.0;
	}

	public void computeAngles(Vector target) {

		// offset so that hand doesn't go through board
		Vector tgt = new Vector(target.x(), Math.max(0.0, target.y() + 15.0),
				target.z());

		double[] parameters = { wrist_z_rotation, forearm_z_rotation,
				arm_z_rotation, pivot_y_rotation };
		DownhillSimplex ds = new DownhillSimplex(0.1, 1000);

		// Return the square of the distance of the arm end from the target.
		ObjectiveFunction objectiveFunction = p -> translate(translate)
				.translate(0.0, 16.0, 0.0).rotateY(p[3])
				.translate(12.5, 3.3, 4.5).rotateZ(p[2])
				.translate(25.0, -0.5, -2.0).rotateZ(p[1])
				.translate(18.5, 0.0, 1.6).rotateZ(p[0])
				.translate(12.1, 0.0, -2.6).multiply(new Vector(0, 0, 0))
				.distanceSquared(tgt);

		// Compute angles.
		ds.minimize(parameters, objectiveFunction);
		wrist_z_rotation = parameters[0];
		forearm_z_rotation = parameters[1];
		arm_z_rotation = parameters[2];
		pivot_y_rotation = parameters[3];

		// Compute tarsal so that the hand always points down.
		Vector wrist = translate(translate).translate(0.0, 16.0, 0.0)
				.rotateY(pivot_y_rotation).translate(12.5, 3.3, 4.5)
				.rotateZ(arm_z_rotation).translate(25.0, -0.5, -2.0)
				.rotateZ(forearm_z_rotation).translate(18.5, 0.0, 1.6)
				.rotateZ(wrist_z_rotation).translate(12.1, 0.0, -2.6)
				.multiply(new Vector(0, 0, 0));

		tarsal_z_rotation = 0.0;
		Vector dir = calcEndEffector().subtract(wrist).normalize();
		tarsal_z_rotation = degrees(Math.acos(new Vector(0.0, -1.0, 0.0)
				.dot(dir)));

		Vector rotation1 = rotateZ(tarsal_z_rotation).multiply(dir);
		Vector rotation2 = rotateZ(-tarsal_z_rotation).multiply(dir);

		if (rotation2.y() < rotation1.y()) {
			tarsal_z_rotation = -tarsal_z_rotation;
		}

		// compute hand so that it always faces the same way
		hand_x_rotation = pivot_y_rotation + 90.0;
	}

	public void render(GL2 gl, int nrm) {
		gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
		gl.glPushMatrix();

		gl.glTranslated(translate.x(), translate.y(), translate.z());

		base.render(gl, nrm);

		gl.glTranslated(0.0, 16.0, 0.0);
		gl.glRotated(-pivot_y_rotation, 0.0, 1.0, 0.0);

		pivot.render(gl, nrm);

		gl.glTranslated(12.5, 3.3, 4.5);
		gl.glRotated(-arm_z_rotation, 0.0, 0.0, 1.0);

		arm.render(gl, nrm);

		gl.glTranslated(25.0, -0.5, -2.0);
		gl.glRotated(-forearm_z_rotation, 0.0, 0.0, 1.0);

		forearm.render(gl, nrm);

		gl.glTranslated(18.5, 0.0, 1.6);
		gl.glRotated(-wrist_z_rotation, 0.0, 0.0, 1.0);

		wrist.render(gl, nrm);

		gl.glTranslated(12.1, 0.0, -2.6);
		gl.glRotated(-tarsal_z_rotation, 0.0, 0.0, 1.0);

		tarsal.render(gl, nrm);

		gl.glTranslated(3.0, 0.0, 0.0);
		gl.glRotated(-hand_x_rotation, 1.0, 0.0, 0.0);

		hand_wrist.render(gl, nrm);

		gl.glPushMatrix();
		gl.glTranslated(4.5, (1.0 - grasp) * 6.0, 0);
		finger1.render(gl, nrm);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslated(4.5, (1.0 - grasp) * -6.0, 0);
		finger2.render(gl, nrm);
		gl.glPopMatrix();

		gl.glPopMatrix();
		gl.glPopAttrib();
	}
}
