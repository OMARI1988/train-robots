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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.trainrobots.core.DataContext;
import com.trainrobots.core.configuration.Configuration;
import com.trainrobots.core.configuration.ConfigurationReader;

public class RegressionTests {

	@Test
	public void shouldExportSingleImage() throws IOException {
		List<Configuration> configuration = ConfigurationReader
				.read(DataContext.getFile("configuration.txt"));

		RobotControl rc = new RobotControl();
		RobotBuffer rb = new RobotBuffer(rc, 325, 350);

		Configuration c = configuration.get(250);

		rc.loadConfiguration(c);
		byte[] data = rb.renderToArray();
		rb.destroy();

		assertEquals(data.length, 11636);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], data[23 * i]);
		}
	}

	private static final byte[] expected = { -119, 94, 112, -101, -19, -112,
			58, 123, 32, -126, 8, 32, -126, 8, 32, -126, 122, 116, -112, -112,
			-116, 20, -32, 120, -60, 25, -49, 64, 104, -83, 74, -14, 16, -39,
			116, -29, 57, -3, 112, 110, 80, 32, -2, -67, -5, -80, 85, -37,
			-107, -63, -5, -50, -101, -44, -17, -61, -18, -66, 105, -11, -1, 1,
			-51, -110, -47, -53, -91, 109, 92, 91, 86, -6, 53, 108, 49, 71,
			-126, 40, -111, 92, -25, -40, 75, -37, 88, 9, -7, -97, -59, 49, 74,
			-58, -23, -82, -29, 45, 94, -39, -78, -43, 86, 76, -77, 38, -115,
			38, -6, -55, 12, -1, 111, 58, 51, -71, -77, 77, -38, 124, -87, -20,
			57, -2, -124, 41, 81, -32, -57, -18, 8, -42, 42, -19, 123, -53, 68,
			-103, -19, 82, -89, 57, 74, 123, 75, -30, -77, 118, 103, -61, 36,
			-35, -11, -69, -87, 30, 87, -60, -98, 79, -4, -51, -115, -36, -21,
			-12, 111, 115, -74, 67, 35, 115, 97, 109, 91, -95, -117, -33, 78,
			44, 51, -40, 110, -97, 69, -15, 14, 21, -120, -7, 85, 81, 88, 121,
			55, -39, 62, 102, 118, -81, 96, -114, -18, 38, 105, -3, 102, -34,
			91, 117, 125, 108, 7, -126, -53, -35, 63, -68, -86, 120, 81, -37,
			87, -64, 68, -39, -39, -102, -128, 118, -12, -106, 64, 108, -85,
			-61, -14, -119, 72, -7, -40, 98, 90, -50, 51, 115, -59, 103, -72,
			-74, -118, 33, 81, 67, -84, -127, -3, 46, 74, 121, 6, -23, 35, 127,
			63, 102, -90, 93, -35, 79, 17, 30, 124, -112, -16, 72, -37, -4, 63,
			93, 0, 14, -128, -50, -37, -19, 19, 32, 43, -23, -87, 18, -83, 101,
			30, 17, -57, -15, 12, -12, -53, 74, 66, 73, -75, -75, -34, -40,
			-79, 120, 88, 86, 103, -6, -39, -113, 102, 64, -49, -86, 38, 26,
			108, 92, -121, -69, 126, 115, -6, 84, 51, -33, 108, -15, 23, -26,
			-2, -85, 114, 22, -8, -111, -100, 45, 51, -100, -71, -23, -52, 89,
			-11, 106, -91, -47, -34, 65, -38, -52, 72, -77, 84, -116, 30, -68,
			51, -98, -14, -27, 46, -114, 94, -41, 83, 122, 52, -32, -53, 109,
			46, 115, 52, -63, -63, -116, 62, 52, -101, 97, 13, 101, 35, 57,
			-112, -100, -5, 98, -80, -16, -39, -77, 59, 121, 108, 127, 66, 108,
			45, -75, 92, -113, -28, 23, -56, -75, -92, -13, 48, 124, 34, -3,
			89, -112, -10, -30, 10, -121, 126, -100, 20, 47, 86, -68, -19, 19,
			-74, -60, -91, 42, -99, 118, 50, -65, 10, -125, 89, -99, 23, -74,
			116, 83, 46, -116, 80, 65, 73, 24, -81, 103, 91, -86, -38, 93, 69,
			-33, -112, 46, 24, -39, -37, 90, -53, 15, -96, -10, -123, 22, -62,
			1, 109, 8, -54, -53, 92, -33, -126, -95, 0, 10, 64, -123, -1, 4,
			-86, 81, 106, -86, 121, -114, -62, -45, 33, 40, 70, 84, 121, 16,
			65, 7 };
}