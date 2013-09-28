/* Copyright (C) Kais Dukes.
 * Email: kais@kaisdukes.com
 *
 * This file is part of Train Robots.
 *
 * This is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Train Robots. If not, see <http://www.gnu.org/licenses/>.
 */

package com.trainrobots.nlp.parser.partial;

import java.util.HashMap;
import java.util.Map;

import com.trainrobots.core.CoreException;
import com.trainrobots.core.nodes.Node;

public class Lexicon {

	private static final Map<String, Node> entries = new HashMap<String, Node>();

	private Lexicon() {
	}

	public static Node get(String text) {
		Node node = entries.get(text);
		return node != null ? node.clone() : null;
	}

	static {
		add("a", "(description: indefinite)");
		add("above", "(indicator: above)");
		add("adjacent", "(indicator: adjacent)");
		add("adjcent", "(indicator: adjacent)");
		add("after", "(indicator: after)");
		add("air", "(entity: (type: air))");
		add("all", "(Determiner all)");
		add("an", "(description: indefinite)");
		add("and", "(conjunction: and)");
		add("another", "(indicator: another)");
		add("are", "(copula: are)");
		add("arm", "(entity: (type: arm))");
		add("at", "(indicator: at)");
		add("away", "(indicator: away)");
		add("back", "(indicator: back)");
		add("backwards", "(indicator: back)");
		add("be", "(copula: be)");
		add("behind", "(indicator: behind)");
		add("below", "(indicator: below)");
		add("beside", "(indicator: beside)");
		add("between", "(indicator: between)");
		add("blok", "(entity: (type: cube))");
		add("block", "(entity: (type: cube))");
		add("blocks", "(entity: (type: cube) (number: plural))");
		add("blog", "(entity: (type: cube))");
		add("blue", "(color: blue)");
		add("board", "(entity: (type: board))");
		add("bock", "(entity: (type: cube))");
		add("border", "(entity: (type: edge))");
		add("bottom", "(indicator: back)");
		add("box", "(entity: (type: cube))");
		add("brick", "(entity: (type: cube))");
		add("bricks", "(entity: (type: cube) (number: plural))");
		add("but", "(conjunction: but)");
		add("by", "(indicator: at)");
		add("cell", "(entity: (type: tile))");
		add("cells", "(entity: (type: tile) (number: plural))");
		add("center", "(entity: (type: center))");
		add("centre", "(entity: (type: center))");
		add("central", "(indicator: central)");
		add("close", "(indicator: near)");
		add("closer", "(indicator: near)");
		add("closest", "(indicator: nearest)");
		add("column", "(entity: (type: column))");
		add("coloum", "(entity: (type: column))");
		add("columns", "(entity: (type: column) (number: plural))");
		add("cone", "(entity: (type: prism))");
		add("corner", "(entity: (type: corner))");
		add("corners", "(entity: (type: corner) (number: plural))");
		add("cube", "(entity: (type: cube))");
		add("cubes", "(entity: (type: cube) (number: plural))");
		add("current", "(indicator: current)");
		add("cyan", "(color: cyan)");
		add("dark", "(indicator: dark)");
		add("diagonal", "(indicator: diagonal)");
		add("diagonally", "(indicator: diagonal)");
		add("different", "(indicator: different)");
		add("direction", "(indicator: direction)");
		add("directly", "(indicator: direct)");
		add("do", "(event: (action: do))");
		add("down", "(indicator: down)");
		add("drop", "(event: (action: drop))");
		add("edge", "(entity: (type: edge))");
		add("edges", "(entity: (type: edges) (number: plural))");
		add("elevation", "(entity: (type: elevation))");
		add("empty", "(indicator: empty)");
		add("exactly", "(indicator: exactly)");
		add("far", "(indicator: far)");
		add("farthest", "(indicator: furthest)");
		add("first", "(ordinal: 1)");
		add("five", "(cardinal: 5)");
		add("fifth", "(ordinal: 5)");
		add("floor", "(entity: (type: board))");
		add("forward", "(indicator: forward)");
		add("four", "(cardinal: 4)");
		add("fourth", "(ordinal: 4)");
		add("from", "(indicator: from)");
		add("front", "(indicator: front)");
		add("furthest", "(indicator: furthest)");
		add("grab", "(event: (action: take))");
		add("gray", "(color: gray)");
		add("green", "(color: green)");
		add("grey", "(color: gray)");
		add("grid", "(entity: (type: board))");
		add("hand", "(entity: (type: gripper))");
		add("has", "(copula: has)");
		add("highest", "(indicator: highest)");
		add("hold", "(event: (action: hold))");
		add("in", "(indicator: in)");
		add("inside", "(indicator: inside)");
		add("into", "(indicator: into)");
		add("is", "(copula: is)");
		add("isolated", "(indicator: isolated)");
		add("it", "(entity: (type: reference))");
		add("it's", "(contraction: it is)");
		add("its", "(possessive: its)");
		add("last", "(indicator: last)");
		add("left", "(indicator: left)");
		add("leftmost", "(indicator: leftmost)");
		add("lift", "(event: (action: lift))");
		add("light", "(indicator: light)");
		add("line", "(entity: (type: line))");
		add("located", "(link: that-is)");
		add("lower", "(indicator: lower)");
		add("lowest", "(indicator: lowest)");
		add("magenta", "(color: magenta)");
		add("margin", "(entity: (type: margin))");
		add("middle", "(indicator: middle)");
		add("most", "(indicator: most)");
		add("move", "(event: (action: move))");
		add("near", "(indicator: near)");
		add("nearer", "(indicator: nearer)");
		add("nearest", "(indicator: nearest)");
		add("next", "(indicator: adjacent)");
		add("not", "(indicator: negative)");
		add("of", "(relation: of)");
		add("on", "(indicator: above)");
		add("one", "(cardinal: 1)");
		add("ones", "(entity: (type: reference) (number: plural))");
		add("only", "(indicator: unique)");
		add("onto", "(indicator: above)");
		add("opposite", "(indicator: opposite)");
		add("other", "(indicator: other)");
		add("over", "(indicator: above)");
		add("parallelipiped", "(entity: (type: stack))");
		add("pick", "(event: (action: take))");
		add("pile", "(entity: (type: stack))");
		add("pillar", "(entity: (type: stack))");
		add("pink", "(color: magenta)");
		add("place", "(event: (action: move))");
		add("placed", "(link: that-is)");
		add("places", "(entity: (type: tile) (number: plural))");
		add("position", "(entity: (type: position))");
		add("prism", "(entity: (type: prism))");
		add("prisms", "(entity: (type: prism) (number: plural))");
		add("prim", "(entity: (type: prism))");
		add("pyrramid", "(entity: (type: prism))");
		add("purple", "(color: magenta)");
		add("put", "(event: (action: move))");
		add("pyramid", "(entity: (type: prism))");
		add("pyraamid", "(entity: (type: prism))");
		add("pyramids", "(entity: (type: prism) (number: plural))");
		add("red", "(color: red)");
		add("rd", "(color: red)");
		add("remote", "(indicator: remote)");
		add("remove", "(event: (action: remove))");
		add("right", "(indicator: right)");
		add("rightmost", "(indicator: rightmost)");
		add("robot", "(entity: (type: robot))");
		add("row", "(entity: (type: row))");
		add("rows", "(entity: (type: row) (number: plural))");
		add("same", "(indicator: same)");
		add("second", "(ordinal: 2)");
		add("shift", "(event: (action: shift))");
		add("side", "(entity: (type: edge))");
		add("single", "(indicator: individual)");
		add("sits", "(indicator: above)");
		add("sitting", "(link: that-is)");
		add("situated", "(link: that-is)");
		add("seven", "(cardinal: 7)");
		add("six", "(cardinal: 6)");
		add("sky", "(color: cyan)");
		add("slab", "(entity: (type: stack))");
		add("smallest", "(indicator: smallest)");
		add("space", "(entity: (type: space))");
		add("spaces", "(entity: (type: tile) (number: plural))");
		add("square", "(entity: (type: tile))");
		add("squares", "(entity: (type: tile) (number: plural))");
		add("squrae", "(entity: (type: tile))");
		add("sqaure", "(entity: (type: tile))");
		add("stack", "(entity: (type: stack))");
		add("stacks", "(entity: (type: stack) (number: plural))");
		add("standing", "(link: that-is)");
		add("step", "(entity: (type: tile))");
		add("steps", "(entity: (type: tile) (number: plural))");
		add("siyan", "(color: cyan)");
		add("surrounded", "(indicator: surrounded)");
		add("take", "(event: (action: take))");
		add("tall", "(indicator: tall)");
		add("taller", "(indicator: taller)");
		add("tallest", "(indicator: tallest)");
		add("tetrahedron", "(entity: (type: prism))");
		add("terahedron", "(entity: (type: prism))");
		add("that", "(type: reference)");
		add("that's", "(contraction: that is)");
		add("the", "(description: definite)");
		add("them", "(type: reference)");
		add("then", "(conjunction: then)");
		add("third", "(ordinal: 3)");
		add("this", "(type: reference)");
		add("three", "(cardinal: 3)");
		add("tile", "(entity: (type: tile))");
		add("tiles", "(entity: (type: tile) (number: plural))");
		add("to", "(indicator: to)");
		add("top", "(indicator: top)");
		add("toward", "(indicator: toward)");
		add("towards", "(indicator: toward)");
		add("tower", "(entity: (type: stack))");
		add("towers", "(entity: (type: stack) (number: plural))");
		add("triangle", "(entity: (type: prism))");
		add("triangles", "(entity: (type: prism) (number: plural))");
		add("turquoise", "(color: cyan)");
		add("two", "(cardinal: 2)");
		add("under", "(indicator: under)");
		add("underneath", "(indicator: below)");
		add("up", "(indicator: up)");
		add("upper", "(indicator: upper)");
		add("where", "(wh-determiner: where)");
		add("which", "(wh-determiner: which)");
		add("white", "(color: white)");
		add("with", "(indicator: with)");
		add("yellow", "(color: yellow)");
		add("you", "(entity: (type: robot))");
		add("your", "(determiner: your)");
	}

	private static void add(String text, String entry) {
		if (entries.containsKey(text)) {
			throw new CoreException("Duplicate lexicon entry: " + text);
		}
		Node node = Node.fromString(entry);
		if (node.hasTag("entity:") && node.getChild("type:") == null) {
			throw new CoreException("Entity type not specified: " + text);
		}
		entries.put(text, node);
	}
}