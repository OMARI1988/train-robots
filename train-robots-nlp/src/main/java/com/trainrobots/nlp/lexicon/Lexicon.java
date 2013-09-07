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

import com.trainrobots.nlp.NlpException;
import com.trainrobots.nlp.trees.Node;

public class Lexicon {

	private static final Map<String, Node> entries = new HashMap<String, Node>();

	private Lexicon() {
	}

	public static Node get(String text) {
		Node node = entries.get(text);
		return node != null ? node.clone() : null;
	}

	static {
		add("a", "(Description indefinite)");
		add("above", "(SpatialIndicator above)");
		add("adjacent", "(SpatialIndicator adjacent)");
		add("adjcent", "(SpatialIndicator adjacent)");
		add("after", "(SpatialIndicator after)");
		add("air", "(Object (Type air))");
		add("all", "(Determiner all)");
		add("an", "(Description indefinite)");
		add("and", "(Conjunction and)");
		add("another", "(Attribute another)");
		add("are", "(Copula are)");
		add("arm", "(Object (Type arm))");
		add("at", "(SpatialIndicator at)");
		add("away", "(Attribute away)");
		add("back", "(Attribute back)");
		add("backwards", "(Attribute back)");
		add("be", "(Copula be)");
		add("behind", "(SpatialIndicator behind)");
		add("below", "(SpatialIndicator below)");
		add("beside", "(SpatialIndicator beside)");
		add("between", "(SpatialIndicator between)");
		add("blok", "(Object (Type cube))");
		add("block", "(Object (Type cube))");
		add("blocks", "(Object (Type cube) (Number plural))");
		add("blog", "(Object (Type cube))");
		add("blue", "(Attribute blue)");
		add("board", "(Object (Type board))");
		add("border", "(Object (Type edge))");
		add("bottom", "(Attribute back)");
		add("box", "(Object (Type cube))");
		add("brick", "(Object (Type cube))");
		add("bricks", "(Object (Type cube) (Number plural))");
		add("but", "(Conjunction but)");
		add("by", "(SpatialIndicator at)");
		add("cell", "(Object (Type tile))");
		add("cells", "(Object (Type tile) (Numer plural))");
		add("center", "(Object (Type center))");
		add("centre", "(Object (Type center))");
		add("close", "(SpatialIndicator near)");
		add("closer", "(SpatialIndicator near)");
		add("closest", "(SpatialIndicator nearest)");
		add("column", "(Object (Type column))");
		add("coloum", "(Object (Type column))");
		add("columns", "(Object (Type column) (Number plural))");
		add("cone", "(Object (Type prism))");
		add("corner", "(Object (Type corner))");
		add("corners", "(Object (Type corner) (Number plural))");
		add("cube", "(Object (Type cube))");
		add("cubes", "(Object (Type cube) (Number plural))");
		add("current", "(Attribute current)");
		add("cyan", "(Attribute cyan)");
		add("dark", "(Attribute dark)");
		add("diagonal", "(SpatialIndicator diagonal)");
		add("diagonally", "(SpatialIndicator diagonal)");
		add("different", "(Attribute different)");
		add("direction", "(SpatialIndicator direction)");
		add("directly", "(Attribute direct)");
		add("do", "(Command (Action do))");
		add("down", "(Attribute down)");
		add("drop", "(Command (Action drop))");
		add("east", "(Attribute east)");
		add("edge", "(Object (Type edge))");
		add("edges", "(Object (Type edges) (Number plural))");
		add("elevation", "(Object (Type elevation))");
		add("empty", "(Attribute empty)");
		add("exactly", "(Attribute exactly)");
		add("far", "(SpatialIndicator far)");
		add("farthest", "(SpatialIndicator furthest)");
		add("first", "(Ordinal 1)");
		add("five", "(Cardinal 5)");
		add("fifth", "(Ordinal 5)");
		add("floor", "(Object (Type board))");
		add("forward", "(Attribute forward)");
		add("four", "(Cardinal 4)");
		add("fourth", "(Ordinal 4)");
		add("from", "(SpatialIndicator from)");
		add("front", "(Attribute front)");
		add("furthest", "(SpatialIndicator furthest)");
		add("grab", "(Command (Action grab))");
		add("gray", "(Attribute gray)");
		add("green", "(Attribute green)");
		add("grey", "(Attribute gray)");
		add("grid", "(Object (Type board))");
		add("hand", "(Object (Type gripper))");
		add("has", "(Copula has)");
		add("highest", "(Attribute highest)");
		add("hold", "(Command (Action hold))");
		add("in", "(SpatialIndicator in)");
		add("inside", "(SpatialIndicator inside)");
		add("into", "(SpatialIndicator into)");
		add("is", "(Copula is)");
		add("it", "(Anaphor it)");
		add("it's", "(Contraction it is)");
		add("its", "(Possessive its)");
		add("last", "(SpatialIndicator last)");
		add("left", "(Attribute left)");
		add("leftmost", "(SpatialIndicator leftmost)");
		add("lift", "(Command (Action lift))");
		add("light", "(Attribute light)");
		add("line", "(Object (Type line))");
		add("located", "(Link that-is)");
		add("lower", "(Attribute lower)");
		add("lowest", "(Attribute lowest)");
		add("magenta", "(Attribute magenta)");
		add("margin", "(Object (Type margin))");
		add("middle", "(SpatialIndicator middle)");
		add("most", "(Attribute most)");
		add("move", "(Command (Action move))");
		add("near", "(SpatialIndicator near)");
		add("nearer", "(SpatialIndicator nearer)");
		add("nearest", "(SpatialIndicator nearest)");
		add("next", "(SpatialIndicator adjacent)");
		add("not", "(Attribute negative)");
		add("of", "(Relation of)");
		add("on", "(SpatialIndicator on)");
		add("one", "(Cardinal 1)");
		add("ones", "(Object (Type Anaphor) (Number Plural))");
		add("only", "(Attribute only)");
		add("onto", "(SpatialIndicator onto)");
		add("opposite", "(SpatialIndicator opposite)");
		add("other", "(Attribute other)");
		add("over", "(SpatialIndicator above)");
		add("parallelipiped", "(Object (Type stack))");
		add("pick", "(Command (Action pick))");
		add("pile", "(Object (Type stack))");
		add("pillar", "(Object (Type stack))");
		add("pink", "(Attribute magenta)");
		add("place", "(Command (Action place))");
		add("placed", "(Link that-is)");
		add("places", "(Object (Type tile) (Number plural))");
		add("position", "(Object (Type position))");
		add("prism", "(Object (Type prism))");
		add("prim", "(Object (Type prism))");
		add("pyrramid", "(Object (Type prism))");
		add("purple", "(Attribute magenta)");
		add("put", "(Command (Action put))");
		add("pyramid", "(Object (Type prism))");
		add("pyraamid", "(Object (Type prism))");
		add("pyramids", "(Object (Type prism) (Number plural))");
		add("red", "(Attribute red)");
		add("rd", "(Attribute red)");
		add("remove", "(Command (Action remove))");
		add("right", "(Attribute right)");
		add("rightmost", "(SpatialIndicator rightmost)");
		add("robot", "(Object (Type robot))");
		add("row", "(Object (Type row))");
		add("rows", "(Object (Type row) (Number plural))");
		add("same", "(Attribute same)");
		add("second", "(Ordinal 2)");
		add("shift", "(Command (Action shift))");
		add("side", "(Object (Type edge))");
		add("single", "(Attribute single)");
		add("sits", "(SpatialIndicator above)");
		add("sitting", "(Link that-is)");
		add("situated", "(Link that-is)");
		add("seven", "(Cardinal 7)");
		add("six", "(Cardinal 6)");
		add("sky", "(Attribute cyan)");
		add("slab", "(Object (Type stack))");
		add("smallest", "(Attribute smallest)");
		add("south", "(Attribute south)");
		add("space", "(Object (Type space))");
		add("spaces", "(Object (Type tile) (Numer plural))");
		add("square", "(Object (Type tile))");
		add("squares", "(Object (Type tile) (Numer plural))");
		add("squrae", "(Object (Type tile))");
		add("sqaure", "(Object (Type tile))");
		add("stack", "(Object (Type stack))");
		add("stacks", "(Object (Type stack) (Number plural))");
		add("standing", "(Link that-is)");
		add("step", "(Object (Type tile))");
		add("steps", "(Object (Type tile) (Numer plural))");
		add("siyan", "(Attribute cyan)");
		add("surrounded", "(SpatialIndicator surrounded)");
		add("take", "(Command (Action take))");
		add("tall", "(Attribute tall)");
		add("taller", "(Attribute taller)");
		add("tallest", "(Attribute tallest)");
		add("tetrahedron", "(Object (Type prism))");
		add("terahedron", "(Object (Type prism))");
		add("that", "(Anaphor that)");
		add("that's", "(Contraction that is)");
		add("the", "(Description definite)");
		add("them", "(Anaphor them)");
		add("then", "(Conjunction then)");
		add("third", "(Ordinal 3)");
		add("this", "(Anaphor this)");
		add("three", "(Cardinal 3)");
		add("to", "(SpatialIndicator to)");
		add("top", "(Attribute top)");
		add("toward", "(SpatialIndicator toward)");
		add("towards", "(SpatialIndicator toward)");
		add("tower", "(Object (Type stack))");
		add("towers", "(Object (Type stack) (Number plural))");
		add("triangle", "(Object (Type prism))");
		add("triangles", "(Object (Type prism) (Number plural))");
		add("turquoise", "(Attribute cyan)");
		add("two", "(Cardinal 2)");
		add("under", "(SpatialIndicator under)");
		add("underneath", "(SpatialIndicator below)");
		add("up", "(Attribute up)");
		add("upper", "(Attribute upper)");
		add("where", "(WhDeterminer where)");
		add("which", "(WhDeterminer which)");
		add("white", "(Attribute white)");
		add("with", "(SpatialIndicator with)");
		add("yellow", "(Attribute yellow)");
		add("you", "(Object (Type robot))");
		add("your", "(Determiner your)");
	}

	private static void add(String text, String entry) {
		if (entries.containsKey(text)) {
			throw new NlpException("Duplicate lexicon entry: " + text);
		}
		Node node = Node.fromString(entry);
		if (node.hasTag("Object") && node.getChild("Type") == null) {
			throw new NlpException("INVALID: " + text);
		}
		entries.put(text, node);
	}
}