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

public class MTLReader
{

public Material readMaterial(String fn)
  {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(ResourceUtil.open("/com/trainrobots/ui/models/" + fn)));
      
      String line = reader.readLine();
      
      Material m = new Material();
      
      while(line != null)
      {
        StringTokenizer stok = new StringTokenizer(line);
        
        if(stok.hasMoreTokens())
        {
          String tok = stok.nextToken();
          
          if(tok.equals("Ka")) // ambient color
          {
            float red = new Float(stok.nextToken()),
                   green = new Float(stok.nextToken()),
                   blue = new Float(stok.nextToken());
            m.setAmbient(red, green, blue, 1.0f);
          }
          else if(tok.equals("Kd")) // diffuse color
          {
            float red = new Float(stok.nextToken()),
                  green = new Float(stok.nextToken()),
                  blue = new Float(stok.nextToken());
            m.setDiffuse(red, green, blue, 1.0f);
          }
          else if(tok.equals("Ks")) // specular color
          {
            float red = new Float(stok.nextToken()),
                  green = new Float(stok.nextToken()),
                  blue = new Float(stok.nextToken());
            m.setSpecular(red, green, blue, 1.0f);
          }
        }
        
        line = reader.readLine();
      }
      
      reader.close();
      
      return m;
    }
    catch(IOException e)
    { System.out.println(e); }
    
    return null;
  }
  
}
