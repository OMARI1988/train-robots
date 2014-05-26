/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.renderer.demo;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.trainrobots.scenes.Position;
import com.trainrobots.ui.renderer.scene.SceneElement;

public class RobotControlDialog extends JDialog implements ActionListener {
	private JScrollBar[] m_scrollbars = {
			new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 8), // x scroll bar.
			new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 8), // y scroll bar.
			new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 8) // z scroll bar.
	};
	private JButton[] m_buttons = { new JButton("Grap/Release"),
			new JButton("Move") };

	private SceneElement scene;

	public RobotControlDialog(Frame parent, SceneElement scene) {
		super(parent);

		this.scene = scene;

		setSize(400, 200);
		setPreferredSize(new Dimension(400, 200));
		setLayout(new GridLayout(4, 1));

		String[] labels = { "x: ", "y: ", "z: " };

		for (int i = 0; i < labels.length; ++i) {
			JPanel s = new JPanel();
			s.setLayout(new GridLayout(1, 2));
			s.add(new JLabel(labels[i]));
			s.add(m_scrollbars[i]);
			add(s);
		}

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(m_buttons[0]);
		m_buttons[0].addActionListener(this);
		p.add(m_buttons[1]);
		m_buttons[1].addActionListener(this);
		add(p);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == m_buttons[0]) {
			scene.toggleGripper();
		} else if (e.getSource() == m_buttons[1]) {
			Position p = new Position(m_scrollbars[0].getValue(),
					m_scrollbars[1].getValue(), m_scrollbars[2].getValue());
			scene.moveArm(p);
		}
	}
}
