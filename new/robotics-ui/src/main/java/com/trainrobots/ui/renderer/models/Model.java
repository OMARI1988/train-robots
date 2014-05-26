/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.models;

import javax.media.opengl.GL2;

import com.trainrobots.ui.renderer.math.Vector;

public class Model implements Element {

	private int m_npolyvtx; // number of vertices per polygon, either 3 or 4.
	private int m_npolys; // number of polygons.
	private double[] m_vtxs; // vertex data, size 3*m_nvtx
	private double[] m_facenrms; // face normal data, size 3*m_npolys
	private int[] m_faceinds; // face indices
	private final float[] ambient;
	private final float[] diffuse;

	public Model(boolean triPoly, double[] vtxs, int[] faceinds,
			float[] ambient, float[] diffuse) {

		m_npolyvtx = triPoly ? 3 : 4;
		m_npolys = faceinds.length / m_npolyvtx;

		m_vtxs = vtxs;
		m_faceinds = faceinds;

		m_facenrms = new double[m_npolys * 3];

		this.ambient = ambient;
		this.diffuse = diffuse;

		calcNormals();
	}

	public void render(GL2 gl) {

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, ambient, 0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 0);

		if (m_npolyvtx == 3) {
			gl.glBegin(GL2.GL_TRIANGLES);
		} else {
			gl.glBegin(GL2.GL_QUADS);
		}

		for (int i = 0, j = 0, k, idx; i < m_npolys; ++i, j += m_npolyvtx) {
			idx = i * 3;
			gl.glNormal3d(m_facenrms[idx], m_facenrms[idx + 1],
					m_facenrms[idx + 2]);

			for (k = 0; k < m_npolyvtx; ++k) {
				idx = m_faceinds[j + k] * 3;
				gl.glVertex3d(m_vtxs[idx], m_vtxs[idx + 1], m_vtxs[idx + 2]);
			}
		}

		gl.glEnd();
	}

	private void calcNormals() {
		for (int i = 0, j = 0, k = 0; i < m_npolys; ++i, j += m_npolyvtx, k += 3) {
			int i0 = m_faceinds[j] * 3;
			int i1 = m_faceinds[j + 1] * 3;
			int i2 = m_faceinds[j + 2] * 3;

			Vector e0 = new Vector(m_vtxs[i1] - m_vtxs[i0], m_vtxs[i1 + 1]
					- m_vtxs[i0 + 1], m_vtxs[i1 + 2] - m_vtxs[i0 + 2])
					.normalize();

			Vector e1 = new Vector(m_vtxs[i2] - m_vtxs[i0], m_vtxs[i2 + 1]
					- m_vtxs[i0 + 1], m_vtxs[i2 + 2] - m_vtxs[i0 + 2])
					.normalize();

			Vector n = e1.cross(e0).normalize();

			m_facenrms[k] = n.x();
			m_facenrms[k + 1] = n.y();
			m_facenrms[k + 2] = n.z();
		}
	}
}