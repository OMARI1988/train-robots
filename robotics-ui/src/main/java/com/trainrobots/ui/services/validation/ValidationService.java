/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.services.validation;

import javax.swing.SwingWorker;

import com.trainrobots.Log;
import com.trainrobots.nlp.losr.PartialTree;
import com.trainrobots.nlp.validation.ValidationResult;
import com.trainrobots.nlp.validation.ValidationResults;
import com.trainrobots.nlp.validation.Validator;
import com.trainrobots.ui.services.command.CommandService;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.views.command.CommandView;

public class ValidationService {

	private final WindowService windowService;
	private final TreebankService treebankService;
	private final CommandService commandService;
	private final Validator validator = new Validator();
	private ValidationResults results;

	public ValidationService(WindowService windowService,
			TreebankService treebankService, CommandService commandService) {
		this.windowService = windowService;
		this.treebankService = treebankService;
		this.commandService = commandService;
	}

	public void validate() {
		windowService.status("Validating...");
		new SwingWorker<ValidationResults, Void>() {

			protected ValidationResults doInBackground() throws Exception {

				// Editor has a partial tree?
				CommandView commandView = windowService.pane(CommandView.class);
				if (commandView != null) {
					PartialTree partialTree = commandView.partialTree();
					if (partialTree.items().count() >= 2) {
						ValidationResults results = new ValidationResults();
						results.add(partialTree.command(),
								"Annotation not complete.");
						return results;
					}
				}

				// Validate.
				return validator.validate(treebankService.treebank());
			}

			public void done() {
				try {
					results = get();
					handleResults();
				} catch (Exception exception) {
					Log.error("Failed to validate commands.", exception);
				}
			}
		}.execute();
	}

	public void navigate() {

		// Go to first error.
		int size = results.count();
		if (size > 0) {
			commandService.command(results.get(0).command());
		}
	}

	private void handleResults() {

		// No errors?
		int size = results.count();
		if (size == 0) {
			windowService.status("Validated successfully.");
			return;
		}

		// Display first error.
		ValidationResult result = results.get(0);
		windowService.error("%d %s. Command %d: %s", size, size == 1 ? "error"
				: "errors", result.command().id(), result.message());
	}
}