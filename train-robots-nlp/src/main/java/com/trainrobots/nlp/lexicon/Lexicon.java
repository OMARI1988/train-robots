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
		add("above", "(Direction above)");
		add("after", "(SpatialIndicator after)");
		add("air", "(Object (Type air))");
		add("all", "(Relation all)");
		add("an", "(Description indefinite)");
		add("and", "(Conjunction and)");
		add("another", "(Relation another)");
		add("are", "(Copula are)");
		add("arm", "(Object (Type arm))");
		add("at", "(SpatialIndicator at)");
		add("away", "(Relation away)");
		add("back", "(Direction back)");
		add("backwards", "(Direction back)");
		add("be", "(Copula be)");
		add("behind", "(SpatialIndicator behind)");
		add("below", "(SpatialIndicator below)");
		add("beside", "(SpatialIndicator beside)");
		add("between", "(SpatialIndicator between)");
		add("block", "(Object (Type cube))");
		add("blocks", "(Object (Type cube) (Number plural))");
		add("blue", "(Color blue)");
		add("board", "(Object (Type board))");
		add("border", "(Object (Type edge))");
		add("bottom", "(Direction back)");
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
		add("columns", "(Object (Type column) (Number plural))");
		add("cone", "(Object (Type prism))");
		add("corner", "(Object (Type corner))");
		add("cube", "(Object (Type cube))");
		add("cubes", "(Object (Type cube) (Number plural))");
		add("current", "(Relation current)");
		add("cyan", "(Color cyan)");
		add("dark", "(Color dark)");
		add("diagonal", "(SpatialIndicator diagonal)");
		add("diagonally", "(SpatialIndicator diagonal)");
		add("different", "(Relation different)");
		add("direction", "(SpatialIndicator direction)");
		add("directly", "(Relation direct)");
		add("do", "(Command (Action do))");
		add("down", "(Direction down)");
		add("drop", "(Command (Action drop))");
		add("east", "(Direction east)");
		add("edge", "(Object (Type edge))");
		add("elevation", "(Object (Type elevation))");
		add("empty", "(Relation empty)");
		add("exactly", "(Relation exactly)");
		add("far", "(SpatialIndicator far)");
		add("farthest", "(SpatialIndicator furthest)");
		add("first", "(Ordinal 1)");
		add("five", "(Cardinal 5)");
		add("floor", "(Object (Type board))");
		add("forward", "(Direction forward)");
		add("four", "(Cardinal 4)");
		add("fourth", "(Ordinal 4)");
		add("from", "(SpatialIndicator from)");
		add("front", "(Direction front)");
		add("furthest", "(SpatialIndicator furthest)");
		add("grab", "(Command (Action grab))");
		add("gray", "(Color gray)");
		add("green", "(Color green)");
		add("grey", "(Color gray)");
		add("grid", "(Object (Type board))");
		add("hand", "(Object (Type gripper))");
		add("has", "(Copula has)");
		add("highest", "(Relation highest)");
		add("hold", "(Command (Action hold))");
		add("in", "(SpatialIndicator in)");
		add("inside", "(SpatialIndicator inside)");
		add("into", "(SpatialIndicator into)");
		add("is", "(Copula is)");
		add("it", "(Pronoun it)");
		add("it's", "(Contraction it is)");
		add("its", "(Possessive its)");
		add("last", "(SpatialIndicator last)");
		add("left", "(Direction left)");
		add("leftmost", "(SpatialIndicator leftmost)");
		add("lift", "(Command (Action lift))");
		add("light", "(Color light)");
		add("line", "(Object (Type line))");
		add("located", "(SpatialIndicator at)");
		add("lower", "(Relation lower)");
		add("lowest", "(Relation lowest)");
		add("magenta", "(Color magenta)");
		add("margin", "(Object (Type margin))");
		add("middle", "(SpatialIndicator middle)");
		add("most", "(Relation most)");
		add("move", "(Command (Action move))");
		add("near", "(SpatialIndicator near)");
		add("nearer", "(SpatialIndicator nearer)");
		add("nearest", "(SpatialIndicator nearest)");
		add("next", "(SpatialIndicator adjacent)");
		add("not", "(Relation negative)");
		add("of", "(Relation of)");
		add("on", "(SpatialIndicator on)");
		add("one", "(Cardinal 1)");
		add("ones", "(Pronoun ones)");
		add("only", "(Relation only)");
		add("onto", "(SpatialIndicator onto)");
		add("opposite", "(SpatialIndicator opposite)");
		add("other", "(Relation other)");
		add("over", "(SpatialIndicator above)");
		add("parallelipiped", "(Object (Type stack))");
		add("pick", "(Command (Action pick))");
		add("pillar", "(Object (Type stack))");
		add("pink", "(Color magenta)");
		add("place", "(Command (Action place))");
		add("placed", "(SpatialIndicator at)");
		add("places", "(Object (Type tile) (Number plural))");
		add("position", "(Object (Type position))");
		add("prism", "(Object (Type prism))");
		add("purple", "(Color magenta)");
		add("put", "(Command (Action put))");
		add("pyramid", "(Object (Type prism))");
		add("pyramids", "(Object (Type prism) (Number plural))");
		add("red", "(Color red)");
		add("remove", "(Command (Action remove))");
		add("right", "(Direction right)");
		add("rightmost", "(SpatialIndicator rightmost)");
		add("robot", "(Object (Type robot))");
		add("row", "(Object (Type row))");
		add("rows", "(Object (Type row) (Number plural))");
		add("same", "(Relation same)");
		add("second", "(Ordinal 2)");
		add("shift", "(Command (Action shift))");
		add("side", "(Object (Type edge))");
		add("single", "(Relation single)");
		add("sits", "(SpatialIndicator above)");
		add("sitting", "(SpatialIndicator at)");
		add("six", "(Cardinal 6)");
		add("sky", "(Color cyan)");
		add("slab", "(Object (Type stack))");
		add("smallest", "(Relation smallest)");
		add("south", "(Direction south)");
		add("space", "(Object (Type space))");
		add("spaces", "(Object (Type tile) (Numer plural))");
		add("square", "(Object (Type tile))");
		add("squares", "(Object (Type tile) (Numer plural))");
		add("stack", "(Object (Type stack))");
		add("stacks", "(Object (Type stack) (Number plural))");
		add("step", "(Object (Type tile))");
		add("steps", "(Object (Type tile) (Numer plural))");
		add("surrounded", "(SpatialIndicator surrounded)");
		add("take", "(Command (Action take))");
		add("tallest", "(Relation tallest)");
		add("tetrahedron", "(Object (Type prism))");
		add("that", "(Determiner that)");
		add("that's", "(Contraction that is)");
		add("the", "(Description definite)");
		add("them", "(Pronoun them)");
		add("then", "(Conjunction then)");
		add("third", "(Ordinal 3)");
		add("this", "(Pronoun this)");
		add("three", "(Cardinal 3)");
		add("to", "(SpatialIndicator to)");
		add("top", "(Direction top)");
		add("toward", "(SpatialIndicator toward)");
		add("towards", "(SpatialIndicator toward)");
		add("tower", "(Object (Type stack))");
		add("towers", "(Object (Type stack) (Number plural))");
		add("triangle", "(Object (Type prism))");
		add("triangles", "(Object (Type prism) (Number plural))");
		add("turquoise", "(Color cyan)");
		add("two", "(Cardinal 2)");
		add("under", "(SpatialIndicator under)");
		add("underneath", "(SpatialIndicator below)");
		add("up", "(Direction up)");
		add("where", "(Determiner where)");
		add("which", "(Determiner which)");
		add("white", "(Color white)");
		add("with", "(SpatialIndicator with)");
		add("yellow", "(Color yellow)");
		add("you", "(Object (Type robot))");
		add("your", "(Pronoun your)");
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