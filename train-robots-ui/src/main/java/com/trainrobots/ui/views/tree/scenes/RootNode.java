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

package com.trainrobots.ui.views.tree.scenes;

import com.trainrobots.ui.services.ConfigurationService;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.tree.TreeNode;

public class RootNode extends TreeNode {

	private final ConfigurationService configurationService;
	private final WindowService windowService;

	public RootNode(ConfigurationService configurationService,
			WindowService windowService) {
		super("Groups", false);
		this.configurationService = configurationService;
		this.windowService = windowService;
	}

	@Override
	protected void createChildNodes() {
		int groupCount = configurationService.getGroupCount();
		for (int i = 1; i <= groupCount; i++) {
			add(new GroupNode(windowService, i));
		}
	}
}
