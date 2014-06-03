/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.xml.sax.Attributes;

import com.trainrobots.Robotics;
import com.trainrobots.XmlReader;
import com.trainrobots.losr.Losr;

public class TreebankTests {

	private int count = 0;

	@Test
	public void shouldWriteTreebank() {

		// Read.
		Commands commands = Robotics.system().commands();
		new XmlReader() {
			protected void handleElementStart(String name, Attributes attributes) {
				if (name.equals("command")) {

					// Expected.
					int commandId = Integer.parseInt(attributes.getValue("id"));
					String format = attributes.getValue("losr");

					// Actual.
					Losr losr = commands.command(commandId).losr();
					String actual = losr.toString();

					// Verify.
					if (!actual.equals(format)) {
						System.out.println("Expected: " + format);
						System.out.println("Actual:   " + actual);
						assertThat(losr.toString(), is(format));
					}
					count++;
				}
			}
		}.read("../.data/losr.xml");

		// Verify count.
		int expectedCount = 0;
		for (Command command : commands) {
			if (command.losr() != null) {
				expectedCount++;
			}
		}
		assertThat(count, is(expectedCount));
	}
}