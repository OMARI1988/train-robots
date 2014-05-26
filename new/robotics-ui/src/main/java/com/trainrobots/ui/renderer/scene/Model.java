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

	private final int npolyvtx; // number of vertices per polygon (3 or 4)
	private final int npolys; // number of polygons.
	private final double[] vtxs; // vertex data
	private final double[] facenrms; // face normal data
	private final int[] faceinds; // face indices
	private final float[] ambient;
	private final float[] diffuse;

	public Model(boolean triPoly, double[] vtxs, int[] faceinds,
			float[] ambient, float[] diffuse) {

		this.npolyvtx = triPoly ? 3 : 4;
		this.npolys = faceinds.length / npolyvtx;
		this.vtxs = vtxs;
		this.faceinds = faceinds;
		this.facenrms = new double[npolys * 3];
		this.ambient = ambient;
		this.diffuse = diffuse;
		calcNormals();
	}

	public void render(GL2 gl) {

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, ambient, 0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 0);

		if (npolyvtx == 3) {
			gl.glBegin(GL2.GL_TRIANGLES);
		} else {
			gl.glBegin(GL2.GL_QUADS);
		}

		for (int i = 0, j = 0, k, idx; i < npolys; ++i, j += npolyvtx) {
			idx = i * 3;
			gl.glNormal3d(facenrms[idx], facenrms[idx + 1], facenrms[idx + 2]);

			for (k = 0; k < npolyvtx; ++k) {
				idx = faceinds[j + k] * 3;
				gl.glVertex3d(vtxs[idx], vtxs[idx + 1], vtxs[idx + 2]);
			}
		}

		gl.glEnd();
	}

	private void calcNormals() {
		for (int i = 0, j = 0, k = 0; i < npolys; ++i, j += npolyvtx, k += 3) {
			int i0 = faceinds[j] * 3;
			int i1 = faceinds[j + 1] * 3;
			int i2 = faceinds[j + 2] * 3;

			Vector e0 = new Vector(vtxs[i1] - vtxs[i0], vtxs[i1 + 1]
					- vtxs[i0 + 1], vtxs[i1 + 2] - vtxs[i0 + 2]).normalize();

			Vector e1 = new Vector(vtxs[i2] - vtxs[i0], vtxs[i2 + 1]
					- vtxs[i0 + 1], vtxs[i2 + 2] - vtxs[i0 + 2]).normalize();

			Vector n = e1.cross(e0).normalize();

			facenrms[k] = n.x();
			facenrms[k + 1] = n.y();
			facenrms[k + 2] = n.z();
		}
	}
}