/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.command;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
import com.trainrobots.losr.Action;
import com.trainrobots.losr.Actions;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Indicators;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.TextContext;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Token;

public class Editor {

	private final LosrView view;

	public Editor(LosrView view) {
		this.view = view;
	}

	public <T extends Losr> void addLosr(Class<T> type) {

		// Selection.
		Items<Text> selection = view.selection();
		if (selection == null) {
			return;
		}

		// Token.
		if (selection.count() == 1 && selection.get(0) instanceof Token) {
			Token token = (Token) selection.get(0);
			if (token.id() != 0) {
				TextContext context = new TextContext(token.id());
				view.partialTree().add(terminal(context, type));
				view.redrawTree();
			}
		}
	}

	private <T extends Losr> T terminal(TextContext context, Class<T> type) {

		if (type == Action.class) {
			return (T) new Action(context, Actions.Take);
		}

		if (type == Cardinal.class) {
			return (T) new Cardinal(context, 1);
		}

		if (type == Color.class) {
			return (T) new Color(context, Colors.Red);
		}

		if (type == Indicator.class) {
			return (T) new Indicator(context, Indicators.Left);
		}

		if (type == Relation.class) {
			return (T) new Relation(context, Relations.Above);
		}

		if (type == Type.class) {
			return (T) new Type(context, Types.Cube);
		}

		throw new RoboticException("Can't create %s terminal.",
				type.getSimpleName());
	}
}