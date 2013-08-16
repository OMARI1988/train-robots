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

package com.trainrobots.web.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.trainrobots.web.WebException;

public class ImageService {

	private static final String EXTENSION = ".jpg";
	private final List<Integer> imageIds = new ArrayList<Integer>();

	public ImageService() {
		try {
			Context env = (Context) new InitialContext()
					.lookup("java:comp/env");
			String path = (String) env.lookup("static-path");
			File dataFolder = new File(path);
			for (File file : dataFolder.listFiles()) {
				String name = file.getName();
				if (name.endsWith(EXTENSION)) {
					name = name
							.substring(0, name.length() - EXTENSION.length());
					imageIds.add(Integer.parseInt(name));
				}
			}
		} catch (NamingException exception) {
			throw new WebException(exception);
		}
	}

	public String random() {
		return imageIds.get(new Random().nextInt(imageIds.size())) + EXTENSION;
	}
}