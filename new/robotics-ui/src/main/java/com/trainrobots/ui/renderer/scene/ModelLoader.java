/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

import java.io.*;
import java.util.*;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.ui.Resources;
import com.trainrobots.ui.renderer.math.Vector;

public class ModelLoader {

	private ModelLoader() {
	}

	public static Model load(String fn) {
		Log.info("Loading 3D model: %s", fn);
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							Resources.open("/com/trainrobots/ui/models/" + fn
									+ ".obj")));

			ArrayList<Vector> verts = new ArrayList<Vector>();
			ArrayList<Integer> vtxinds = new ArrayList<Integer>();

			float[] ambient = { 0.2f, 0.2f, 0.2f, 1.0f };
			float[] diffuse = { 0.8f, 0.8f, 0.8f, 1.0f };

			String line = reader.readLine();
			while (line != null) {
				StringTokenizer stok = new StringTokenizer(line);

				if (stok.hasMoreTokens()) {
					String tok = stok.nextToken();

					if (tok.equals("mtllib")) // material
					{
						readMaterial(stok.nextToken(), ambient, diffuse);
					} else if (tok.equals("v")) // vertex
					{
						double x = new Double(stok.nextToken());
						double y = new Double(stok.nextToken());
						double z = new Double(stok.nextToken());
						verts.add(new Vector(x, y, z));
					} else if (tok.equals("f")) // face
					{
						int nverts = 0;
						int[] tmp_verts = new int[10];

						while (stok.hasMoreTokens()) {
							tok = stok.nextToken();

							// switch according to whether the file contains
							// normal indices and texture indices
							if (tok.contains("//")) {
								StringTokenizer st = new StringTokenizer(tok,
										"//");
								tmp_verts[nverts] = new Integer(st.nextToken());
							} else if (tok.contains("/")) {
								StringTokenizer st = new StringTokenizer(tok,
										"/");
								tmp_verts[nverts] = new Integer(st.nextToken());
							} else {
								tmp_verts[nverts] = new Integer(tok);
							}

							++nverts;
						}

						for (int i = 1; i < (nverts - 1); ++i) {
							vtxinds.add(new Integer(tmp_verts[0] - 1));
							vtxinds.add(new Integer(tmp_verts[i] - 1));
							vtxinds.add(new Integer(tmp_verts[i + 1] - 1));
						}
					}
				}

				line = reader.readLine();
			}

			reader.close();

			// retriangulate n-gons into triangles
			double[] res_verts = new double[verts.size() * 3];
			int[] res_inds = new int[vtxinds.size()];

			for (int i = 0, j = 0; i < verts.size(); ++i, j += 3) {
				res_verts[j] = verts.get(i).x();
				res_verts[j + 1] = verts.get(i).y();
				res_verts[j + 2] = verts.get(i).z();
			}

			for (int i = 0; i < vtxinds.size(); i += 3) {
				res_inds[i] = vtxinds.get(i);
				res_inds[i + 1] = vtxinds.get(i + 1);
				res_inds[i + 2] = vtxinds.get(i + 2);
			}

			return new Model(true, res_verts, res_inds, ambient, diffuse);
		} catch (IOException e) {
			throw new RoboticException(e);
		}
	}

	private static void readMaterial(String fn, float[] ambient, float[] diffuse) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					Resources.open("/com/trainrobots/ui/models/" + fn)));

			String line = reader.readLine();

			while (line != null) {
				StringTokenizer stok = new StringTokenizer(line);

				if (stok.hasMoreTokens()) {
					String tok = stok.nextToken();

					if (tok.equals("Ka")) // ambient color
					{
						float red = new Float(stok.nextToken()), green = new Float(
								stok.nextToken()), blue = new Float(
								stok.nextToken());
						ambient[0] = red;
						ambient[1] = green;
						ambient[2] = blue;
						ambient[3] = 1.0f;
					} else if (tok.equals("Kd")) // diffuse color
					{
						float red = new Float(stok.nextToken()), green = new Float(
								stok.nextToken()), blue = new Float(
								stok.nextToken());
						diffuse[0] = red;
						diffuse[1] = green;
						diffuse[2] = blue;
						diffuse[3] = 1.0f;
					} else if (tok.equals("Ks")) // specular color
					{
						stok.nextToken();
						stok.nextToken();
						stok.nextToken();
					}
				}

				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			throw new RoboticException(e);
		}
	}
}
