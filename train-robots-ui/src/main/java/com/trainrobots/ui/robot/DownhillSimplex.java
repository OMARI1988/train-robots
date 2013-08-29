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

import java.util.*;

public class DownhillSimplex
{
  
private static final double TINY = 0.0000001;
  
  private double m_size; // initial size for the simplex
  private int m_maxits; // maximum iterations for optimisation

  public DownhillSimplex(double sz, int mits)
  {
    m_size = sz;
    m_maxits = mits;
  }

  // contract the simplex about a point
private static void multiple_contract(double[][] simplex, double scale)
  {
    for(int i = 1, j; i < simplex.length; ++i)
    {
      for(j = 0; j < simplex[i].length; ++j)
        simplex[i][j] += (simplex[0][j] - simplex[i][j])*scale;
    }
  }
  
  // reflect vertex
private static double[] reflect(double[][] simplex, double scale)
  {
    int i, j,
        rows = simplex.length,
        cols = simplex[0].length;
    double[] vertex = new double[cols];
    
    for(i = (rows - 2); i >= 0; --i)
    {
      for(j = 0; j < cols; ++j)
        vertex[j] += simplex[i][j];
    }
  
    for(i = 0; i < cols; ++i)
    {
      vertex[i] /= (rows - 1);
      vertex[i] -= simplex[rows - 1][i];
      vertex[i] *= scale;
      vertex[i] += simplex[rows - 1][i];
    }
    
    return vertex;
  }
  
  // sort simplex by evaluation
private static double[][] sort_simplex(double[][] simplex, double[] simplex_eval)
  {
    class IndexComparator implements Comparator<Integer>
    {
      private double[] m_arr;
      
      public IndexComparator(double[] arr) { m_arr = arr; }
      
      public Integer[] createInds()
      {
        Integer[] inds = new Integer[m_arr.length];
        for(int i = 0; i < m_arr.length; ++i)
          inds[i] = i;
        return inds;
      }
      
      public int compare(Integer index1, Integer index2)
      { return Double.compare(m_arr[index1], m_arr[index2]); }
    }
  
    IndexComparator comp = new IndexComparator(simplex_eval);
    Integer[] inds = comp.createInds();
    Arrays.sort(inds, comp);
    
    int rows = simplex.length,
        cols = simplex[0].length;
    double[][] res = new double[rows][cols];
    
    double[] eval_tmp = new double[rows];
    for(int i = 0; i < rows; ++i)
      eval_tmp[i] = simplex_eval[inds[i]];
    
    for(int i = 0, j; i < rows; ++i)
    {
      simplex_eval[i] = eval_tmp[i];
      for(j = 0; j < cols; ++j)
        res[i][j] = simplex[inds[i]][j];
    }
    
    return res;
  }
  
  double minimize(double[] init, ObjectiveFunction obj_func, Object data)
  {
    int i, j, numv = init.length + 1;

    double[] simplex_eval = new double[numv];
    double[][] simplex = new double[numv][init.length];
    
    
    for(i = 0; i < numv; ++i)
    {
      for(j = 0; j < init.length; ++j)
        simplex[i][j] = init[j];
      
      if(i > 0) simplex[i][i - 1] += m_size;
      simplex_eval[i] = obj_func.eval(simplex[i], data);
    }

    simplex = sort_simplex(simplex, simplex_eval);

    for(i = 0; i < m_maxits && (simplex_eval[numv - 1] - simplex_eval[0]) > TINY; ++i)
    {
      double[] vertex = reflect(simplex, 2.0);
      double eval = obj_func.eval(vertex, data);

      if(eval < simplex_eval[numv - 1])
      {

        for(j = 0; j < simplex[numv - 1].length; ++j)
          simplex[numv - 1][j] = vertex[j];
        simplex_eval[numv - 1] = eval;
      }

      if(eval <= simplex_eval[0])
      {
        vertex = reflect(simplex, -2.0);
        eval = obj_func.eval(vertex, data);

        if(eval < simplex_eval[numv - 1])
        {
          for(j = 0; j < simplex[numv - 1].length; ++j)
            simplex[numv - 1][j] = vertex[j];
          simplex_eval[numv - 1] = eval;
        }
      }
      else if(eval >= simplex_eval[numv - 2])
      {
        double tmp = eval;
        
        vertex = reflect(simplex, 0.5);
        eval = obj_func.eval(vertex, data);

        if(eval >= tmp)
          multiple_contract(simplex, 0.5);
        else
        {
          for(j = 0; j < simplex[numv - 1].length; ++j)
            simplex[numv - 1][j] = vertex[j];
          simplex_eval[numv - 1] = eval;
        }
      }
      
      simplex = sort_simplex(simplex, simplex_eval);
    }

    for(i = 0; i < init.length; ++i)
      init[i] = simplex[0][i];
    return simplex_eval[0];
    
  }
  
}
