/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.command.CommandAware;

public class CommentsView extends PaneView implements CommandAware {

	private final JTextArea textArea;
	private Command command;

	public CommentsView() {
		super("Comments");

		// Initiate.
		setLayout(new BorderLayout());

		// Text.
		textArea = new JTextArea();
		textArea.setBackground(new Color(254, 249, 188));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		add(new JScrollPane(textArea), BorderLayout.CENTER);

		// Listener.
		textArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent event) {
				commit();
			}

			@Override
			public void removeUpdate(DocumentEvent event) {
				commit();
			}

			@Override
			public void changedUpdate(DocumentEvent event) {
				commit();
			}
		});
	}

	@Override
	public String paneType() {
		return "comments";
	}

	@Override
	public void bindTo(Command command) {
		commit();
		this.command = command;
		String comment = command.comment();
		textArea.setText(comment);
	}

	private void commit() {
		if (command == null) {
			return;
		}
		String comment = textArea.getText();
		if (comment != null) {
			comment = comment.trim();
		}
		if (comment != null && comment.length() > 0) {
			command.comment(comment);
		} else {
			command.comment(null);
		}
	}
}