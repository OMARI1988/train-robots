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
import com.trainrobots.losr.Destination;
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
import com.trainrobots.ui.visualization.visuals.Detail;
import com.trainrobots.ui.visualization.visuals.Header;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Token;

public class Editor {

	private final LosrView view;
	private final Popup popup;

	public Editor(LosrView view, Popup popup) {
		this.view = view;
		this.popup = popup;
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

	public void change() {

		// Selection.
		Items<Text> selection = view.selection();
		if (selection == null) {
			return;
		}

		// Single node.
		if (selection.count() != 1) {
			throw new RoboticException("Can't change multiple nodes.");
		}
		Text text = selection.get(0);

		// Detail.
		if (!(text instanceof Detail)) {
			return;
		}
		Detail detail = (Detail) text;
		Losr losr = detail.header().losr();

		if (losr instanceof Action) {
			popup.show(detail, (Object[]) Actions.values(),
					((Action) losr).action());
			return;
		}

		if (losr instanceof Color) {
			popup.show(detail, (Object[]) Colors.values(),
					((Color) losr).color());
			return;
		}

		if (losr instanceof Type) {
			popup.show(detail, (Object[]) Types.values(), ((Type) losr).type());
			return;
		}

		if (losr instanceof Relation) {
			popup.show(detail, (Object[]) Relations.values(),
					((Relation) losr).relation());
			return;
		}

		if (losr instanceof Indicator) {
			popup.show(detail, (Object[]) Indicators.values(),
					((Indicator) losr).indicator());
			return;
		}
	}

	public void acceptChange(Detail detail, Object value) {

		Losr losr = detail.header().losr();

		if (losr instanceof Action) {
			Action action = (Action) losr;
			action.action((Actions) value);
			view.redrawTree();
			return;
		}

		if (losr instanceof Color) {
			Color color = (Color) losr;
			color.color((Colors) value);
			view.redrawTree();
			return;
		}

		if (losr instanceof Type) {
			Type type = (Type) losr;
			type.type((Types) value);
			view.redrawTree();
			return;
		}

		if (losr instanceof Relation) {
			Relation relation = (Relation) losr;
			relation.relation((Relations) value);
			view.redrawTree();
			return;
		}

		if (losr instanceof Indicator) {
			Indicator indicator = (Indicator) losr;
			indicator.indicator((Indicators) value);
			view.redrawTree();
			return;
		}
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

		if (type == Destination.class) {
			return (T) new Destination(0, 0, items(selection));
		}

		if (type == Action.class) {
			return (T) new Action(context(selection), Actions.Take);
		}

		if (type == Cardinal.class) {
			return (T) new Cardinal(context(selection), 1);
		}

		if (type == Color.class) {
			return (T) new Color(context(selection), Colors.Red);
		}

		if (type == Indicator.class) {
			return (T) new Indicator(context(selection), Indicators.Left);
		}

		if (type == Relation.class) {
			return (T) new Relation(context(selection), Relations.Above);
		}

		if (type == Type.class) {
			return (T) new Type(context(selection), Types.Cube);
		}

		throw new RoboticException("Can't create %s.", type.getSimpleName());
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