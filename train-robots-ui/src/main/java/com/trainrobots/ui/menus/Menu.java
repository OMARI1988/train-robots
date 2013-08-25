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

package com.trainrobots.ui.menus;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Menu extends JMenu {

	protected JMenuItem addItem(String name, String shortCutKey, Action action) {

		// Create item.
		JMenuItem item = new JMenuItem(name);

		// Short cut key.
		if (shortCutKey != null) {
			if (shortCutKey.length() == 1) {
				item.setMnemonic(shortCutKey.charAt(0));
			} else {
				item.setAccelerator(KeyStroke.getKeyStroke(shortCutKey));
			}
		}

		// Action.
		if (action != null) {
			item.addActionListener(action);
		}

		// Add item.
		add(item);

		// Return item.
		return item;
	}
}