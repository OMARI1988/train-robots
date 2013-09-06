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
		add("a", "(State indefinite)");
		add("above", "(Direction above)");
		add("after", "(SpatialRelation after)");
		add("air", "(Object air)");
		add("all", "(Relation all)");
		add("an", "(State indefinite)");
		add("and", "(Conjunction and)");
		add("another", "(Relation another)");
		add("are", "(Copula are)");
		add("arm", "(Object arm)");
		add("at", "(SpatialRelation at)");
		add("away", "(Relation away)");
		add("back", "(Direction back)");
		add("backwards", "(Direction back)");
		add("be", "(Copula be)");
		add("behind", "(SpatialRelation behind)");
		add("below", "(SpatialRelation below)");
		add("beside", "(SpatialRelation beside)");
		add("between", "(SpatialRelation between)");
		add("block", "(Object block)");
		add("blocks", "(Object (Object block) (Number plural))");
		add("blue", "(Color blue)");
		add("board", "(Object board)");
		add("border", "(Object edge)");
		add("bottom", "(Direction back)");
		add("box", "(Object block)");
		add("brick", "(Object block)");
		add("bricks", "(Object (Object block) (Number plural))");
		add("but", "(Conjunction but)");
		add("by", "(SpatialRelation at)");
		add("center", "(Object center)");
		add("centre", "(Object center)");
		add("close", "(SpatialRelation near)");
		add("closer", "(SpatialRelation near)");
		add("closest", "(SpatialRelation nearest)");
		add("column", "(Object column)");
		add("columns", "(Object (Object column) (Number plural))");
		add("cone", "(Object prism)");
		add("corner", "(Object corner)");
		add("cube", "(Object block)");
		add("cubes", "(Object (Object block) (Number plural))");
		add("current", "(Relation current)");
		add("cyan", "(Color cyan)");
		add("dark", "(Color dark)");
		add("diagonal", "(SpatialRelation diagonal)");
		add("diagonally", "(SpatialRelation diagonal)");
		add("different", "(Relation different)");
		add("direction", "(SpatialRelation direction)");
		add("directly", "(Relation direct)");
		add("do", "(Action do)");
		add("down", "(Direction down)");
		add("drop", "(Action drop)");
		add("east", "(Direction east)");
		add("edge", "(Object edge)");
		add("elevation", "(Object elevation)");
		add("empty", "(Relation empty)");
		add("exactly", "(Relation exactly)");
		add("far", "(SpatialRelation far)");
		add("farthest", "(SpatialRelation furthest)");
		add("first", "(Ordinal first)");
		add("five", "(Cardinal five)");
		add("floor", "(Object board)");
		add("forward", "(Direction forward)");
		add("four", "(Cardinal four)");
		add("fourth", "(Ordinal fourth)");
		add("from", "(SpatialRelation from)");
		add("front", "(Direction front)");
		add("furthest", "(SpatialRelation furthest)");
		add("grab", "(Action grab)");
		add("gray", "(Color gray)");
		add("green", "(Color green)");
		add("grey", "(Color gray)");
		add("grid", "(Object board)");
		add("hand", "(Object gripper)");
		add("has", "(Copula has)");
		add("highest", "(Relation highest)");
		add("hold", "(Action hold)");
		add("in", "(SpatialRelation in)");
		add("inside", "(SpatialRelation inside)");
		add("into", "(SpatialRelation into)");
		add("is", "(Copula is)");
		add("it", "(Pronoun it)");
		add("it's", "(Contraction it is)");
		add("its", "(Possessive its)");
		add("last", "(SpatialRelation last)");
		add("left", "(Direction left)");
		add("leftmost", "(SpatialRelation leftmost)");
		add("lift", "(Action lift)");
		add("light", "(Color light)");
		add("line", "(Object line)");
		add("located", "(SpatialRelation at)");
		add("lower", "(Relation lower)");
		add("lowest", "(Relation lowest)");
		add("magenta", "(Color magenta)");
		add("margin", "(Object margin)");
		add("middle", "(SpatialRelation middle)");
		add("most", "(Relation most)");
		add("move", "(Action move)");
		add("near", "(SpatialRelation near)");
		add("nearer", "(SpatialRelation nearer)");
		add("nearest", "(SpatialRelation nearest)");
		add("next", "(SpatialRelation adjacent)");
		add("not", "(Relation negative)");
		add("of", "(Relation of)");
		add("on", "(SpatialRelation on)");
		add("one", "(Cardinal one)");
		add("ones", "(Pronoun ones)");
		add("only", "(Relation only)");
		add("onto", "(SpatialRelation onto)");
		add("opposite", "(SpatialRelation opposite)");
		add("other", "(Relation other)");
		add("over", "(SpatialRelation above)");
		add("parallelipiped", "(Object stack)");
		add("pick", "(Action pick)");
		add("pillar", "(Object stack)");
		add("pink", "(Color magenta)");
		add("place", "(Action place)");
		add("placed", "(SpatialRelation at)");
		add("places", "(Object (Object tile) (Number plural))");
		add("position", "(Object position)");
		add("prism", "(Object prism)");
		add("purple", "(Color magenta)");
		add("put", "(Action put)");
		add("pyramid", "(Object prism)");
		add("pyramids", "(Object (Object prism) (Number plural))");
		add("red", "(Color red)");
		add("remove", "(Action remove)");
		add("right", "(Direction right)");
		add("rightmost", "(SpatialRelation rightmost)");
		add("robot", "(Object robot)");
		add("row", "(Object row)");
		add("rows", "(Object (Object row) (Number plural))");
		add("same", "(Relation same)");
		add("second", "(Ordinal second)");
		add("shift", "(Action shift)");
		add("side", "(Object edge)");
		add("single", "(Number single)");
		add("sits", "(SpatialRelation above)");
		add("sitting", "(SpatialRelation at)");
		add("six", "(Cardinal six)");
		add("sky", "(Color cyan)");
		add("slab", "(Object stack)");
		add("smallest", "(Relation smallest)");
		add("south", "(Direction south)");
		add("space", "(Object space)");
		add("spaces", "(Object (Object tile) (Numer plural))");
		add("square", "(Object tile)");
		add("squares", "(Object (Object tile) (Numer plural))");
		add("stack", "(Object stack)");
		add("stacks", "(Object (Object stack) (Number plural))");
		add("step", "(Object tile)");
		add("steps", "(Object (Object tile) (Numer plural))");
		add("surrounded", "(SpatialRelation surrounded)");
		add("take", "(Action take)");
		add("tallest", "(Relation tallest)");
		add("tetrahedron", "(Object prism)");
		add("that", "(Determiner that)");
		add("that's", "(Contraction that is)");
		add("the", "(State definite)");
		add("them", "(Pronoun them)");
		add("then", "(Conjunction then)");
		add("third", "(Ordinal third)");
		add("this", "(Pronoun this)");
		add("three", "(Cardinal three)");
		add("to", "(SpatialRelation to)");
		add("top", "(Direction top)");
		add("toward", "(SpatialRelation toward)");
		add("towards", "(SpatialRelation toward)");
		add("tower", "(Object stack)");
		add("towers", "(Object (Object stack) (Number plural))");
		add("triangle", "(Object prism)");
		add("triangles", "(Object (Object prism) (Number plural))");
		add("turquoise", "(Color cyan)");
		add("two", "(Cardinal two)");
		add("under", "(SpatialRelation under)");
		add("underneath", "(SpatialRelation below)");
		add("up", "(Direction up)");
		add("where", "(Determiner where)");
		add("which", "(Determiner which)");
		add("white", "(Color white)");
		add("with", "(SpatialRelation with)");
		add("yellow", "(Color yellow)");
		add("you", "(Object robot)");
		add("your", "(Pronoun your)");
	}

	private static void add(String text, String entry) {
		if (entries.containsKey(text)) {
			throw new NlpException("Duplicate lexicon entry: " + text);
		}
		entries.put(text, Node.fromString(entry));
	}
}