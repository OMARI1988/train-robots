/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.web;

import javax.servlet.ServletContext;

import com.trainrobots.Log;
import com.trainrobots.treebank.Treebank;

public class Application {

	private final Treebank treebank;

	public Application(ServletContext context) {

		// Diagnostics.
		Log.toConsole();
		Log.info("===============================================================================");
		Log.info("Starting application...");

		// Treebank.
		treebank = new Treebank(context.getInitParameter("treebank-path"));

		// Register with context.
		context.setAttribute("application", this);
	}

	public Treebank treebank() {
		return treebank;
	}

	public static Application get(ServletContext context) {
		return (Application) context.getAttribute("application");
	}
}