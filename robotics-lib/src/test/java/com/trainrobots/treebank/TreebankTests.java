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

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.xml.sax.Attributes;

import com.trainrobots.Context;
import com.trainrobots.io.XmlReader;
import com.trainrobots.io.ZipReader;
import com.trainrobots.losr.Losr;

public class TreebankTests {

	private int count = 0;

	@Test
	public void shouldWriteLosr() throws IOException {

		// Test.
		Commands commands = Context.treebank().commands();
		try (ZipReader zip = new ZipReader("../.data/treebank.zip")) {
			try (InputStream stream = zip.open("losr.xml")) {
				test(commands, stream);
			}
		}

		// Verify count.
		int expectedCount = 0;
		for (Command command : commands) {
			if (command.losr() != null) {
				expectedCount++;
			}
		}
		assertThat(count, is(expectedCount));
	}

	private void test(Commands commands, InputStream stream) {
		new XmlReader() {
			protected void handleElementStart(String name, Attributes attributes) {
				if (name.equals("command")) {

					// Expected.
					int commandId = Integer.parseInt(attributes.getValue("id"));
					String expected = attributes.getValue("losr");

					// Actual.
					Losr losr = commands.command(commandId).losr();
					String actual = losr.toString();

					// Verify.
					if (!actual.equals(expected)) {
						System.out.println("Expected: " + expected);
						System.out.println("Actual:   " + actual);
						assertThat(losr.toString(), is(expected));
					}
					count++;
				}
			}
		}.read(stream);
	}
}