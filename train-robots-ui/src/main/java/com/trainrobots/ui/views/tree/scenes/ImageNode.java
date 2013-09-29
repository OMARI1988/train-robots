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

import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.views.AnnotationWindow;
import com.trainrobots.ui.views.tree.TreeNode;

public class ImageNode extends TreeNode {

	private final WindowService windowService;
	private final int groupNumber;
	private final int imageNumber;

	public ImageNode(WindowService windowService, int groupNumber,
			int imageNumber) {
		super(getName(groupNumber, imageNumber), true);
		this.windowService = windowService;
		this.groupNumber = groupNumber;
		this.imageNumber = imageNumber;
	}

	@Override
	public void select() {
		windowService.get(AnnotationWindow.class).getSceneView()
				.select(groupNumber, imageNumber);
	}

	private static String getName(int groupNumber, int imageNumber) {
		StringBuilder text = new StringBuilder();
		text.append(groupNumber);
		text.append('.');
		text.append(imageNumber);
		return text.toString();
	}
}
