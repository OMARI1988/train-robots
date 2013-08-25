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

package com.trainrobots.ui.views.tree;

import com.trainrobots.ui.services.WindowService;

public class GroupNode extends TreeNode {

	private final WindowService windowService;
	private final int groupNumber;

	public GroupNode(WindowService windowService, int groupNumber) {
		super(Integer.toString(groupNumber), false);
		this.windowService = windowService;
		this.groupNumber = groupNumber;
	}

	@Override
	protected void createChildNodes() {
		for (int i = 1; i <= 5; i++) {
			add(new ImageNode(windowService, groupNumber, i));
		}
	}
}
