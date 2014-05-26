/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

import javax.media.opengl.GL2;

import com.trainrobots.ui.renderer.math.Vector;

public class Model implements Element {

	private final int polygonVertices;
	private final int polygons;
	private final double[] vertices;
	private final double[] normals;
	private final int[] indices;
	private final float[] ambient;
	private final float[] diffuse;

	public Model(int polygonVertices, double[] vertices, int[] indices,
			float[] ambient, float[] diffuse) {

		this.polygonVertices = polygonVertices;
		this.polygons = indices.length / polygonVertices;
		this.vertices = vertices;
		this.indices = indices;
		this.normals = new double[polygons * 3];
		this.ambient = ambient;
		this.diffuse = diffuse;

		calculateNormals();
	}

	public void render(GL2 gl) {

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, ambient, 0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 0);

		if (polygonVertices == 3) {
			gl.glBegin(GL2.GL_TRIANGLES);
		} else {
			gl.glBegin(GL2.GL_QUADS);
		}

		for (int i = 0, j = 0, k, idx; i < polygons; ++i, j += polygonVertices) {
			idx = i * 3;
			gl.glNormal3d(normals[idx], normals[idx + 1], normals[idx + 2]);

			for (k = 0; k < polygonVertices; ++k) {
				idx = indices[j + k] * 3;
				gl.glVertex3d(vertices[idx], vertices[idx + 1],
						vertices[idx + 2]);
			}
		}

		gl.glEnd();
	}

	private void calculateNormals() {
		for (int i = 0, j = 0, k = 0; i < polygons; ++i, j += polygonVertices, k += 3) {
			int i0 = indices[j] * 3;
			int i1 = indices[j + 1] * 3;
			int i2 = indices[j + 2] * 3;

			Vector e0 = new Vector(vertices[i1] - vertices[i0],
					vertices[i1 + 1] - vertices[i0 + 1], vertices[i1 + 2]
							- vertices[i0 + 2]).normalize();

			Vector e1 = new Vector(vertices[i2] - vertices[i0],
					vertices[i2 + 1] - vertices[i0 + 1], vertices[i2 + 2]
							- vertices[i0 + 2]).normalize();

			Vector n = e1.cross(e0).normalize();

			normals[k] = n.x();
			normals[k + 1] = n.y();
			normals[k + 2] = n.z();
		}
	}
}