/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class ZipArchive implements AutoCloseable {

	private final ZipFile zipFile;

	public ZipArchive(String filename) {
		try {
			zipFile = new ZipFile(filename);
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	public InputStream open(String entryName) {
		try {
			return zipFile.getInputStream(zipFile.getEntry(entryName));
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	@Override
	public void close() {
		try {
			zipFile.close();
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}
}