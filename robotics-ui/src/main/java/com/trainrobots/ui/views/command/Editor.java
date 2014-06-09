/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.ui.views.command;

import static com.trainrobots.nlp.lexicon.LexicalKey.key;

import com.trainrobots.RoboticException;
import com.trainrobots.collections.Items;
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
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.visualization.PartialTree;
import com.trainrobots.ui.visualization.visuals.Detail;
import com.trainrobots.ui.visualization.visuals.Header;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Token;

public class Editor {

	private final TreebankService treebankService;
	private final LosrView view;
	private final Popup popup;

	public Editor(TreebankService treebankService, LosrView view, Popup popup) {
		this.treebankService = treebankService;
		this.view = view;
		this.popup = popup;
	}

	public <T extends Losr> void add(Class<T> type) {

		// Selection.
		Items<Text> selection = view.selection();
		if (selection == null) {
			return;
		}

		// Terminals?
		Losr losr = selection.get(0) instanceof Token ? terminal(type,
				selection) : nonTerminal(type, selection);

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
		Losr losr;
		if (text instanceof Detail) {
			losr = ((Detail) text).header().losr();
		} else if (text instanceof Header) {
			losr = ((Header) text).losr();
		} else {
			throw new RoboticException("Header node not selected.");
		}

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
		Detail detail = null;
		if (text instanceof Detail) {
			detail = ((Detail) text);
		} else if (text instanceof Header) {
			Items<Detail> details = (((Header) text)).details();
			if (details.count() > 0) {
				detail = details.get(details.count() - 1);
			}
		}
		if (detail == null) {
			return;
		}

		// LOSR.
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

	private <T extends Losr> T terminal(Class<T> type, Items<Text> selection) {

		// Lexicon.
		TextContext context = context(selection);
		Class<? extends Terminal> terminalType = type == Losr.class ? null
				: (Class<? extends Terminal>) type;
		Terminal terminal = treebankService.lexicon().terminal(terminalType,
				key(view.partialTree().tokens(), context), context);
		if (terminal != null) {
			return (T) terminal;
		}

		// Terminals.
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

		throw new RoboticException("Can't create %s from terminals.",
				type.getSimpleName());
	}

	private <T extends Losr> T nonTerminal(Class<T> type, Items<Text> selection) {

		// Grammar.
		Items<Losr> items = items(selection);
		if (type == Losr.class) {
			Losr losr = treebankService.grammar().nonTerminal(items);
			if (losr != null) {
				return (T) losr;
			}

			// Diagnostics.
			StringBuilder key = new StringBuilder();
			int size = items.count();
			for (int i = 0; i < size; i++) {
				if (key.length() > 0) {
					key.append(' ');
				}
				key.append(items.get(i).name());
			}
			throw new RoboticException("No grammar rule found for '%s'.", key);
		}

		// Non-terminals.
		if (type == Event.class) {
			return (T) new Event(0, 0, items);
		}
		if (type == SpatialRelation.class) {
			return (T) new SpatialRelation(0, 0, items);
		}
		if (type == Entity.class) {
			return (T) new Entity(0, 0, items);
		}
		if (type == Destination.class) {
			return (T) new Destination(0, 0, items);
		}
		throw new RoboticException("Can't create %s from non-terminals.",
				type.getSimpleName());
	}

	private Items<Losr> items(Items<Text> selection) {

		// Use a partial tree to preserve sort order.
		PartialTree result = new PartialTree(view.partialTree().tokens());

		// Add items.
		int size = selection.count();
		for (int i = 0; i < size; i++) {
			Text item = selection.get(i);
			if (!(item instanceof Header)) {
				throw new RoboticException(
						"Selection should contain only headers.");
			}
			result.add(((Header) item).losr());
		}
		return result.items();
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