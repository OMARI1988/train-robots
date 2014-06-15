/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation.rules;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Types;
import com.trainrobots.treebank.Command;

public class AboveOrWithinRule implements ValidationRule {

	@Override
	public String name() {
		return "above/within";
	}

	@Override
	public void validate(Command command) {
		Losr losr = command.losr();
		if (losr != null) {
			validate(losr);
		}
	}

	public void validate(Losr root) {
		root.visit(x -> {
			if (x instanceof SpatialRelation) {
				validate(root, (SpatialRelation) x);
			}
		});
	}

	public void validate(Losr root, SpatialRelation spatialRelation) {
		Relations relation = spatialRelation.relation();
		if (relation == Relations.Above || relation == Relations.Within) {

			Types type = spatialRelation.entity().type();
			if (type == Types.TypeReference || type == Types.TypeReferenceGroup) {
				Losr resolved = root.find(spatialRelation.entity()
						.referenceId());
				if (resolved == null) {
					return;
				}
				type = ((Entity) resolved).type();
			}

			boolean b1 = relation == Relations.Within;
			boolean b2 = type == Types.Corner || type == Types.Region;

			if (b1 != b2) {
				throw new RoboticException("Relation mismatch: %s %s",
						relation, type);
			}
		}
	}
}