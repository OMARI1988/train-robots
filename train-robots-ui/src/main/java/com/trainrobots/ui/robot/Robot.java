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

public class Robot implements ObjectiveFunction
{

  // meshes for rendering
  PolyMesh m_base,
           m_pivot,
           m_arm,
           m_forearm,
           m_wrist,
           m_tarsal,
           m_hand_wrist,
           m_finger1,
           m_finger2;

  Vec3 m_translate = new Vec3(0.0, 0.0, 0.0); // base translation of the arm
  double m_pivot_roty = 0.0, // y rotation of pivot
         m_arm_rotz = -90.0, // z rotation of arm
         m_forearm_rotz = 90.0, // z rotation of forearm
         m_wrist_rotz = 90.0, // z rotation of wrist
         m_tarsal_rotz = 0.0, // z rotation of tarsal
         m_hand_rotx = 0.0, // x rotation of hand
         m_grasp = 0.0; // grasp parameter

  public Robot()
  {
    // read in the meshes
    OBJReader reader = new OBJReader();
    m_base = reader.readMesh("Base.obj");
    m_pivot = reader.readMesh("Pivot.obj");
    m_arm = reader.readMesh("Arm.obj");
    m_forearm = reader.readMesh("Forearm.obj");
    m_wrist = reader.readMesh("Wrist.obj");
    m_tarsal = reader.readMesh("Tarsal.obj");
    m_hand_wrist = reader.readMesh("Hand-Wrist.obj");
    m_finger1 = reader.readMesh("Finger1.obj");
    m_finger2 = reader.readMesh("Finger2.obj");
  }
  
  public double getPivotRotY() { return m_pivot_roty; }
  public double getArmRotZ() { return m_arm_rotz; }
  public double getForearmRotZ() { return m_forearm_rotz; }
  public double getWristRotZ() { return m_wrist_rotz; }
  public double getTarsalRotZ() { return m_tarsal_rotz; }
  public double getHandRotX() { return m_hand_rotx; }
  public double getGrasp() { return m_grasp; }
  
  public void setTranslate(double tx, double ty, double tz)
  { m_translate.x = tx; m_translate.y = ty; m_translate.z = tz; }
  
  public void setPivotRotY(double ry) { m_pivot_roty = ry; }
  public void setArmRotZ(double rz) { m_arm_rotz = rz; }
  public void setForearmRotZ(double rz) { m_forearm_rotz = rz; }
  public void setWristRotZ(double rz) { m_wrist_rotz = rz; }
  public void setTarsalRotZ(double rz) { m_tarsal_rotz = rz; }
  public void setHandRotX(double rx) { m_hand_rotx = rx; }
  public void setGrasp(double g) { m_grasp = g; }
  
public static Vec3 calcEndEffector(double hand_rotx,
                                   double tarsal_rotz,
                                   double wrist_rotz,
                                   double forearm_rotz,
                                   double arm_rotz,
                                   double pivot_roty,
                                   double tx, double ty, double tz)
  {
    double DEG_TO_RAD = 0.01745329;
    Vec3 v = new Vec3(0.0, 0.0, 0.0);
    Mat44 tfrm = Mat44.translate(12.0, 0.0, 0.0).mul(
                 Mat44.rotateX(DEG_TO_RAD*hand_rotx).mul(
                 Mat44.translate(3.0, 0.0, 0).mul(
                 Mat44.rotateZ(DEG_TO_RAD*tarsal_rotz).mul(
                 Mat44.translate(12.1, 0.0, -2.6).mul(
                 Mat44.rotateZ(DEG_TO_RAD*wrist_rotz).mul(
                 Mat44.translate(18.5, 0.0, 1.6).mul(
                 Mat44.rotateZ(DEG_TO_RAD*forearm_rotz).mul(
                 Mat44.translate(25.0, -0.5, -2.0).mul(
                 Mat44.rotateZ(DEG_TO_RAD*arm_rotz).mul(
                 Mat44.translate(12.5, 3.3, 4.5).mul(
                 Mat44.rotateY(DEG_TO_RAD*pivot_roty).mul(
                 Mat44.translate(0.0, 16.0, 0.0).mul(
                 Mat44.translate(tx, ty, tz))))))))))))));
    return v.mul(tfrm);
  }

  public Vec3 calcEndEffector()
  {
    return calcEndEffector(m_hand_rotx,
                           m_tarsal_rotz,
                           m_wrist_rotz,
                           m_forearm_rotz,
                           m_arm_rotz,
                           m_pivot_roty,
                           m_translate.x, 
                           m_translate.y,
                           m_translate.z);
  }
  
  public double eval(double[] param, Object data)
  {
    Vec3 target = (Vec3)data;
    double DEG_TO_RAD = 0.01745329;
    Mat44 tfrm = Mat44.translate(12.1, 0.0, -2.6).mul(
                 Mat44.rotateZ(DEG_TO_RAD*param[0]).mul(
                 Mat44.translate(18.5, 0.0, 1.6).mul(
                 Mat44.rotateZ(DEG_TO_RAD*param[1]).mul(
                 Mat44.translate(25.0, -0.5, -2.0).mul(
                 Mat44.rotateZ(DEG_TO_RAD*param[2]).mul(
                 Mat44.translate(12.5, 3.3, 4.5).mul(
                 Mat44.rotateY(DEG_TO_RAD*param[3]).mul(
                 Mat44.translate(0.0, 16.0, 0.0).mul(
                 Mat44.translate(m_translate.x, 
                                 m_translate.y, 
                                 m_translate.z))))))))));
    Vec3 end = (new Vec3()).mul(tfrm);

    return target.sqdistance(end); // return the square of the distance of the arm end from the target
  }
  
