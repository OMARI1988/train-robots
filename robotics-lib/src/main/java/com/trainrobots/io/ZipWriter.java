/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.io;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

import com.trainrobots.RoboticException;

public class ZipWriter implements AutoCloseable {

	private final FileSystem fileSystem;

	public ZipWriter(String filename) {
		try {
			fileSystem = FileSystems.newFileSystem(Paths.get(filename), null);
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}

	public ZipEntryWriter open(String path) {
		return new ZipEntryWriter(fileSystem.getPath(path));
	}

	@Override
	public void close() {
		try {
			fileSystem.close();
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}
}