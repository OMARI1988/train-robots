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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class GraphicsPanel extends JPanel {

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		render((Graphics2D) graphics, getWidth(), getHeight());
	}

	private void render(Graphics2D graphics, int width, int height) {

		// Anti-aliasing.
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Fractional metrics.
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		// Board.
		Color color1 = new Color(1, 161, 255);
		Color color2 = new Color(183, 255, 252);
		int s = width / 8;
		int p = (width - (s * 8)) / 2;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				graphics.setPaint(x % 2 == y % 2 ? color1 : color2);
				graphics.fillRect(p + s * x, p + s * y, s, s);
			}
		}
	}
}