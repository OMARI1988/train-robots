/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

public class NotImplementedException extends RuntimeException {

	public NotImplementedException() {
		super("The method or operation is not implemented.");
	}

	public NotImplementedException(String message) {
		super(message);
	}
}