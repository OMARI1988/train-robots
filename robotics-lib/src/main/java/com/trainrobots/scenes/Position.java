/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

public class Position {

	private final int value;

	public Position(int x, int y, int z) {
		if (x < -128 || x > 127 || y < -128 || y > 127 || z < -128 || z > 127) {
			throw new IllegalArgumentException(String.format(
					"The position '%d %d %d' is out of range.", x, y, z));
		}
		this.value = (0xFF & x) | ((0xFF & y) << 8) | ((0xFF & z) << 16);
	}

	public int x() {
		return (byte) (value);
	}

	public int y() {
		return (byte) (value >> 8);
	}

	public int z() {
		return (byte) (value >> 16);
	}

	public Position add(int dx, int dy, int dz) {
		return new Position(x() + dx, y() + dy, z() + dz);
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof Position && ((Position) object).value == value;
	}

	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public String toString() {

		// Use a capacity of 5, as most positions will be of the form 'X Y Z',
		// and each component will between 0 and 7 (and hence a single digit).
		StringBuilder text = new StringBuilder(5);
		text.append(x());
		text.append(' ');
		text.append(y());
		text.append(' ');
		text.append(z());
		return text.toString();
	}

	public static Position parse(String text) {
		return new Parser(text).parse();
	}

	private static class Parser {

		private final String text;
		private int i;

		public Parser(String text) {
			if (text == null) {
				throw new IllegalArgumentException(
						"Failed to parse null position.");
			}
			this.text = text;
		}

		public Position parse() {
			int x = readInteger();
			readSpace();
			int y = readInteger();
			readSpace();
			int z = readInteger();
			if (i != text.length()) {
				throw parserException();
			}
			return new Position(x, y, z);
		}

		private int readInteger() {
			int j = i;
			while (i < text.length() && text.charAt(i) != ' ') {
				i++;
			}
			try {
				return Integer.parseInt(text.substring(j, i));
			} catch (Exception exception) {
				throw parserException();
			}
		}

		private void readSpace() {
			if (i >= text.length() || text.charAt(i) != ' ') {
				throw parserException();
			}
			i++;
		}

		private IllegalArgumentException parserException() {
			return new IllegalArgumentException(String.format(
					"The position '%s' is not valid.", text));
		}
	}
}