/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.treebank;

import javax.swing.SwingWorker;

import com.trainrobots.Log;
import com.trainrobots.nlp.grammar.Grammar;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.treebank.Treebank;
import com.trainrobots.ui.services.window.WindowService;

public class TreebankService {

	private final WindowService windowService;
	private final Treebank treebank;
	private final Lexicon lexicon;
	private final Grammar grammar;

	public TreebankService(WindowService windowService) {
		this.windowService = windowService;
		this.treebank = new Treebank("../.data/treebank.zip");
		this.lexicon = new Lexicon(treebank);
		this.grammar = new Grammar(treebank);
	}

	public Treebank treebank() {
		return treebank;
	}

	public Lexicon lexicon() {
		return lexicon;
	}

	public Grammar grammar() {
		return grammar;
	}

	public void save() {
		windowService.status("Saving...");
		new SwingWorker<Exception, Void>() {
			protected Exception doInBackground() throws Exception {
				try {
					treebank.write();
				} catch (Exception exception) {
					return exception;
				}
				return null;
			}

			public void done() {
				try {
					Exception exception = get();
					if (exception != null) {
						windowService.error(exception.getMessage());
					} else {
						windowService.status("Saved");
					}
				} catch (Exception exception) {
					Log.error("Failed to save.", exception);
				}
			}
		}.execute();
	}
}