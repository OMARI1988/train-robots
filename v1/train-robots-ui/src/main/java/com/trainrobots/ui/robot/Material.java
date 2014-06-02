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

public class Material {

	private float[] m_amb = { 0.2f, 0.2f, 0.2f, 1.0f }, // ambient color
			m_diff = { 0.8f, 0.8f, 0.8f, 1.0f }, // diffuse color
			m_spec = { 0.0f, 0.0f, 0.0f, 1.0f }; // specular color
	private float m_shininess = 0.0f; // shininess exponent

	public Material() {
	}

	public float[] getAmbient() {
		return m_amb;
	}

	public float[] getDiffuse() {
		return m_diff;
	}

	public float[] getSpecular() {
		return m_spec;
	}

	public float getShininess() {
		return m_shininess;
	}

	public void setAmbient(float r, float g, float b, float a) {
		m_amb[0] = r;
		m_amb[1] = g;
		m_amb[2] = b;
		m_amb[3] = a;
	}

	public void setDiffuse(float r, float g, float b, float a) {
		m_diff[0] = r;
		m_diff[1] = g;
		m_diff[2] = b;
		m_diff[3] = a;
	}

	public void setSpecular(float r, float g, float b, float a) {
		m_spec[0] = r;
		m_spec[1] = g;
		m_spec[2] = b;
		m_spec[3] = a;
	}

	public void setShininess(float sh) {
		m_shininess = sh;
	}

	public void render(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, m_amb, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, m_diff, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, m_amb, 0);
		gl.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, m_shininess);
	}

}
