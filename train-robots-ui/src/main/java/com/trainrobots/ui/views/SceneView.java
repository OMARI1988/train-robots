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
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.trainrobots.ui.configuration.Block;
import com.trainrobots.ui.configuration.Configuration;
import com.trainrobots.ui.robot.RobotControl;

public class SceneView extends JPanel {

	private final JLabel label1 = new JLabel();
	private final JLabel label2 = new JLabel();
	private final GraphicsPanel panel1 = new GraphicsPanel(325, 350);
	private final GraphicsPanel panel2 = new GraphicsPanel(325, 350);
	private Configuration configuration;

	@Inject
	public SceneView() {

		// Configuration.
		configuration = new Configuration();
		configuration.armX = 3;
		configuration.armY = 2;
		configuration.armZ = 7;
		configuration.gripperOpen = false;
		configuration.blocks = new ArrayList<Block>();
		configuration.blocks.add(new Block(Block.CYAN, Block.CUBE, 0, 0, 0));
		configuration.blocks.add(new Block(Block.RED, Block.CUBE, 1, 2, 0));
		configuration.blocks.add(new Block(Block.YELLOW, Block.CUBE, 2, 4, 0));
		configuration.blocks.add(new Block(Block.GREEN, Block.CUBE, 3, 6, 0));
		configuration.blocks.add(new Block(Block.MAGENTA, Block.CUBE, 4, 7, 0));
		configuration.blocks.add(new Block(Block.GRAY, Block.CUBE, 5, 5, 0));
		configuration.blocks.add(new Block(Block.BLUE, Block.CUBE, 6, 3, 0));
		configuration.blocks.add(new Block(Block.WHITE, Block.CUBE, 0, 5, 0));

		// Layout.
		setLayout(null);
		setBackground(Color.BLACK);

		// Label 1.
		label1.setText("No scene selected");
		label1.setForeground(Color.WHITE);
		Dimension ps1 = label1.getPreferredSize();
		label1.setBounds(25, 15, (int) ps1.getWidth(), (int) ps1.getHeight());
		add(label1);

		// Label 2.
		label2.setText("No scene selected");
		label2.setForeground(Color.WHITE);
		Dimension ps2 = label2.getPreferredSize();
		label2.setBounds(350, 15, (int) ps2.getWidth(), (int) ps2.getHeight());
		add(label2);

		// Panel 1.
		panel1.setBounds(25, 32, 325, 350);
		add(panel1);

		// Panel 2.
		panel2.setBounds(350, 32, 325, 350);
		add(panel2);

		// Mouse.
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (contains(e.getPoint())) {
					requestFocus();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		// Keyboard.
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				handleKey(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
	}

	public void select(int groupNumber, int imageNumber) {
		label1.setText(groupNumber + ".1");
		label2.setText(groupNumber + "." + imageNumber);
	}

	private void handleKey(KeyEvent e) {
		if (!hasFocus()) {
			return;
		}

		RobotControl rc = panel2.getRobotControl();

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (e.isShiftDown()) {
				rc.raiseArm();
			} else {
				rc.moveUp();
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (e.isShiftDown()) {
				rc.lowerArm();
			} else {
				rc.moveDown();
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT && !e.isShiftDown()) {
			rc.moveLeft();
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT && !e.isShiftDown()) {
			rc.moveRight();
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			rc.toggleGrasp();
		}

		if (e.getKeyCode() == KeyEvent.VK_DELETE
				|| e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			rc.remove();
		}

		switch (e.getKeyChar()) {
		case 'b':
			rc.addCube(Block.BLUE);
			break;
		case 'c':
			rc.addCube(Block.CYAN);
			break;
		case 'r':
			rc.addCube(Block.RED);
			break;
		case 'y':
			rc.addCube(Block.YELLOW);
			break;
		case 'g':
			rc.addCube(Block.GREEN);
			break;
		case 'm':
			rc.addCube(Block.MAGENTA);
			break;
		case 'x':
			rc.addCube(Block.GRAY);
			break;
		case 'w':
			rc.addCube(Block.WHITE);
			break;

		case 'B':
			rc.addPyramid(Block.BLUE);
			break;
		case 'C':
			rc.addPyramid(Block.CYAN);
			break;
		case 'R':
			rc.addPyramid(Block.RED);
			break;
		case 'Y':
			rc.addPyramid(Block.YELLOW);
			break;
		case 'G':
			rc.addPyramid(Block.GREEN);
			break;
		case 'M':
			rc.addPyramid(Block.MAGENTA);
			break;
		case 'X':
			rc.addPyramid(Block.GRAY);
			break;
		case 'W':
			rc.addPyramid(Block.WHITE);
			break;
		}
	}
}