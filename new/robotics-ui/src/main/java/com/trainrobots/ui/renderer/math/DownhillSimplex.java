/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.math;

import java.util.Arrays;
import java.util.Comparator;

public class DownhillSimplex {

	private double size;
	private int maxIterations;

	public DownhillSimplex(double size, int maxIterations) {
		this.size = size;
		this.maxIterations = maxIterations;
	}

	public double minimize(double[] v, ObjectiveFunction objectiveFunction) {

		int vertices = v.length + 1;
		double[] evaluation = new double[vertices];
		double[][] simplex = new double[vertices][v.length];

		for (int i = 0; i < vertices; ++i) {
			for (int j = 0; j < v.length; ++j) {
				simplex[i][j] = v[j];
			}
			if (i > 0) {
				simplex[i][i - 1] += size;
			}
			evaluation[i] = objectiveFunction.evaluate(simplex[i]);
		}

		simplex = sort(simplex, evaluation);

		for (int i = 0; i < maxIterations
				&& (evaluation[vertices - 1] - evaluation[0]) > 0.0000001; ++i) {
			double[] vertex = reflect(simplex, 2.0);
			double eval = objectiveFunction.evaluate(vertex);

			if (eval < evaluation[vertices - 1]) {

				for (int j = 0; j < simplex[vertices - 1].length; ++j) {
					simplex[vertices - 1][j] = vertex[j];
				}
				evaluation[vertices - 1] = eval;
			}

			if (eval <= evaluation[0]) {
				vertex = reflect(simplex, -2.0);
				eval = objectiveFunction.evaluate(vertex);

				if (eval < evaluation[vertices - 1]) {
					for (int j = 0; j < simplex[vertices - 1].length; ++j) {
						simplex[vertices - 1][j] = vertex[j];
					}
					evaluation[vertices - 1] = eval;
				}
			} else if (eval >= evaluation[vertices - 2]) {
				double tmp = eval;

				vertex = reflect(simplex, 0.5);
				eval = objectiveFunction.evaluate(vertex);

				if (eval >= tmp) {
					contract(simplex, 0.5);
				} else {
					for (int j = 0; j < simplex[vertices - 1].length; ++j) {
						simplex[vertices - 1][j] = vertex[j];
					}
					evaluation[vertices - 1] = eval;
				}
			}

			simplex = sort(simplex, evaluation);
		}

		for (int i = 0; i < v.length; ++i) {
			v[i] = simplex[0][i];
		}
		return evaluation[0];
	}

	private static void contract(double[][] simplex, double scale) {

		// Contract the simplex about a point.
		for (int i = 1, j; i < simplex.length; ++i) {
			for (j = 0; j < simplex[i].length; ++j) {
				simplex[i][j] += (simplex[0][j] - simplex[i][j]) * scale;
			}
		}
	}

	private static double[] reflect(double[][] simplex, double scale) {

		// Reflect vertex.
		int rows = simplex.length;
		int cols = simplex[0].length;
		double[] vertex = new double[cols];
		for (int i = rows - 2; i >= 0; --i) {
			for (int j = 0; j < cols; ++j)
				vertex[j] += simplex[i][j];
		}
		for (int i = 0; i < cols; ++i) {
			vertex[i] /= (rows - 1);
			vertex[i] -= simplex[rows - 1][i];
			vertex[i] *= scale;
			vertex[i] += simplex[rows - 1][i];
		}
		return vertex;
	}

	private static double[][] sort(double[][] simplex, double[] evaluation) {

		// Sort simplex by evaluation.
		class IndexComparator implements Comparator<Integer> {
			private double[] array;

			public IndexComparator(double[] array) {
				this.array = array;
			}

			public Integer[] createIndices() {
				Integer[] indices = new Integer[array.length];
				for (int i = 0; i < array.length; ++i) {
					indices[i] = i;
				}
				return indices;
			}

			public int compare(Integer index1, Integer index2) {
				return Double.compare(array[index1], array[index2]);
			}
		}

		IndexComparator comparator = new IndexComparator(evaluation);
		Integer[] indices = comparator.createIndices();
		Arrays.sort(indices, comparator);

		int rows = simplex.length;
		int cols = simplex[0].length;
		double[][] result = new double[rows][cols];

		double[] eval_tmp = new double[rows];
		for (int i = 0; i < rows; ++i) {
			eval_tmp[i] = evaluation[indices[i]];
		}

		for (int i = 0, j; i < rows; ++i) {
			evaluation[i] = eval_tmp[i];
			for (j = 0; j < cols; ++j) {
				result[i][j] = simplex[indices[i]][j];
			}
		}

		return result;
	}
}
