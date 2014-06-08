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
import com.trainrobots.collections.ItemsArray;
import com.trainrobots.losr.Action;
import com.trainrobots.losr.Actions;
import com.trainrobots.losr.Cardinal;
import com.trainrobots.losr.Color;
import com.trainrobots.losr.Colors;
import com.trainrobots.losr.Entity;
import com.trainrobots.losr.Event;
import com.trainrobots.losr.Indicator;
import com.trainrobots.losr.Indicators;
import com.trainrobots.losr.Losr;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.TextContext;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.ui.visualization.PartialTree;
import com.trainrobots.ui.visualization.visuals.Header;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Token;

public class Editor {

	private final LosrView view;

	public Editor(LosrView view) {
		this.view = view;
	}

	public <T extends Losr> void add(Class<T> type) {

		// Selection.
		Items<Text> selection = view.selection();
		if (selection == null) {
			return;
		}

		// Losr.
		Losr losr = losr(type, selection);

		// Add.
		PartialTree partialTree = view.partialTree();
		partialTree.add(losr);
		view.redrawTree();
	}

	public void delete() {

		// Selection.
		Items<Text> selection = view.selection();
		if (selection == null) {
			return;
		}

		// Single node.
		if (selection.count() != 1) {
			throw new RoboticException("Can't delete multiple nodes.");
		}
		Text text = selection.get(0);

		// Header.
		if (!(text instanceof Header)) {
			throw new RoboticException("Header node not selected.");
		}
		Losr losr = ((Header) text).losr();

		// Remove.
		view.partialTree().remove(losr);
		view.redrawTree();
	}

	private static <T extends Losr> T losr(Class<T> type, Items<Text> selection) {

		if (type == Event.class) {
			return (T) new Event(0, 0, items(selection));
		}

		if (type == SpatialRelation.class) {
			return (T) new SpatialRelation(0, 0, items(selection));
		}

		if (type == Entity.class) {
			return (T) new Entity(0, 0, items(selection));
		}

		return terminal(context(selection), type);
	}

	private static <T extends Losr> T terminal(TextContext context,
			Class<T> type) {

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

	private static Items<Losr> items(Items<Text> selection) {
		int size = selection.count();
		Losr[] result = new Losr[size];
		for (int i = 0; i < size; i++) {
			Text item = selection.get(i);
			if (!(item instanceof Header)) {
				throw new RoboticException(
						"Selection should contain only headers.");
			}
			result[i] = ((Header) item).losr();
		}
		return new ItemsArray(result);
	}

	private static TextContext context(Items<Text> selection) {

		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		int size = selection.count();
		for (int i = 0; i < size; i++) {
			Text item = selection.get(i);
			if (!(item instanceof Token)) {
				throw new RoboticException(
						"Selection should contain only tokens.");
			}
			Token token = (Token) item;
			int id = token.id();
			if (id < min) {
				min = id;
			}
			if (id > max) {
				max = id;
			}
		}

		if (max - min + 1 != size) {
			throw new RoboticException("Contiguous tokens not selected.");
		}

		return new TextContext(min, max);
	}
}