/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr.reader;

import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TokenContext;

@FunctionalInterface
public interface TerminalBuilder {

	public Terminal build(TokenContext context, String content);
}