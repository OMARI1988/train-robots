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

public class Mat44
{

  // underlying matrix data initialised to empty
  private double m_data[][] = {{0.0f, 0.0f, 0.0f, 0.0f},
                              {0.0f, 0.0f, 0.0f, 0.0f},
                              {0.0f, 0.0f, 0.0f, 0.0f},
                              {0.0f, 0.0f, 0.0f, 0.0f}};

public static Mat44 mul(Mat44 m0, Mat44 m1)
  {
    Mat44 res = new Mat44(m0.m_data[0][0]*m1.m_data[0][0] +
                          m0.m_data[0][1]*m1.m_data[1][0] +
                          m0.m_data[0][2]*m1.m_data[2][0] +
                          m0.m_data[0][3]*m1.m_data[3][0],
                          m0.m_data[0][0]*m1.m_data[0][1] +
                          m0.m_data[0][1]*m1.m_data[1][1] +
                          m0.m_data[0][2]*m1.m_data[2][1] +
                          m0.m_data[0][3]*m1.m_data[3][1],
                          m0.m_data[0][0]*m1.m_data[0][2] +
                          m0.m_data[0][1]*m1.m_data[1][2] +
                          m0.m_data[0][2]*m1.m_data[2][2] +
                          m0.m_data[0][3]*m1.m_data[3][2],
                          m0.m_data[0][0]*m1.m_data[0][3] +
                          m0.m_data[0][1]*m1.m_data[1][3] +
                          m0.m_data[0][2]*m1.m_data[2][3] +
                          m0.m_data[0][3]*m1.m_data[3][3],
                          m0.m_data[1][0]*m1.m_data[0][0] +
                          m0.m_data[1][1]*m1.m_data[1][0] +
                          m0.m_data[1][2]*m1.m_data[2][0] +
                          m0.m_data[1][3]*m1.m_data[3][0],
                          m0.m_data[1][0]*m1.m_data[0][1] +
                          m0.m_data[1][1]*m1.m_data[1][1] +
                          m0.m_data[1][2]*m1.m_data[2][1] +
                          m0.m_data[1][3]*m1.m_data[3][1],
                          m0.m_data[1][0]*m1.m_data[0][2] +
                          m0.m_data[1][1]*m1.m_data[1][2] +
                          m0.m_data[1][2]*m1.m_data[2][2] +
                          m0.m_data[1][3]*m1.m_data[3][2],
                          m0.m_data[1][0]*m1.m_data[0][3] +
                          m0.m_data[1][1]*m1.m_data[1][3] +
                          m0.m_data[1][2]*m1.m_data[2][3] +
                          m0.m_data[1][3]*m1.m_data[3][3],
                          m0.m_data[2][0]*m1.m_data[0][0] +
                          m0.m_data[2][1]*m1.m_data[1][0] +
                          m0.m_data[2][2]*m1.m_data[2][0] +
                          m0.m_data[2][3]*m1.m_data[3][0],
                          m0.m_data[2][0]*m1.m_data[0][1] +
                          m0.m_data[2][1]*m1.m_data[1][1] +
                          m0.m_data[2][2]*m1.m_data[2][1] +
                          m0.m_data[2][3]*m1.m_data[3][1],
                          m0.m_data[2][0]*m1.m_data[0][2] +
                          m0.m_data[2][1]*m1.m_data[1][2] +
                          m0.m_data[2][2]*m1.m_data[2][2] +
                          m0.m_data[2][3]*m1.m_data[3][2],
                          m0.m_data[2][0]*m1.m_data[0][3] +
                          m0.m_data[2][1]*m1.m_data[1][3] +
                          m0.m_data[2][2]*m1.m_data[2][3] +
                          m0.m_data[2][3]*m1.m_data[3][3],
                          m0.m_data[3][0]*m1.m_data[0][0] +
                          m0.m_data[3][1]*m1.m_data[1][0] +
                          m0.m_data[3][2]*m1.m_data[2][0] +
                          m0.m_data[3][3]*m1.m_data[3][0],
                          m0.m_data[3][0]*m1.m_data[0][1] +
                          m0.m_data[3][1]*m1.m_data[1][1] +
                          m0.m_data[3][2]*m1.m_data[2][1] +
                          m0.m_data[3][3]*m1.m_data[3][1],
                          m0.m_data[3][0]*m1.m_data[0][2] +
                          m0.m_data[3][1]*m1.m_data[1][2] +
                          m0.m_data[3][2]*m1.m_data[2][2] +
                          m0.m_data[3][3]*m1.m_data[3][2],
                          m0.m_data[3][0]*m1.m_data[0][3] +
                          m0.m_data[3][1]*m1.m_data[1][3] +
                          m0.m_data[3][2]*m1.m_data[2][3] +
                          m0.m_data[3][3]*m1.m_data[3][3]);
    return res;
  }

public static Mat44 rotateX(double xa)
  {
    double sin_xa = (double)Math.sin(xa),
          cos_xa = (double)Math.cos(xa);

    Mat44 res = Mat44.id();
    res.m_data[1][1] = res.m_data[2][2] = cos_xa;
    res.m_data[1][2] = -1.0f*sin_xa;
    res.m_data[2][1] = sin_xa;

    return res;
  }

public static Mat44 rotateY(double ya)
  {
    double sin_ya = (double)Math.sin(ya),
          cos_ya = (double)Math.cos(ya);

    Mat44 res = Mat44.id();
    res.m_data[0][0] = res.m_data[2][2] = cos_ya;
    res.m_data[0][2] = sin_ya;
    res.m_data[2][0] = -1.0f*sin_ya;

    return res;
  }

public static Mat44 rotateZ(double za)
  {
    double sin_za = (double)Math.sin(za),
          cos_za = (double)Math.cos(za);

    Mat44 res = Mat44.id();
    res.m_data[0][0] = res.m_data[1][1] = cos_za;
    res.m_data[0][1] = -1.0f*sin_za;
    res.m_data[1][0] = sin_za;

    return res;
  }

public static Mat44 scale(double xs, double ys, double zs)
  {
    Mat44 res = Mat44.id();
    res.m_data[0][0] = xs;
    res.m_data[1][1] = ys;
    res.m_data[2][2] = zs;
    return res;
  }

public static Mat44 translate(double xt, double yt, double zt)
  {
    Mat44 res = Mat44.id();
    res.m_data[3][0] = xt;
    res.m_data[3][1] = yt;
    res.m_data[3][2] = zt;
    return res;
  }

public static Mat44 id()
  {
    return new Mat44(1.0f, 0.0f, 0.0f, 0.0f,
                     0.0f, 1.0f, 0.0f, 0.0f,
                     0.0f, 0.0f, 1.0f, 0.0f,
                     0.0f, 0.0f, 0.0f, 1.0f);
  }

