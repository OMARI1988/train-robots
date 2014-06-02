/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import org.xml.sax.Attributes;

import com.trainrobots.XmlReader;

public class LosrReader extends XmlReader {

	private final Commands commands;
	private int count;

	public LosrReader(Commands commands) {
		this.commands = commands;
	}

	public int count() {
		return count;
	}

	@Override
	protected void handleElementStart(String name, Attributes attributes) {

		if (name.equals("command")) {
			int id = Integer.parseInt(attributes.getValue("id"));
			commands.command(id).setLosr(attributes.getValue("losr"));
			count++;
		}
	}
}