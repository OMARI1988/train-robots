/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.scene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.ui.Resources;
import com.trainrobots.ui.renderer.math.Vector;

public class ModelLoader {

	private ModelLoader() {
	}

	public static Model load(String filename) {

		// Diagnostics.
		Log.info("Loading 3D model: %s", filename);

		// Initiate.
		ArrayList<Vector> vertexList = new ArrayList<Vector>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		float[] ambient = new float[4];
		float[] diffuse = new float[4];

		// Read.
		try (BufferedReader reader = open(filename + ".obj")) {
			String line = reader.readLine();
			while (line != null) {

				// Read line.
				StringTokenizer tokenizer = new StringTokenizer(line);
				if (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();

					// Material.
					if (token.equals("mtllib")) {
						loadMaterial(tokenizer.nextToken(), ambient, diffuse);
					}

					// Vertex.
					else if (token.equals("v")) {
						double x = Double.parseDouble(tokenizer.nextToken());
						double y = Double.parseDouble(tokenizer.nextToken());
						double z = Double.parseDouble(tokenizer.nextToken());
						vertexList.add(new Vector(x, y, z));
					}

					// Face.
					else if (token.equals("f")) {
						int n = 0;
						int[] vertices = new int[10];
						while (tokenizer.hasMoreTokens()) {
							token = tokenizer.nextToken();
							StringTokenizer items = new StringTokenizer(token,
									"/");
							vertices[n] = Integer.parseInt(items.nextToken());
							n++;
						}
						for (int i = 1; i < n - 1; i++) {
							indexList.add(vertices[0] - 1);
							indexList.add(vertices[i] - 1);
							indexList.add(vertices[i + 1] - 1);
						}
					}
				}
				line = reader.readLine();
			}
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}

		// Convert polygons to triangles.
		int size = vertexList.size();
		double[] vertices = new double[size * 3];
		for (int i = 0, j = 0; i < size; ++i, j += 3) {
			Vector v = vertexList.get(i);
			vertices[j] = v.x();
			vertices[j + 1] = v.y();
			vertices[j + 2] = v.z();
		}
		size = indexList.size();
		int[] indices = new int[size];
		for (int i = 0; i < size; i++) {
			indices[i] = indexList.get(i);
		}
		return new Model(3, vertices, indices, ambient, diffuse);
	}

	private static void loadMaterial(String filename, float[] ambient,
			float[] diffuse) {

		// Read.
		try (BufferedReader reader = open(filename)) {
			String line = reader.readLine();

			// Read line.
			while (line != null) {
				StringTokenizer tokenizer = new StringTokenizer(line);
				if (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();

					// Ambient color.
					if (token.equals("Ka")) {
						ambient[0] = Float.parseFloat(tokenizer.nextToken());
						ambient[1] = Float.parseFloat(tokenizer.nextToken());
						ambient[2] = Float.parseFloat(tokenizer.nextToken());
						ambient[3] = 1.0f;
					}

					// Diffuse color.
					else if (token.equals("Kd")) {
						diffuse[0] = Float.parseFloat(tokenizer.nextToken());
						diffuse[1] = Float.parseFloat(tokenizer.nextToken());
						diffuse[2] = Float.parseFloat(tokenizer.nextToken());
						diffuse[3] = 1.0f;
					}

					// Specular color.
					else if (token.equals("Ks")) {
						tokenizer.nextToken();
						tokenizer.nextToken();
						tokenizer.nextToken();
					}
				}
				line = reader.readLine();
			}
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	private static BufferedReader open(String filename) {
		String path = "/com/trainrobots/ui/models/" + filename;
		InputStream stream = Resources.open(path);
		return new BufferedReader(new InputStreamReader(stream));
	}
}
