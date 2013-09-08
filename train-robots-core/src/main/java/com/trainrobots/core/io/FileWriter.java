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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import com.trainrobots.core.CoreException;

public class FileWriter {

	private final PrintStream printStream;

	public FileWriter(String filename) {
		FileOutputStream stream;
		try {
			stream = new FileOutputStream(filename);
		} catch (FileNotFoundException exception) {
			throw new CoreException(exception);
		}
		printStream = new PrintStream(stream);
	}

	public FileWriter(String filename, String encoding) {
		FileOutputStream stream;
		try {
			stream = new FileOutputStream(filename);
		} catch (FileNotFoundException exception) {
			throw new CoreException(exception);
		}
		try {
			printStream = new PrintStream(stream, false, encoding);
		} catch (UnsupportedEncodingException exception) {
			throw new CoreException(exception);
		}
	}

	public void write(char ch) {
		printStream.print(ch);
	}

	public void write(int value) {
		printStream.print(value);
	}

	public void write(String text) {
		printStream.print(text);
	}

	public void writeLine() {
		printStream.println();
	}

	public void writeLine(String text) {
		printStream.println(text);
	}

	public void close() {
		printStream.close();
	}
}