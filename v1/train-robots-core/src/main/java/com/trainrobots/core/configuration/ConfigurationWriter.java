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

package com.trainrobots.core.configuration;

import com.trainrobots.core.io.FileWriter;

public class ConfigurationWriter {

	private ConfigurationWriter() {
	}

	public static void write(String filename,
			Iterable<Configuration> configurations) {

		// Open file.
		FileWriter writer = new FileWriter(filename, "UTF-8");

		// Write.
		for (Configuration configuration : configurations) {

			// Header.
			writer.write(configuration.groupNumber);
			writer.write('\t');
			writer.write(configuration.imageNumber);
			writer.writeLine();

			// Content.
			writer.writeLine(configuration.toString());
			writer.writeLine();
		}

		// Close file.
		writer.close();
	}
}