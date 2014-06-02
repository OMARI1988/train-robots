/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.ui.views;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class LayoutPanel extends JPanel {

	protected static final String TOP = SpringLayout.NORTH;
	protected static final String BOTTOM = SpringLayout.SOUTH;
	protected static final String LEFT = SpringLayout.WEST;
	protected static final String RIGHT = SpringLayout.EAST;
	protected static final String VERTICAL_CENTER = SpringLayout.VERTICAL_CENTER;
	protected static final String HORIZONTAL_CENTER = SpringLayout.HORIZONTAL_CENTER;
	protected static final String BASELINE = SpringLayout.BASELINE;

	public LayoutPanel() {
		setLayout(new SpringLayout());
	}

	protected void setEdge(Component component, String edge, int distance,
			Component baseComponent, String baseEdge) {

		// Get layout.
		SpringLayout layout = (SpringLayout) getLayout();

		// Set edge constraint.
		layout.putConstraint(edge, component, distance, baseEdge, baseComponent);
	}

}