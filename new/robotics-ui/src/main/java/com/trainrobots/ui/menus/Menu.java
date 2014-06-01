/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.menus;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.trainrobots.ui.commands.Command;
import com.trainrobots.ui.commands.CommandAction;

public abstract class Menu extends JMenu {

	protected Menu(String text, char mnemonic) {
		super(text);
		setMnemonic(mnemonic);
	}

	protected JMenuItem addItem(String name, String shortCutKey, Command command) {

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
		if (command != null) {
			item.addActionListener(new CommandAction(command));
		}

		// Add item.
		add(item);

		// Return item.
		return item;
	}
}