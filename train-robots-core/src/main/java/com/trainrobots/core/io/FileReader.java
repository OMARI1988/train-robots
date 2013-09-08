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

package com.trainrobots.core.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.trainrobots.core.CoreException;

public class FileReader {

	private final BufferedReader reader;
	private static final String DEFAULT_ENCODING = "UTF-8";

	public FileReader(String filename) {
		try {
			reader = new BufferedReader(new java.io.FileReader(filename));
		} catch (FileNotFoundException exception) {
			throw new CoreException(exception);
		}
	}

	public FileReader(String filename, String encoding) {
		try {
			FileInputStream inputStream = new FileInputStream(filename);
			InputStreamReader streamReader = new InputStreamReader(inputStream,
					encoding);
			reader = new BufferedReader(streamReader);
		} catch (Exception exception) {
			throw new CoreException(exception);
		}
	}

	public FileReader(InputStream stream) {
		try {
			reader = new BufferedReader(new InputStreamReader(stream,
					DEFAULT_ENCODING));
		} catch (UnsupportedEncodingException exception) {
			throw new CoreException(exception);
		}
	}

	public String readText() {

		// Read the text.
		StringBuilder text = new StringBuilder();
		String line;
		while ((line = readLine()) != null) {
			text.append(line);
			text.append("\r\n");
		}

		// Return text.
		return text.toString();
	}

	public String readLine() {
		String line;
		try {
			line = reader.readLine();
		} catch (IOException exception) {
			throw new CoreException(exception);
		}
		return line;
	}

	public void close() {
		try {
			reader.close();
		} catch (IOException exception) {
			throw new CoreException(exception);
		}
	}
}