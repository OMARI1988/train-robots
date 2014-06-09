/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.losr.factory;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Losr;

@FunctionalInterface
public interface NonTerminalBuilder {

	public Losr build(int id, int referenceId, Items<Losr> children);
}