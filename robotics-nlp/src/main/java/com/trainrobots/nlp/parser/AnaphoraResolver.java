/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.parser;

import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Sequence;
import com.trainrobots.losr.Types;

public class AnaphoraResolver {

	private Entity last;

	public void resolve(Losr losr) {
		losr.visit(x -> {
			if (x instanceof Entity) {
				Entity entity = (Entity) x;
				Types type = entity.type();
				if (type == Types.Reference || type == Types.TypeReference
						|| type == Types.TypeReferenceGroup) {
					if (losr instanceof Sequence) {
						Sequence sequence = (Sequence) losr;
						map(entity, ((Event) sequence.get(0)).entity());
					} else if (last != null) {
						map(entity, last);
					}
				}
				last = entity;
			}
		});
	}

	private void map(Entity entity, Entity reference) {
		reference.id(1);
		entity.referenceId(1);
	}
}