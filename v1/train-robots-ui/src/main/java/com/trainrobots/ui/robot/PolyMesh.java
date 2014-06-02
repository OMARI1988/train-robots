/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.ui.robot;

import javax.media.opengl.*;

public class PolyMesh {

	public static final int TRI_MESH = 3;
	public static final int QUAD_MESH = 4;

	public static final int FACE_NORMALS = 0;
	public static final int VTX_NORMALS = 1;

	public static PolyMesh pyramid(double w, double h, double d) {
		PolyMesh mesh = new PolyMesh(TRI_MESH, 5, 6);

		double[] vtxs = mesh.getVertices();

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

		int[] inds = mesh.getFaceInds();
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

		mesh.calcNormals();

		return mesh;
	}

	public static PolyMesh cube(double w, double h, double d) {
		double x = 0.0, y = 0.0, z = 0.0;

		PolyMesh mesh = new PolyMesh(QUAD_MESH, 8, 6);

		double[] vtxs = mesh.getVertices();

		vtxs[0] = x;
		vtxs[1] = y;
		vtxs[2] = z;
		vtxs[3] = x;
		vtxs[4] = y + h;
		vtxs[5] = z;
		vtxs[6] = x;
		vtxs[7] = y + h;
		vtxs[8] = z - d;
		vtxs[9] = x;
		vtxs[10] = y;
		vtxs[11] = z - d;
		vtxs[12] = x + w;
		vtxs[13] = y;
		vtxs[14] = z;
		vtxs[15] = x + w;
		vtxs[16] = y + h;
		vtxs[17] = z;
		vtxs[18] = x + w;
		vtxs[19] = y + h;
		vtxs[20] = z - d;
		vtxs[21] = x + w;
		vtxs[22] = y;
		vtxs[23] = z - d;

		int[] inds = mesh.getFaceInds();

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

		mesh.calcNormals();

		return mesh;
	}

	private int m_type; // type of mesh, either TRI_MESH or QUAD_MESH.
	private int m_nvtx; // number of vertices.
	private int m_npolys; // number of polygons.
	private double[] m_vtxs; // vertex data, size 3*m_nvtx
	private double[] m_vtxnrms; // vertex normal data, size 3*m_nvtx
	private double[] m_facenrms; // face normal data, size 3*m_npolys
	private int[] m_faceinds; // face indices
	private Material m_material; // material
	
	public Object tag;

	public PolyMesh(int type, int nv, int np) {
		m_type = type;
		m_nvtx = nv;
		m_npolys = np;

		m_vtxs = new double[m_nvtx * 3];
		m_vtxnrms = new double[m_nvtx * 3];
		m_facenrms = new double[m_npolys * 3];
		m_faceinds = new int[m_npolys * m_type];

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

	public double[] getVertexNormals() {
		return m_vtxnrms;
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

	public Vec3 calcCentre() {
		Vec3 ctr = new Vec3();

		for (int i = 0, j = 0; i < m_nvtx; ++i, j += 3) {
			ctr.x += m_vtxs[j];
			ctr.y += m_vtxs[j + 1];
			ctr.z += m_vtxs[j + 2];
		}

		return ctr.divEq(m_nvtx);
	}

	public void calcNormals() {
		int i, j, k, idx0, idx1, idx2;
		Vec3 e0 = new Vec3(), e1 = new Vec3();
		int count[] = new int[m_nvtx];

		for (i = 0, j = 0; i < m_nvtx; ++i, j += 3) {
			count[i] = 0;
			m_vtxnrms[j] = 0.0f;
			m_vtxnrms[j + 1] = 0.0f;
			m_vtxnrms[j + 2] = 0.0f;
		}

		for (i = 0, j = 0, k = 0; i < m_npolys; ++i, j += m_type, k += 3) {
			idx0 = m_faceinds[j] * 3;
			idx1 = m_faceinds[j + 1] * 3;
			idx2 = m_faceinds[j + 2] * 3;
			e0.x = (m_vtxs[idx1] - m_vtxs[idx0]);
			e0.y = (m_vtxs[idx1 + 1] - m_vtxs[idx0 + 1]);
			e0.z = (m_vtxs[idx1 + 2] - m_vtxs[idx0 + 2]);
			e0.normalize();
			e1.x = (m_vtxs[idx2] - m_vtxs[idx0]);
			e1.y = (m_vtxs[idx2 + 1] - m_vtxs[idx0 + 1]);
			e1.z = (m_vtxs[idx2 + 2] - m_vtxs[idx0 + 2]);
			e1.normalize();
			Vec3 n = e1.cross(e0);
			n.normalize();
			m_facenrms[k] = n.x;
			m_facenrms[k + 1] = n.y;
			m_facenrms[k + 2] = n.z;

			m_vtxnrms[idx0] += n.x;
			m_vtxnrms[idx0 + 1] += n.y;
			m_vtxnrms[idx0 + 2] += n.z;
			count[m_faceinds[j]]++;
			m_vtxnrms[idx1] += n.x;
			m_vtxnrms[idx1 + 1] += n.y;
			m_vtxnrms[idx1 + 2] += n.z;
			count[m_faceinds[j + 1]]++;
			m_vtxnrms[idx2] += n.x;
			m_vtxnrms[idx2 + 1] += n.y;
			m_vtxnrms[idx2 + 2] += n.z;
			count[m_faceinds[j + 2]]++;
		}

		for (i = 0, j = 0; i < m_nvtx; ++i, j += 3) {
			m_vtxnrms[j] /= count[i];
			m_vtxnrms[j + 1] /= count[i];
			m_vtxnrms[j + 2] /= count[i];
		}
	}

	public void render(GL2 gl) {
		render(gl, VTX_NORMALS, true);
	}

	public void render(GL2 gl, int nrm, boolean use_mtl) {
		if (use_mtl)
			m_material.render(gl);

		if (m_type == TRI_MESH) {
			gl.glBegin(GL2.GL_TRIANGLES);
		} else if (m_type == QUAD_MESH) {
			gl.glBegin(GL2.GL_QUADS);
		} else {
			return;
		}

		if (nrm == FACE_NORMALS) {
			for (int i = 0, j = 0, k, idx; i < m_npolys; ++i, j += m_type) {
				idx = i * 3;
				gl.glNormal3d(m_facenrms[idx], m_facenrms[idx + 1],
						m_facenrms[idx + 2]);

				for (k = 0; k < m_type; ++k) {
					idx = m_faceinds[j + k] * 3;
					gl.glVertex3d(m_vtxs[idx], m_vtxs[idx + 1], m_vtxs[idx + 2]);
				}
			}
		} else {
			for (int i = 0, j, idx; i < m_faceinds.length; i += m_type) {
				for (j = 0; j < m_type; ++j) {
					idx = m_faceinds[i + j] * 3;
					gl.glNormal3d(m_vtxnrms[idx], m_vtxnrms[idx + 1],
							m_vtxnrms[idx + 2]);
					gl.glVertex3d(m_vtxs[idx], m_vtxs[idx + 1], m_vtxs[idx + 2]);
				}
			}
		}

		gl.glEnd();
	}

}
