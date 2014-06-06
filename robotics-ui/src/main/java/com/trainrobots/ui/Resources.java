/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui;

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
}
