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

public class Model {

	public static final int FACE_NORMALS = 0;

	public static Model load(String name) {
		return ModelLoader.load(name);
	}

	public static Model pyramid(double w, double h, double d) {
		Model model = new Model(true, 5, 6);

		double[] vtxs = model.getVertices();

		vtxs[0] = 0.0;
		vtxs[1] = 0.0;
		vtxs[2] = 0.0;
		vtxs[3] = w / 2;
		vtxs[4] = h;
		vtxs[5] = -d / 2;
		vtxs[6] = w;
		vtxs[7] = 0.0;
		vtxs[8] = 0.0;
		vtxs[9] = w;
		vtxs[10] = 0.0;
		vtxs[11] = -d;
		vtxs[12] = 0.0;
		vtxs[13] = 0.0;
		vtxs[14] = -d;

		int[] inds = model.getFaceInds();
		inds[0] = 0;
		inds[1] = 2;
		inds[2] = 1;
		inds[3] = 2;
		inds[4] = 3;
		inds[5] = 1;
		inds[6] = 3;
		inds[7] = 4;
		inds[8] = 1;
		inds[9] = 4;
		inds[10] = 0;
		inds[11] = 1;
		inds[12] = 0;
		inds[13] = 4;
		inds[14] = 2;
		inds[15] = 4;
		inds[16] = 3;
		inds[17] = 2;

		model.calcNormals();

		return model;
	}

	public static Model cube(double w, double h, double d) {
		Model model = new Model(false, 8, 6);

		double[] vtxs = model.getVertices();

		vtxs[0] = 0;
		vtxs[1] = 0;
		vtxs[2] = 0;
		vtxs[3] = 0;
		vtxs[4] = h;
		vtxs[5] = 0;
		vtxs[6] = 0;
		vtxs[7] = h;
		vtxs[8] = -d;
		vtxs[9] = 0;
		vtxs[10] = 0;
		vtxs[11] = -d;
		vtxs[12] = w;
		vtxs[13] = 0;
		vtxs[14] = 0;
		vtxs[15] = w;
		vtxs[16] = h;
		vtxs[17] = 0;
		vtxs[18] = w;
		vtxs[19] = h;
		vtxs[20] = -d;
		vtxs[21] = w;
		vtxs[22] = 0;
		vtxs[23] = -d;

		int[] inds = model.getFaceInds();

		inds[0] = 0;
		inds[1] = 1;
		inds[2] = 2;
		inds[3] = 3;
		inds[4] = 7;
		inds[5] = 6;
		inds[6] = 5;
		inds[7] = 4;
		inds[8] = 1;
		inds[9] = 0;
		inds[10] = 4;
		inds[11] = 5;
		inds[12] = 3;
		inds[13] = 2;
		inds[14] = 6;
		inds[15] = 7;
		inds[16] = 5;
		inds[17] = 6;
		inds[18] = 2;
		inds[19] = 1;
		inds[20] = 7;
		inds[21] = 4;
		inds[22] = 0;
		inds[23] = 3;

		model.calcNormals();

		return model;
	}

	private int m_npolyvtx; // number of vertices per polygon, either 3 or 4.
	private int m_nvtx; // number of vertices.
	private int m_npolys; // number of polygons.
	private double[] m_vtxs; // vertex data, size 3*m_nvtx
	private double[] m_facenrms; // face normal data, size 3*m_npolys
	private int[] m_faceinds; // face indices
	private Material m_material; // material

	public Model(boolean triPoly, int nv, int np) {

		m_npolyvtx = triPoly ? 3 : 4;
		m_nvtx = nv;
		m_npolys = np;

		m_vtxs = new double[m_nvtx * 3];
		m_facenrms = new double[m_npolys * 3];
		m_faceinds = new int[m_npolys * m_npolyvtx];

		m_material = new Material();
	}

	public int getNumVtx() {
		return m_nvtx;
	}

	public int getNumPolys() {
		return m_npolys;
	}

	public double[] getVertices() {
		return m_vtxs;
	}

	public double[] getFaceNormals() {
		return m_facenrms;
	}

	public int[] getFaceInds() {
		return m_faceinds;
	}

	public Material getMaterial() {
		return m_material;
	}

	public void setMaterial(Material m) {
		m_material = m;
	}

	public void calcNormals() {
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

	public void render(GL2 gl, int nrm) {
		m_material.render(gl);

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
}
