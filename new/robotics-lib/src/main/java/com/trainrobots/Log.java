/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import java.io.OutputStreamWriter;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log {

	private static Logger logger = Logger.getRootLogger();

	private Log() {
	}

	public static void configureConsole() {

		// Already configured?
		if (logger.getAllAppenders().hasMoreElements()) {
			return;
		}

		// Console appender.
		ConsoleAppender appender = new ConsoleAppender();
		appender.setWriter(new OutputStreamWriter(System.out));
		appender.setLayout(new PatternLayout(
				"%d{yyyy-MM-dd HH:mm:ss.SSS} [%p] [%t] %m%n"));
		logger.addAppender(appender);
	}

	public static void debug(String message) {
		logger.debug(message);
	}

	public static void debug(String format, Object... parameters) {
		logger.debug(String.format(format, parameters));
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void info(String format, Object... parameters) {
		logger.info(String.format(format, parameters));
	}

	public static void warn(String message) {
		logger.warn(message);
	}

	public static void warn(String format, Object... parameters) {
		logger.warn(String.format(format, parameters));
	}

	public static void error(String message) {
		logger.error(message);
	}

	public static void error(String message, Exception exception) {
		logger.error(message, exception);
	}

	public static void error(String format, Object... parameters) {
		logger.error(String.format(format, parameters));
	}
}