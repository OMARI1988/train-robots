/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.themes;

import java.awt.Color;
import java.awt.Font;

public interface Theme {

	Font font();

	Color foreground();
	
	Color background();
	
	Color entity();
	
	Color event();
	
	Color spatialRelation();
	
	Color skip();
}