  public void resetAngles()
  {
    m_pivot_roty = 0.0;
    m_arm_rotz = -90.0;
    m_forearm_rotz = 90.0;
    m_wrist_rotz = 90.0;
    m_tarsal_rotz = 0.0;
    m_hand_rotx = 0.0;
  }

  public void computeAngles(Vec3 target)
  {
    Vec3 tgt = new Vec3(target);
    tgt.y = Math.max(0.0, tgt.y + 15.0); // offset so that hand doesn't go through board
    
    double[] param = { m_wrist_rotz,
                       m_forearm_rotz,
                       m_arm_rotz,
                       m_pivot_roty };
    DownhillSimplex ds = new DownhillSimplex(0.1, 1000);
    ds.minimize(param, this, tgt); // compute angles
    
    m_wrist_rotz = param[0];
    m_forearm_rotz = param[1];
    m_arm_rotz = param[2];
    m_pivot_roty = param[3];
    
    // compute tarsal so that the hand always points down
    double DEG_TO_RAD = 0.01745329;
    double RAD_TO_DEG = 57.2957796;
    Mat44 tfrm = Mat44.translate(12.1, 0.0, -2.6).mul(
                 Mat44.rotateZ(DEG_TO_RAD*param[0]).mul(
                 Mat44.translate(18.5, 0.0, 1.6).mul(
                 Mat44.rotateZ(DEG_TO_RAD*param[1]).mul(
                 Mat44.translate(25.0, -0.5, -2.0).mul(
                 Mat44.rotateZ(DEG_TO_RAD*param[2]).mul(
                 Mat44.translate(12.5, 3.3, 4.5).mul(
                 Mat44.rotateY(DEG_TO_RAD*param[3]).mul(
                 Mat44.translate(0.0, 16.0, 0.0).mul(
                 Mat44.translate(m_translate.x, 
                                 m_translate.y, 
                                 m_translate.z))))))))));
    
    m_tarsal_rotz = 0.0;
    Vec3 wrist = (new Vec3()).mul(tfrm),
         end = calcEndEffector(),
         dir = (end.sub(wrist));
    dir.normalize();
    m_tarsal_rotz = RAD_TO_DEG*Math.acos((new Vec3(0.0, -1.0, 0.0)).dot(dir));
    
    Vec3 rot1 = dir.mul(Mat44.rotateZ(m_tarsal_rotz*DEG_TO_RAD)),
         rot2 = dir.mul(Mat44.rotateZ(-m_tarsal_rotz*DEG_TO_RAD));
    
    if(rot2.y < rot1.y)
      m_tarsal_rotz *= -1.0;
    
    // compute hand so that it always faces the same way
    m_hand_rotx = m_pivot_roty + 90.0;
  }
  
  public void render(GL2 gl, int nrm, boolean use_mtl)
  {
    gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
    gl.glPushMatrix();
    
    gl.glTranslated(m_translate.x, m_translate.y, m_translate.z);
    
    m_base.render(gl, nrm, use_mtl);
    
    gl.glTranslated(0.0, 16.0, 0.0);
    gl.glRotated(-m_pivot_roty, 0.0, 1.0, 0.0);
    
    m_pivot.render(gl, nrm, use_mtl);
    
    gl.glTranslated(12.5, 3.3, 4.5);
    gl.glRotated(-m_arm_rotz, 0.0, 0.0, 1.0);
    
    m_arm.render(gl, nrm, use_mtl);
    
    gl.glTranslated(25.0, -0.5, -2.0);
    gl.glRotated(-m_forearm_rotz, 0.0, 0.0, 1.0);
    
    m_forearm.render(gl, nrm, use_mtl);
    
    gl.glTranslated(18.5, 0.0, 1.6);
    gl.glRotated(-m_wrist_rotz, 0.0, 0.0, 1.0);
    
    m_wrist.render(gl, nrm, use_mtl);
    
    gl.glTranslated(12.1, 0.0, -2.6);
    gl.glRotated(-m_tarsal_rotz, 0.0, 0.0, 1.0);
    
    m_tarsal.render(gl, nrm, use_mtl);
    
    gl.glTranslated(3.0, 0.0, 0.0);
    gl.glRotated(-m_hand_rotx, 1.0, 0.0, 0.0);
    
    m_hand_wrist.render(gl, nrm, use_mtl);
    
    gl.glPushMatrix();
    gl.glTranslated(4.5, (1.0 - m_grasp)*6.0, 0);
    m_finger1.render(gl, nrm, use_mtl);
    gl.glPopMatrix();
    
    gl.glPushMatrix();
    gl.glTranslated(4.5, (1.0 - m_grasp)*-6.0, 0);
    m_finger2.render(gl, nrm, use_mtl);
    gl.glPopMatrix();
    
    gl.glPopMatrix();
    gl.glPopAttrib();
  }
  
}
