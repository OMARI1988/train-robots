/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

public class RoboticException extends RuntimeException {

	public RoboticException(String message) {
		super(message);
	}

	public RoboticException(Exception exception) {
		super(exception.getMessage(), exception);
	}

	public RoboticException(String message, Exception exception) {
		super(message, exception);
	}
}