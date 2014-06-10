/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.trainrobots.RoboticException;

public class ZipEntryWriter implements AutoCloseable {

	private final Path path;
	private final ByteArrayOutputStream output = new ByteArrayOutputStream();

	public ZipEntryWriter(Path path) {
		this.path = path;
	}

	public OutputStream output() {
		return output;
	}

	@Override
	public void close() {

		// Data.
		byte[] data = output.toByteArray();

		// Write entry.
		try (ByteArrayInputStream input = new ByteArrayInputStream(data)) {
			Files.copy(input, path, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}
}