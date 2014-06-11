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
import com.trainrobots.losr.Marker;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.Source;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.nlp.grammar.Grammar;
import com.trainrobots.planner.Planner;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.services.window.WindowService;
import com.trainrobots.ui.visualization.losr.Ellipsis;
import com.trainrobots.ui.visualization.losr.EllipticalContext;
import com.trainrobots.ui.visualization.losr.PartialTree;
import com.trainrobots.ui.visualization.visuals.Detail;
import com.trainrobots.ui.visualization.visuals.Header;
import com.trainrobots.ui.visualization.visuals.IdDetail;
import com.trainrobots.ui.visualization.visuals.ReferenceIdDetail;
import com.trainrobots.ui.visualization.visuals.Text;
import com.trainrobots.ui.visualization.visuals.Token;

public class Editor {

	private static final LosrType[] ellipticalTypes = {
			new LosrType(new Type(Types.Region)),
			new LosrType(new Relation(Relations.Above)) };

	private final WindowService windowService;
	private final TreebankService treebankService;
	private final LosrView view;
	private final Popup popup;

	public Editor(WindowService windowService, TreebankService treebankService,
			LosrView view, Popup popup) {
		this.windowService = windowService;
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
		Losr losr;
		if (selection.get(0) instanceof Token) {
			losr = terminal(type, selection);
		} else {
			losr = nonTerminal(type, selection);
			removeEllipticalContext(losr);
		}

		// Add.
		PartialTree partialTree = view.partialTree();
		partialTree.add(losr);

		// Post-processing.
		Items<Losr> items = partialTree.items();
		Grammar grammar = treebankService.grammar();
		if (items.count() >= 2 && (losr = grammar.nonTerminal(items)) != null) {
			partialTree.add(losr);
		}

		// Redraw.
		view.redrawTree();
	}

	public void addEllipsis() {
		Token token = token();
		if (token != null) {
			PartialTree partialTree = view.partialTree();
			partialTree.add(new Ellipsis(token.id()));
			view.redrawTree();
		}
	}

	public void addId() {
		Header header = header();
		if (header != null) {
			Losr losr = header.losr();
			if (losr.id() == 0) {
				losr.id(1);
				view.redrawTree();
			}
		}
	}

	public void addReferenceId() {
		Header header = header();
		if (header != null) {
			Losr losr = header.losr();
			if (losr.referenceId() == 0) {
				losr.referenceId(1);
				view.redrawTree();
			}
		}
	}

	public void delete() {

		// Selection.
		Text text = selection();
		if (text == null) {
			return;
		}

		// Header.
		Losr losr = null;
		if (text instanceof IdDetail) {
			((Detail) text).header().losr().id(0);
		} else if (text instanceof ReferenceIdDetail) {
			((Detail) text).header().losr().referenceId(0);
		} else if (text instanceof Detail) {
			losr = ((Detail) text).header().losr();
		} else if (text instanceof Header) {
			losr = ((Header) text).losr();
		} else {
			throw new RoboticException("Header node not selected.");
		}

		// Remove.
		if (losr != null) {
			view.partialTree().remove(losr);
		}
		view.redrawTree();
	}

	public void change() {

		// Selection.
		Text text = selection();
		if (text == null) {
			return;
		}

		// Detail.
		Detail detail = null;
		if (text instanceof Detail) {
			detail = ((Detail) text);
		}

		// Header
		else if (text instanceof Header) {
			Header header = (Header) text;

			// Ellipsis?
			Losr losr = header.losr();
			if (losr instanceof Ellipsis) {
				popup.show(header, losr, (Object[]) ellipticalTypes,
						ellipticalTypes[0]);
				return;
			}

			// Detail.
			Items<Detail> details = header.details();
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
			popup.show(detail, losr, (Object[]) Actions.values(),
					((Action) losr).action());
			return;
		}

		if (losr instanceof Color) {
			popup.show(detail, losr, (Object[]) Colors.values(),
					((Color) losr).color());
			return;
		}

		if (losr instanceof Type) {
			popup.show(detail, losr, (Object[]) Types.values(),
					((Type) losr).type());
			return;
		}

		if (losr instanceof Relation) {
			popup.show(detail, losr, (Object[]) Relations.values(),
					((Relation) losr).relation());
			return;
		}

		if (losr instanceof Indicator) {
			popup.show(detail, losr, (Object[]) Indicators.values(),
					((Indicator) losr).indicator());
			return;
		}
	}

	public void acceptChange(Losr losr, Object value) {

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

		if (losr instanceof Ellipsis) {
			Ellipsis ellipsis = (Ellipsis) losr;
			PartialTree partialTree = view.partialTree();
			partialTree.remove(ellipsis);
			LosrType type = (LosrType) value;
			partialTree.add(type.terminal().withContext(
					new EllipticalContext(ellipsis.after())));
			view.redrawTree();
			return;
		}
	}

	public void ground() {

		// Header.
		Header header = header();
		if (header == null) {
			windowService.defaultStatus();
			return;
		}

		// Ground.
		Integer groundings = null;
		try {
			Losr losr = header.losr();
			Command command = view.partialTree().command();
			Planner planner = new Planner(command.scene().before());
			Losr root = command.losr();
			if (losr instanceof Entity) {
				groundings = planner.ground(root, (Entity) losr).best().count();
			} else if (losr instanceof SpatialRelation) {
				groundings = planner.ground(root, (SpatialRelation) losr)
						.best().count();
			} else if (losr instanceof Destination) {
				groundings = planner.ground(root, (Destination) losr).best()
						.count();
			}
		} catch (Exception exception) {
			windowService.error(exception.getMessage());
			return;
		}

		// Status.
		if (groundings != null) {
			windowService.status("Groundings: %d", groundings);
		} else {
			windowService.defaultStatus();
		}
	}

	public void tag(Command command) {

		// Clear.
		PartialTree partialTree = view.partialTree();
		partialTree.clear();

		// Tag.
		Items<Losr> items = treebankService.tagger().losr(command);
		for (Losr item : items) {
			partialTree.add(item);
		}

		// Redraw.
		view.redrawTree();
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
		if (type == Marker.class) {
			return (T) new Marker(context);
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
		if (type == Source.class) {
			return (T) new Source(0, 0, items);
		}
		throw new RoboticException("Can't create %s from non-terminals.",
				type.getSimpleName());
	}

	private void removeEllipticalContext(Items<Losr> items) {
		for (Items item : items) {
			if (item instanceof Terminal) {
				Terminal terminal = (Terminal) item;
				if (terminal.context() instanceof EllipticalContext) {
					terminal.context(null);
				}
			}
		}
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

	private Header header() {
		Text text = selection();
		if (text == null) {
			return null;
		}
		if (text instanceof Header) {
			return (Header) text;
		}
		if (text instanceof Detail) {
			return ((Detail) text).header();
		}
		return null;
	}

	private Token token() {
		Text text = selection();
		return text instanceof Token ? (Token) text : null;
	}

	private Text selection() {
		Items<Text> selection = view.selection();
		if (selection == null) {
			return null;
		}
		return selection.count() == 1 ? selection.get(0) : null;
	}
}