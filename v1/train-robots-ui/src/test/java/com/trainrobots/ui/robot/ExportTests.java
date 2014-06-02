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

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.DataContext;
import com.trainrobots.core.configuration.Configuration;
import com.trainrobots.core.configuration.ConfigurationReader;

public class ExportTests {

	@Test
	@Ignore
	public void shouldExportImages() {

		List<Configuration> configuration = ConfigurationReader
				.read(DataContext.getFile("configuration.txt"));

		RobotControl rc = new RobotControl();
		RobotBuffer rb = new RobotBuffer(rc, 325, 350);

		for (Configuration c : configuration) {
			String path = DataContext.getFile("g" + c.groupNumber + "/x"
					+ c.imageNumber + ".png");
			rc.loadConfiguration(c);
			rb.renderToFile(path);
			System.out.println("Wrote: " + path);
		}

		rb.destroy();
	}
}