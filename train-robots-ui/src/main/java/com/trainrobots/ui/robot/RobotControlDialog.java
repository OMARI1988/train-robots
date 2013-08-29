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

package com.trainrobots.ui.robot;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RobotControlDialog extends JDialog implements ActionListener {
	private JScrollBar[] m_scrollbars = {
			new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 8), // x scroll bar.
			new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 8), // y scroll bar.
			new JScrollBar(Adjustable.HORIZONTAL, 0, 1, 0, 8) // z scroll bar.
	};
	private JButton[] m_buttons = { new JButton("Grap/Release"),
			new JButton("Move") };

	private RobotControl m_rbtctrl;

	public RobotControlDialog(Frame parent, RobotControl rc) {
		super(parent);

		m_rbtctrl = rc;

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
			m_rbtctrl.toggleGrasp();
		} else if (e.getSource() == m_buttons[1]) {
			int x = m_scrollbars[0].getValue(), y = m_scrollbars[1].getValue(), z = m_scrollbars[2]
					.getValue();
			m_rbtctrl.moveArm(x, y, z);
		}
	}

}
