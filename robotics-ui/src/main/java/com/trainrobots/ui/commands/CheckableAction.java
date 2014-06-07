/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.commands;

import java.awt.event.ActionEvent;
import java.util.function.BooleanSupplier;

import javax.swing.AbstractAction;

public class CheckableAction extends AbstractAction {

	private final Checkable checkable;
	private final BooleanSupplier value;

	public CheckableAction(Checkable checkable, BooleanSupplier value) {
		this.checkable = checkable;
		this.value = value;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		checkable.execute(value.getAsBoolean());
	}
}