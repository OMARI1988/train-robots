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

import java.io.*;
import java.util.*;

import com.trainrobots.ui.resources.ResourceUtil;

public class OBJReader
{

public PolyMesh readMesh(String fn)
  {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceUtil.open("/com/trainrobots/ui/models/" + fn)));
      
      Vector<Vec3> verts = new Vector<Vec3>();
      Vector<Integer> vtxinds = new Vector<Integer>();
      
      Material mtl = new Material();
      
      String line = reader.readLine();
      while(line != null)
      {
        StringTokenizer stok = new StringTokenizer(line);
        
        if(stok.hasMoreTokens())
        {
          String tok = stok.nextToken();
        
          if(tok.equals("mtllib")) // material
          {
            mtl = (new MTLReader()).readMaterial(stok.nextToken());
          }
          else if(tok.equals("v")) // vertex
          {
            double x = new Double(stok.nextToken()),
                   y = new Double(stok.nextToken()),
                   z = new Double(stok.nextToken());
            verts.add(new Vec3(x, y, z));
          }
          else if(tok.equals("f")) // face
          { 
            int nverts = 0;
            int[] tmp_verts = new int[10];
            
            while(stok.hasMoreTokens())
            {
              tok = stok.nextToken();
              
              // switch according to whether the file contains normal indices and texture indices
              if(tok.contains("//"))
              {
                StringTokenizer st = new StringTokenizer(tok, "//");
                tmp_verts[nverts] = new Integer(st.nextToken());
              }
              else if(tok.contains("/"))
              {
                StringTokenizer st = new StringTokenizer(tok, "/");
                tmp_verts[nverts] = new Integer(st.nextToken());
              }
              else
              {
                tmp_verts[nverts] = new Integer(tok);
              }

              ++nverts;
            }
            
            for(int i = 1; i < (nverts - 1); ++i)
            {
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
      PolyMesh res = new PolyMesh(PolyMesh.TRI_MESH, verts.size(), vtxinds.size()/3);
      res.setMaterial(mtl);

      double[] res_verts = res.getVertices();
      int[] res_inds = res.getFaceInds();
    
      for(int i = 0, j = 0; i < verts.size(); ++i, j += 3)
      {
        res_verts[j] = verts.get(i).x;
        res_verts[j + 1] = verts.get(i).y;
        res_verts[j + 2] = verts.get(i).z;
      }
      
      for(int i = 0; i < vtxinds.size(); i += 3)
      {
        res_inds[i] = vtxinds.get(i);
        res_inds[i + 1] = vtxinds.get(i + 1);
        res_inds[i + 2] = vtxinds.get(i + 2);
      }
      
      // we ignore the normals in the file and recompute ourselves
      res.calcNormals();
      
      return res;
    }
    catch(IOException e)
    { System.out.println(e); }
  
    return null;
  }
  
}
