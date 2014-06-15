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
import com.trainrobots.distributions.observable.ObservableDistribution;
import com.trainrobots.distributions.spatial.DestinationDistribution;
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
import com.trainrobots.losr.MeasureRelation;
import com.trainrobots.losr.Relation;
import com.trainrobots.losr.Relations;
import com.trainrobots.losr.Source;
import com.trainrobots.losr.SpatialRelation;
import com.trainrobots.losr.Terminal;
import com.trainrobots.losr.TextContext;
import com.trainrobots.losr.Type;
import com.trainrobots.losr.Types;
import com.trainrobots.nlp.grammar.Grammar;
import com.trainrobots.nlp.lexicon.Lexicon;
import com.trainrobots.nlp.losr.Ellipsis;
import com.trainrobots.nlp.losr.EllipticalContext;
import com.trainrobots.nlp.losr.PartialTree;
import com.trainrobots.nlp.parser.Parser;
import com.trainrobots.planner.Planner;
import com.trainrobots.scenes.Layout;
import com.trainrobots.treebank.Command;
import com.trainrobots.ui.services.treebank.TreebankService;
import com.trainrobots.ui.services.window.WindowService;
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

	private static final Integer[] numbers = { 1, 2, 3, 4, 5, 6, 7 };

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
		update();
	}

	public void addEllipsis() {
		Token token = token();
		if (token != null) {
			PartialTree partialTree = view.partialTree();
			partialTree.add(new Type(new EllipticalContext(token.id()),
					Types.Region));
			update();
		}
	}

	public void addId() {
		Header header = header();
		if (header != null) {
			Losr losr = header.losr();
			if (losr.id() == 0) {
				losr.id(1);
				update();
			}
		}
	}

	public void addReferenceId() {
		Header header = header();
		if (header != null) {
			Losr losr = header.losr();
			if (losr.referenceId() == 0) {
				losr.referenceId(1);
				update();
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

		// ID
		Losr losr = detail.header().losr();
		if (detail instanceof IdDetail) {
			popup.show(detail, losr, (Object[]) numbers, losr.id());
			return;
		}
		if (detail instanceof ReferenceIdDetail) {
			popup.show(detail, losr, (Object[]) numbers, losr.referenceId());
			return;
		}

		// LOSR.
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
		if (losr instanceof Cardinal) {
			popup.show(detail, losr, (Object[]) numbers,
					((Cardinal) losr).value());
			return;
		}
	}

	public void acceptChange(Text text, Losr losr, Object value) {

		if (text instanceof IdDetail) {
			IdDetail detail = (IdDetail) text;
			detail.header().losr().id((Integer) value);
			view.redrawTree();
			return;
		}

		if (text instanceof ReferenceIdDetail) {
			ReferenceIdDetail detail = (ReferenceIdDetail) text;
			detail.header().losr().referenceId((Integer) value);
			view.redrawTree();
			return;
		}

		if (losr instanceof Action) {
			Action action = (Action) losr;
			action.action((Actions) value);
			update();
			return;
		}

		if (losr instanceof Color) {
			Color color = (Color) losr;
			color.color((Colors) value);
			update();
			return;
		}

		if (losr instanceof Type) {
			Type type = (Type) losr;
			type.type((Types) value);
			update();
			return;
		}

		if (losr instanceof Relation) {
			Relation relation = (Relation) losr;
			relation.relation((Relations) value);
			update();
			return;
		}

		if (losr instanceof Indicator) {
			Indicator indicator = (Indicator) losr;
			indicator.indicator((Indicators) value);
			update();
			return;
		}

		if (losr instanceof Cardinal) {
			Cardinal cardinal = (Cardinal) losr;
			cardinal.value((Integer) value);
			update();
			return;
		}

		if (losr instanceof Ellipsis) {
			Ellipsis ellipsis = (Ellipsis) losr;
			PartialTree partialTree = view.partialTree();
			partialTree.remove(ellipsis);
			LosrType type = (LosrType) value;
			partialTree.add(type.terminal().withContext(
					new EllipticalContext(ellipsis.after())));
			update();
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
		Integer total = null;
		try {
			Losr losr = header.losr();
			Command command = view.partialTree().command();
			Planner planner = new Planner(command.scene().before());
			Losr root = command.losr();
			if (losr instanceof Entity) {
				ObservableDistribution distribution = planner.ground(root,
						(Entity) losr);
				groundings = distribution.best().count();
				total = distribution.count();
			} else if (losr instanceof SpatialRelation) {
				DestinationDistribution distribution = planner.ground(root,
						(SpatialRelation) losr);
				groundings = distribution.best().count();
				total = distribution.count();
			} else if (losr instanceof Destination) {
				DestinationDistribution distribution = planner.ground(root,
						(Destination) losr);
				groundings = distribution.best().count();
				total = distribution.count();
			}
		} catch (Exception exception) {
			windowService.error(exception.getMessage());
			return;
		}

		// No groundings?
		if (groundings == null) {
			windowService.defaultStatus();
			return;
		}

		// Status.
		StringBuilder text = new StringBuilder();
		text.append("Groundings: ");
		text.append(groundings);
		if (total != groundings) {
			text.append(" of ");
			text.append(total);
		}
		windowService.status(text.toString());
	}

	public void tag() {

		// Clear.
		PartialTree partialTree = view.partialTree();
		partialTree.clear();

		// Tag.
		Command command = partialTree.command();
		for (Terminal terminal : treebankService.tagger().terminals(command)) {
			partialTree.add(terminal);
		}

		// Update.
		update();
	}

	public void parse() {

		// Clear.
		view.partialTree().clear();

		// Parse.
		parsePartialTree();

		// Update.
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
		if (type == MeasureRelation.class) {
			return (T) new MeasureRelation(0, 0, items);
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

	public void update() {
		parsePartialTree();
		view.redrawTree();
	}

	private void parsePartialTree() {

		// Items.
		PartialTree partialTree = view.partialTree();
		Items<Losr> items = partialTree.items();

		// Context.
		Command command = partialTree.command();
		Items<Terminal> tokens = command.tokens();
		Layout layout = command.scene().before();
		Lexicon lexicon = treebankService.lexicon();
		Grammar grammar = treebankService.grammar();

		// Partial tree?
		Parser parser;
		if (items.count() > 0) {
			parser = new Parser(layout, grammar, items, tokens, false);
		} else {

			// Tag.
			Items<Terminal> terminals;
			try {
				terminals = treebankService.tagger().sequence(tokens)
						.terminals(lexicon);
			} catch (Exception exception) {
				return;
			}

			// Parser.
			parser = new Parser(layout, grammar, lexicon, (Items) terminals,
					tokens, false);
		}

		// Parse.
		Losr losr = null;
		try {
			losr = parser.parse();
		} catch (Exception exception) {
		}

		// Result.
		if (losr != null) {
			partialTree.clear();
			partialTree.add(losr);
		}
	}
}