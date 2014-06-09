/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr.factory;

import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;

@FunctionalInterface
public interface TerminalBuilder {

	public Terminal build(TextContext context, String content);
}