/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.command;

import com.trainrobots.losr.Losr;
import com.trainrobots.ui.visualization.visuals.Text;

@FunctionalInterface
public interface Popup {

	void show(Text text, Losr losr, Object[] options, Object selected);
}