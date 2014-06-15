/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation.rules;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Location;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Marker;
import com.trainrobots.losr.Source;
import com.trainrobots.losr.Terminal;
import com.trainrobots.nlp.lexicon.LexicalKey;
import com.trainrobots.treebank.Command;

public class MarkerRule implements ValidationRule {

	@Override
	public String name() {
		return "marker";
	}

	@Override
	public void validate(Command command) {
		Losr losr = command.losr();
		if (losr == null) {
			return;
		}
		validate(losr, command.tokens());
	}

	public void validate(Losr losr, Items<Terminal> tokens) {
		losr.visit(x -> {
			if (x instanceof Location) {
				Location location = (Location) x;
				Marker marker = location.marker();
				if (marker != null) {
					String key = LexicalKey.key(tokens, marker.context());
					if (location instanceof Source) {
						if (!key.equals("from") && !key.equals("off")) {
							throw new RoboticException(
									"Invalid source marker '%s'.", key);
						}
					} else {
						if (!key.equals("to")) {
							throw new RoboticException(
									"Invalid destination marker '%s'.", key);
						}
					}
				}
			}
		});
	}
}