  public Mat44(double e00, double e01, double e02, double e03,
               double e10, double e11, double e12, double e13,
               double e20, double e21, double e22, double e23,
               double e30, double e31, double e32, double e33)
  {
    m_data[0][0] = e00; m_data[0][1] = e01; m_data[0][2] = e02; m_data[0][3] = e03; 
    m_data[1][0] = e10; m_data[1][1] = e11; m_data[1][2] = e12; m_data[1][3] = e13; 
    m_data[2][0] = e20; m_data[2][1] = e21; m_data[2][2] = e22; m_data[2][3] = e23; 
    m_data[3][0] = e30; m_data[3][1] = e31; m_data[3][2] = e32; m_data[3][3] = e33; 
  }

  public Mat44(Mat44 m)
  {
    m_data[0][0] = m.m_data[0][0]; m_data[0][1] = m.m_data[0][1]; m_data[0][2] = m.m_data[0][2]; m_data[0][3] = m.m_data[0][3]; 
    m_data[1][0] = m.m_data[1][0]; m_data[1][1] = m.m_data[1][1]; m_data[1][2] = m.m_data[1][2]; m_data[1][3] = m.m_data[1][3]; 
    m_data[2][0] = m.m_data[2][0]; m_data[2][1] = m.m_data[2][1]; m_data[2][2] = m.m_data[2][2]; m_data[2][3] = m.m_data[2][3]; 
    m_data[3][0] = m.m_data[3][0]; m_data[3][1] = m.m_data[3][1]; m_data[3][2] = m.m_data[3][2]; m_data[3][3] = m.m_data[3][3]; 
  }

  public double get(int i, int j)
  { return m_data[i][j]; }

  public void set(int i, int j, double val)
  { m_data[i][j] = val; }

