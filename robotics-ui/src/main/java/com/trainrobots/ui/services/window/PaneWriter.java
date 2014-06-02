/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.window;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.ui.views.PaneView;

public class PaneWriter {

	private final Items<PaneView> panes;

	public PaneWriter(Items<PaneView> panes) {
		this.panes = panes;
	}

	public void write(String filename) {
		try {

			// Document.
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document document = builder.newDocument();
			Element layoutElement = document.createElement("panes");
			document.appendChild(layoutElement);
			for (PaneView pane : panes) {
				Element paneElement = document.createElement(pane.paneType());
				layoutElement.appendChild(paneElement);
				paneElement.setAttribute("x",
						Integer.toString(pane.getLocation().x));
				paneElement.setAttribute("y",
						Integer.toString(pane.getLocation().y));
				paneElement.setAttribute("width",
						Integer.toString(pane.getWidth()));
				paneElement.setAttribute("height",
						Integer.toString(pane.getHeight()));
			}

			// Transformer.
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");

			// Write.
			DOMSource domSource = new DOMSource(document);
			Writer writer = new OutputStreamWriter(new FileOutputStream(
					filename));
			transformer.transform(domSource, new StreamResult(writer));
			writer.close();

		} catch (Exception exception) {
			throw new RoboticException(exception);
		}
	}
}