/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;

import com.trainrobots.RoboticException;
import com.trainrobots.ui.commands.Executable;
import com.trainrobots.ui.commands.ExecutableAction;

public abstract class PaneView extends JInternalFrame {

	protected PaneView(String title) {
		super(title, true, true, true, false);
	}

	public abstract String paneType();

	public boolean alwaysBehind() {
		return false;
	}

	public void focus() {
		try {
			setSelected(true);
		} catch (PropertyVetoException exception) {
			throw new RoboticException(exception);
		}
	}

	protected void bindKey(String key, Executable executable) {
		getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(key), key);
		getActionMap().put(key, new ExecutableAction(executable));
	}
}