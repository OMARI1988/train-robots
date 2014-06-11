/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation;

import com.trainrobots.RoboticException;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Types;

public class AboveWithinRule {

	public void validate(Losr root) {
		root.visit(x -> {
			if (x instanceof SpatialRelation) {
				validate(root, (SpatialRelation) x);
			}
		});
	}

	private void validate(Losr root, SpatialRelation spatialRelation) {
		Relations relation = spatialRelation.relation();
		if (relation == Relations.Above || relation == Relations.Within) {

			Types type = spatialRelation.entity().type();
			if (type == Types.TypeReference || type == Types.TypeReferenceGroup) {

				Losr resolved = root.find(spatialRelation.entity()
						.referenceId());
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