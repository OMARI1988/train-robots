/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation;

import com.trainrobots.treebank.Command;

public class ValidationResult {

	private final Command command;
	private final String message;

	public ValidationResult(Command command, String message) {
		this.command = command;
		this.message = message;
	}

	public Command command() {
		return command;
	}

	public String message() {
		return message;
	}
}