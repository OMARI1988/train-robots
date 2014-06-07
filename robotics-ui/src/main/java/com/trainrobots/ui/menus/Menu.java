/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.menus;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.trainrobots.ui.commands.Checkable;
import com.trainrobots.ui.commands.CheckableAction;
import com.trainrobots.ui.commands.Executable;
import com.trainrobots.ui.commands.ExecutableAction;

public abstract class Menu extends JMenu {

	protected Menu(String text, char mnemonic) {
		super(text);
		setMnemonic(mnemonic);
	}

	protected void addItem(String name, String shortCutKey,
			Executable executable) {

		// Item.
		JMenuItem item = new JMenuItem(name);
		shortCutKey(item, shortCutKey);

		// Action.
		if (executable != null) {
			item.addActionListener(new ExecutableAction(executable));
		}

		// Add.
		add(item);
	}

	protected void addCheckedItem(String name, String shortCutKey,
			Checkable checkable, boolean checked) {

		// Item.
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(name, checked);
		shortCutKey(item, shortCutKey);

		// Action.
		if (checkable != null) {
			item.addActionListener(new CheckableAction(checkable, () -> item
					.isSelected()));
		}

		// Add.
		add(item);
	}

	private void shortCutKey(JMenuItem item, String shortCutKey) {
		if (shortCutKey != null) {
			if (shortCutKey.length() == 1) {
				item.setMnemonic(shortCutKey.charAt(0));
			} else {
				item.setAccelerator(KeyStroke.getKeyStroke(shortCutKey));
			}
		}
	}
}