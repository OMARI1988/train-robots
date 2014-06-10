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

public class CommandWriter {

	private final Commands commands;

	public CommandWriter(Commands commands) {
		this.commands = commands;
	}

	public void writeLosr(OutputStream stream) {
		String newLine = System.getProperty("line.separator");
		try {
			XMLStreamWriter writer = XMLOutputFactory.newInstance()
					.createXMLStreamWriter(stream, "UTF-8");
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
			writer.close();
		} catch (XMLStreamException exception) {
			throw new RoboticException(exception);
		}
	}

	public void writeComments(OutputStream stream) {
		String newLine = System.getProperty("line.separator");
		try {
			XMLStreamWriter writer = XMLOutputFactory.newInstance()
					.createXMLStreamWriter(stream, "UTF-8");
			writer.writeStartElement("commands");
			writer.writeCharacters(newLine);
			for (Command command : commands) {
				String comment = command.comment();
				if (comment == null) {
					continue;
				}
				writer.writeCharacters("\t");
				writer.writeEmptyElement("command");
				writer.writeAttribute("id", Integer.toString(command.id()));
				writer.writeAttribute("comment", comment);
				writer.writeCharacters(newLine);
			}
			writer.writeEndElement();
			writer.close();
		} catch (XMLStreamException exception) {
			throw new RoboticException(exception);
		}
	}
}