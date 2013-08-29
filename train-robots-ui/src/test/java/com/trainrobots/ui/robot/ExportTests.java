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

import org.junit.Ignore;
import org.junit.Test;

public class ExportTests {

	@Test
	@Ignore
	public void shouldExportImages() {

		RobotControl rc = new RobotControl();
		RobotBuffer rb = new RobotBuffer(rc, 325, 350);

		rc.addObject(0, 0, 0, RobotControl.CYAN, RobotControl.CUBE);
		rc.addObject(3, 2, 0, RobotControl.RED, RobotControl.CUBE);
		rc.addObject(5, 7, 0, RobotControl.GREEN, RobotControl.PYRAMID);
		rc.addObject(7, 1, 0, RobotControl.MAGENTA, RobotControl.CUBE);
		rb.renderToFile("c:/temp/test01.png");
		
		rc.moveArm(3, 2, 0);
		rb.renderToFile("c:/temp/test02.png");
		
		rc.grasp();
		rb.renderToFile("c:/temp/test03.png");
		
		rc.moveArm(0, 0, 7);
		rb.renderToFile("c:/temp/test04.png");
		
		rc.grasp();
		rb.renderToFile("c:/temp/test05.png");
		
		rc.moveArm(7, 1, 0);
		rb.renderToFile("c:/temp/test06.png");
		
		rc.grasp();
		rb.renderToFile("c:/temp/test07.png");
		
		rc.moveArm(0, 0, 7);
		rb.renderToFile("c:/temp/test08.png");
		
		rc.grasp();
		rb.renderToFile("c:/temp/test09.png");
		
		rc.moveArm(5, 7, 0);
		rb.renderToFile("c:/temp/test10.png");
		
		rc.grasp();
		rb.renderToFile("c:/temp/test11.png");
		
		rc.moveArm(0, 0, 7);
		rb.renderToFile("c:/temp/test12.png");
		
		rc.grasp();
		rb.renderToFile("c:/temp/test13.png");

		rb.destroy();
	}
}