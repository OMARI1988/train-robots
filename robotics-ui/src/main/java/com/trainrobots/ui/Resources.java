/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.trainrobots.RoboticException;

public class Resources {

	private static final Class resourceType = new Resources().getClass();

	private Resources() {
	}

	public static URL url(String resourcePath) {
		URL url = resourceType.getResource(resourcePath);
		if (url == null) {
			throw new RoboticException("The resource '%s' was not found.",
					resourcePath);
		}
		return url;
	}

	public static InputStream open(String resourcePath) {
		InputStream stream = resourceType.getResourceAsStream(resourcePath);
		if (stream == null) {
			throw new RoboticException("The resource '%s' was not found.",
					resourcePath);
		}
		return stream;
	}

	public static byte[] resource(String resourcePath) {
		try (final InputStream input = open(resourcePath)) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024 * 8];
			int size = -1;
			while ((size = input.read(buffer)) != -1) {
				output.write(buffer, 0, size);
			}
			return output.toByteArray();
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}
}
