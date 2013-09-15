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

package com.trainrobots.nlp.lexicon;

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
		add("above", "(spatial-indicator: above)");
		add("adjacent", "(spatial-indicator: adjacent)");
		add("adjcent", "(spatial-indicator: adjacent)");
		add("after", "(spatial-indicator: after)");
		add("air", "(entity: (type: air))");
		add("all", "(Determiner all)");
		add("an", "(description: indefinite)");
		add("and", "(conjunction: and)");
		add("another", "(spatial-indicator: another)");
		add("are", "(copula: are)");
		add("arm", "(entity: (type: arm))");
		add("at", "(spatial-indicator: at)");
		add("away", "(spatial-indicator: away)");
		add("back", "(spatial-indicator: back)");
		add("backwards", "(spatial-indicator: back)");
		add("be", "(copula: be)");
		add("behind", "(spatial-indicator: behind)");
		add("below", "(spatial-indicator: below)");
		add("beside", "(spatial-indicator: beside)");
		add("between", "(spatial-indicator: between)");
		add("blok", "(entity: (type: cube))");
		add("block", "(entity: (type: cube))");
		add("blocks", "(entity: (type: cube) (number: plural))");
		add("blog", "(entity: (type: cube))");
		add("blue", "(color: blue)");
		add("board", "(entity: (type: board))");
		add("bock", "(entity: (type: cube))");
		add("border", "(entity: (type: edge))");
		add("bottom", "(spatial-indicator: back)");
		add("box", "(entity: (type: cube))");
		add("brick", "(entity: (type: cube))");
		add("bricks", "(entity: (type: cube) (number: plural))");
		add("but", "(conjunction: but)");
		add("by", "(spatial-indicator: at)");
		add("cell", "(entity: (type: tile))");
		add("cells", "(entity: (type: tile) (number: plural))");
		add("center", "(entity: (type: center))");
		add("centre", "(entity: (type: center))");
		add("central", "(spatial-indicator: central)");
		add("close", "(spatial-indicator: near)");
		add("closer", "(spatial-indicator: near)");
		add("closest", "(spatial-indicator: nearest)");
		add("column", "(entity: (type: column))");
		add("coloum", "(entity: (type: column))");
		add("columns", "(entity: (type: column) (number: plural))");
		add("cone", "(entity: (type: prism))");
		add("corner", "(entity: (type: corner))");
		add("corners", "(entity: (type: corner) (number: plural))");
		add("cube", "(entity: (type: cube))");
		add("cubes", "(entity: (type: cube) (number: plural))");
		add("current", "(spatial-indicator: current)");
		add("cyan", "(color: cyan)");
		add("dark", "(spatial-indicator: dark)");
		add("diagonal", "(spatial-indicator: diagonal)");
		add("diagonally", "(spatial-indicator: diagonal)");
		add("different", "(spatial-indicator: different)");
		add("direction", "(spatial-indicator: direction)");
		add("directly", "(spatial-indicator: direct)");
		add("do", "(event: (action: do))");
		add("down", "(spatial-indicator: down)");
		add("drop", "(event: (action: drop))");
		add("edge", "(entity: (type: edge))");
		add("edges", "(entity: (type: edges) (number: plural))");
		add("elevation", "(entity: (type: elevation))");
		add("empty", "(spatial-indicator: empty)");
		add("exactly", "(spatial-indicator: exactly)");
		add("far", "(spatial-indicator: far)");
		add("farthest", "(spatial-indicator: furthest)");
		add("first", "(ordinal: 1)");
		add("five", "(cardinal: 5)");
		add("fifth", "(ordinal: 5)");
		add("floor", "(entity: (type: board))");
		add("forward", "(spatial-indicator: forward)");
		add("four", "(cardinal: 4)");
		add("fourth", "(ordinal: 4)");
		add("from", "(spatial-indicator: from)");
		add("front", "(spatial-indicator: front)");
		add("furthest", "(spatial-indicator: furthest)");
		add("grab", "(event: (action: take))");
		add("gray", "(color: gray)");
		add("green", "(color: green)");
		add("grey", "(color: gray)");
		add("grid", "(entity: (type: board))");
		add("hand", "(entity: (type: gripper))");
		add("has", "(copula: has)");
		add("highest", "(spatial-indicator: highest)");
		add("hold", "(event: (action: hold))");
		add("in", "(spatial-indicator: in)");
		add("inside", "(spatial-indicator: inside)");
		add("into", "(spatial-indicator: into)");
		add("is", "(copula: is)");
		add("isolated", "(spatial-indicator: isolated)");
		add("it", "(entity: (type: reference))");
		add("it's", "(contraction: it is)");
		add("its", "(possessive: its)");
		add("last", "(spatial-indicator: last)");
		add("left", "(spatial-indicator: left)");
		add("leftmost", "(spatial-indicator: leftmost)");
		add("lift", "(event: (action: lift))");
		add("light", "(spatial-indicator: light)");
		add("line", "(entity: (type: line))");
		add("located", "(link: that-is)");
		add("lower", "(spatial-indicator: lower)");
		add("lowest", "(spatial-indicator: lowest)");
		add("magenta", "(color: magenta)");
		add("margin", "(entity: (type: margin))");
		add("middle", "(spatial-indicator: middle)");
		add("most", "(spatial-indicator: most)");
		add("move", "(event: (action: move))");
		add("near", "(spatial-indicator: near)");
		add("nearer", "(spatial-indicator: nearer)");
		add("nearest", "(spatial-indicator: nearest)");
		add("next", "(spatial-indicator: adjacent)");
		add("not", "(spatial-indicator: negative)");
		add("of", "(relation: of)");
		add("on", "(spatial-indicator: above)");
		add("one", "(cardinal: 1)");
		add("ones", "(entity: (type: reference) (number: plural))");
		add("only", "(spatial-indicator: only)");
		add("onto", "(spatial-indicator: above)");
		add("opposite", "(spatial-indicator: opposite)");
		add("other", "(spatial-indicator: other)");
		add("over", "(spatial-indicator: above)");
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
		add("remote", "(spatial-indicator: remote)");
		add("remove", "(event: (action: remove))");
		add("right", "(spatial-indicator: right)");
		add("rightmost", "(spatial-indicator: rightmost)");
		add("robot", "(entity: (type: robot))");
		add("row", "(entity: (type: row))");
		add("rows", "(entity: (type: row) (number: plural))");
		add("same", "(spatial-indicator: same)");
		add("second", "(ordinal: 2)");
		add("shift", "(event: (action: shift))");
		add("side", "(entity: (type: edge))");
		add("single", "(spatial-indicator: single)");
		add("sits", "(spatial-indicator: above)");
		add("sitting", "(link: that-is)");
		add("situated", "(link: that-is)");
		add("seven", "(cardinal: 7)");
		add("six", "(cardinal: 6)");
		add("sky", "(color: cyan)");
		add("slab", "(entity: (type: stack))");
		add("smallest", "(spatial-indicator: smallest)");
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
		add("surrounded", "(spatial-indicator: surrounded)");
		add("take", "(event: (action: take))");
		add("tall", "(spatial-indicator: tall)");
		add("taller", "(spatial-indicator: taller)");
		add("tallest", "(spatial-indicator: tallest)");
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
		add("to", "(spatial-indicator: to)");
		add("top", "(spatial-indicator: top)");
		add("toward", "(spatial-indicator: toward)");
		add("towards", "(spatial-indicator: toward)");
		add("tower", "(entity: (type: stack))");
		add("towers", "(entity: (type: stack) (number: plural))");
		add("triangle", "(entity: (type: prism))");
		add("triangles", "(entity: (type: prism) (number: plural))");
		add("turquoise", "(color: cyan)");
		add("two", "(cardinal: 2)");
		add("under", "(spatial-indicator: under)");
		add("underneath", "(spatial-indicator: below)");
		add("up", "(spatial-indicator: up)");
		add("upper", "(spatial-indicator: upper)");
		add("where", "(wh-determiner: where)");
		add("which", "(wh-determiner: which)");
		add("white", "(color: white)");
		add("with", "(spatial-indicator: with)");
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