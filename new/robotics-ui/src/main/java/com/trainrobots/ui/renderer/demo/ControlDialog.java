/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.demo;

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.trainrobots.scenes.Gripper;
import com.trainrobots.scenes.Position;
import com.trainrobots.scenes.Scene;

public class ControlDialog extends JDialog implements ActionListener {

	private final Scene scene;
	private final JScrollBar[] scrollbars;
	private final JButton[] buttons = { new JButton("Open/Close"),
			new JButton("Move") };

	public ControlDialog(Frame parent, Scene scene) {
		super(parent);

		this.scene = scene;

		Position p = scene.gripper().position();
		scrollbars = new JScrollBar[] {
				new JScrollBar(Adjustable.HORIZONTAL, p.x(), 1, 0, 8),
				new JScrollBar(Adjustable.HORIZONTAL, p.y(), 1, 0, 8),
				new JScrollBar(Adjustable.HORIZONTAL, p.z(), 1, 0, 8) };

		setSize(400, 200);
		setPreferredSize(new Dimension(400, 200));
		setLayout(new GridLayout(4, 1));

		String[] labels = { "x: ", "y: ", "z: " };

		for (int i = 0; i < labels.length; ++i) {
			JPanel s = new JPanel();
			s.setLayout(new GridLayout(1, 2));
			s.add(new JLabel(labels[i]));
			s.add(scrollbars[i]);
			add(s);
		}

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(buttons[0]);
		buttons[0].addActionListener(this);
		panel.add(buttons[1]);
		buttons[1].addActionListener(this);
		add(panel);
	}

	public void actionPerformed(ActionEvent e) {
		Gripper gripper = scene.gripper();
		if (e.getSource() == buttons[0]) {
			gripper.open(!gripper.open());
		} else if (e.getSource() == buttons[1]) {
			gripper.position(new Position(scrollbars[0].getValue(),
					scrollbars[1].getValue(), scrollbars[2].getValue()));
		}
	}
}