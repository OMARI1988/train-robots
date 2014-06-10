/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Losr;

public class CommandWriter implements AutoCloseable {

	private final XMLStreamWriter writer;

	public CommandWriter(OutputStream stream) {
		try {
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
					stream, "UTF-8");
		} catch (Exception exception) {
			throw new RoboticException(exception);
		}
	}

	public void write(Commands commands) {
		String newLine = System.getProperty("line.separator");
		try {
			writer.writeStartElement("commands");
			writer.writeCharacters(newLine);
			for (Command command : commands) {
				Losr losr = command.losr();
				if (losr == null) {
					continue;
				}
				writer.writeCharacters("\t");
				writer.writeEmptyElement("command");
				writer.writeAttribute("id", Integer.toString(command.id()));
				writer.writeAttribute("losr", losr.toString());
				writer.writeCharacters(newLine);
			}
			writer.writeEndElement();
		} catch (XMLStreamException exception) {
			throw new RoboticException(exception);
		}
	}

	@Override
	public void close() {
		try {
			writer.close();
		} catch (XMLStreamException exception) {
			throw new RoboticException(exception);
		}
	}
}