  public boolean equals(Mat44 m)
  {
    for(int i = 0; i < 4; ++i)
    {
      if((m_data[i][0] != m.m_data[i][0]) ||
         (m_data[i][1] != m.m_data[i][1]) ||
         (m_data[i][2] != m.m_data[i][2]) ||
         (m_data[i][3] != m.m_data[i][3]))
      { return false; }
    }
    return true;
  }

  public String toString()
  {
    String s = "";
    for(int i = 0; i < 4; ++i)
    {
      s += "|" + m_data[i][0] + "," +
                 m_data[i][1] + "," +
                 m_data[i][2] + "," +
                 m_data[i][3] + "|\n";
    }

    return s;
  }

  public void clear()
  {
    for(int i = 0, j; i < 4; ++i)
    {
      m_data[i][i] = 0.0f;
      for(j = i + 1; j < 4; ++j)
      { m_data[i][j] = m_data[j][i] = 0.0f; }
    }
  }

  public void identity()
  {
    for(int i = 0, j; i < 4; ++i)
    {
      m_data[i][i] = 1.0f;
      for(j = i + 1; j < 4; ++j)
      { m_data[i][j] = m_data[j][i] = 0.0f; }
    }
  }

  public void transpose()
  {
    double tmp;

    for(int i = 0, j; i < 4; ++i)
    {
      for(j = i + 1; j < 4; ++j)
      {
        tmp = m_data[i][j];
        m_data[i][j] = m_data[j][i];
        m_data[j][i] = tmp;
      }
    } 
  }

  public void addEq(double s)
  {
    m_data[0][0] += s; m_data[0][1] += s; m_data[0][2] += s; m_data[0][3] += s; 
    m_data[1][0] += s; m_data[1][1] += s; m_data[1][2] += s; m_data[1][3] += s; 
    m_data[2][0] += s; m_data[2][1] += s; m_data[2][2] += s; m_data[2][3] += s; 
    m_data[3][0] += s; m_data[3][1] += s; m_data[3][2] += s; m_data[3][3] += s; 
  }

  public Mat44 add(double s)
  {
    Mat44 res = new Mat44(this);
    res.addEq(s);
    return res;
  }

  public void subEq(double s)
  { addEq(-1.0f*s); }

  public Mat44 sub(double s)
  {
    Mat44 res = new Mat44(this);
    res.subEq(s);
    return res;
  }

  public void mulEq(double s)
  {
    m_data[0][0] *= s; m_data[0][1] *= s; m_data[0][2] *= s; m_data[0][3] *= s; 
    m_data[1][0] *= s; m_data[1][1] *= s; m_data[1][2] *= s; m_data[1][3] *= s; 
    m_data[2][0] *= s; m_data[2][1] *= s; m_data[2][2] *= s; m_data[2][3] *= s; 
    m_data[3][0] *= s; m_data[3][1] *= s; m_data[3][2] *= s; m_data[3][3] *= s; 
  }

  public Mat44 mul(double s)
  {
    Mat44 res = new Mat44(this);
    res.mulEq(s);
    return res;
  }

  public Mat44 mul(Mat44 m)
  { return Mat44.mul(this, m); }

  public void mulEq(Mat44 m)
  {
    Mat44 tmp = Mat44.mul(this, m);
    
    m_data[0][0] = tmp.m_data[0][0];
    m_data[0][1] = tmp.m_data[0][1]; 
    m_data[0][2] = tmp.m_data[0][2]; 
    m_data[0][3] = tmp.m_data[0][3];
    m_data[1][0] = tmp.m_data[1][0];
    m_data[1][1] = tmp.m_data[1][1]; 
    m_data[1][2] = tmp.m_data[1][2]; 
    m_data[1][3] = tmp.m_data[1][3];
    m_data[2][0] = tmp.m_data[2][0];
    m_data[2][1] = tmp.m_data[2][1]; 
    m_data[2][2] = tmp.m_data[2][2]; 
    m_data[2][3] = tmp.m_data[2][3];
    m_data[3][0] = tmp.m_data[3][0];
    m_data[3][1] = tmp.m_data[3][1]; 
    m_data[3][2] = tmp.m_data[3][2]; 
    m_data[3][3] = tmp.m_data[3][3];
  }
  
  public void divEq(double s)
  { mulEq(1.0f/s); }

  public Mat44 div(double s)
  {
    Mat44 res = new Mat44(this);
    res.divEq(s);
    return res;
  }

}
