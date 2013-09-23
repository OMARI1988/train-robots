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

package com.trainrobots.core.rcl;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;

public class IndicatorAttribute extends Rcl {

	private final SpatialIndicator indicator;

	public IndicatorAttribute(SpatialIndicator indicator) {
		this.indicator = indicator;
	}

	public static IndicatorAttribute fromString(String text) {
		return fromNode(Node.fromString(text));
	}

	public static IndicatorAttribute fromNode(Node node) {

		if (!node.hasTag("spatial-indicator:")) {
			throw new CoreException("Expected 'spatial-indicator:' not '"
					+ node.tag + "'.");
		}

		SpatialIndicator indicator = SpatialIndicator.valueOf(node.getValue());
		return new IndicatorAttribute(indicator);
	}

	@Override
	public Node toNode() {
		return new Node("spatial-indicator:", indicator.toString());
	}

	@Override
	public String generate() {
		throw new CoreException("NOT_IMPLEMENTED");
	}

	@Override
	public void accept(RclVisitor visitor) {
		throw new CoreException("NOT_IMPLEMENTED");
	}

	public SpatialIndicator indicator() {
		return indicator;
	}
}