/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.visualization.visuals;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Action;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.ui.visualization.VisualContext;
import com.trainrobots.ui.visualization.themes.Theme;

public class Header extends Text {

	private final Losr losr;
	private final DetailList details;

	private Header(VisualContext context, Losr losr, String text,
			java.awt.Color color) {
		super(context, text, context.theme().font(), color);
		this.losr = losr;

		// No details?
		Theme theme = context.theme();
		if (!theme.showDetails()) {
			details = null;
			return;
		}

		// ID.
		details = new DetailList(context, this);
		if (losr.id() != 0) {
			details.add(new IdDetail(context, this, losr.id()));
		}
		if (losr.referenceId() != 0) {
			details.add(new ReferenceIdDetail(context, this, losr.referenceId()));
		}

		// Action.
		if (losr instanceof Action) {
			details.add(((Action) losr).action().toString());
			return;
		}

		// Color.
		if (losr instanceof Color) {
			details.add(((Color) losr).color().toString());
			return;
		}

		// Indicator.
		if (losr instanceof Indicator) {
			details.add(((Indicator) losr).indicator().toString());
			return;
		}

		// Relation.
		if (losr instanceof Relation) {
			details.add(((Relation) losr).relation().toString());
			return;
		}

		// Type.
		if (losr instanceof Type) {
			details.add(((Type) losr).type().toString());
			return;
		}
	}

	public static Header from(VisualContext context, Losr losr) {

		// Text and color.
		Theme theme = context.theme();
		String text = losr.name();
		java.awt.Color color = theme.foreground();

		// Overrides.
		if (losr instanceof SpatialRelation) {
			color = theme.spatialRelation();
			text = "sp-relation";
		} else if (losr instanceof Entity) {
			color = theme.entity();
		} else if (losr instanceof Event) {
			color = theme.event();
		} else if (!theme.showDetails() && losr instanceof Type
				&& ((Type) losr).type() == Types.Reference) {
			text = "reference";
		}

		// Header.
		return new Header(context, losr, text, color);
	}

	public Losr losr() {
		return losr;
	}

	public Items<Detail> details() {
		return details;
	}
}