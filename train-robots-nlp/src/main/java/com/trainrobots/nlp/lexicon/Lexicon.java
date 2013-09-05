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

	public static Node get(String text) {
		Node node = entries.get(text);
		return node != null ? node.clone() : null;
	}

	static {
		add("the", "(State definite)");
		add("block", "(Class block)");
		add("on", "(SpatialRelationType on)");
		add("and", "(Conjunction and)");
		add("of", "(RelationType member)");
		add("top", "(Direction top)");
		add("red", "(Color red)");
		add("blue", "(Color blue)");
		add("cube", "(Class block)");
		add("place", "(Action place)");
		add("it", "(Pronoun it)");
		add("pick", "(Action pick)");
		add("move", "(Action move)");
		add("green", "(Color green)");
		add("pyramid", "(Class prism)");
		add("yellow", "(Color yellow)");
		add("to", "(SpatialRelationType to)");
		add("up", "(Direction up)");
		add("left", "(Direction left)");
		add("corner", "(Class corner)");
		add("from", "(SpatialRelationType from)");
		add("right", "(Direction right)");
		add("is", "(Copula is)");
		add("in", "(SpatialRelationType in)");
		add("grey", "(Color gray)");
		add("put", "(Action put)");
		add("tower", "(Class stack)");
		add("that", "(Determiner that)");
		add("which", "(Determiner which)");
		add("one", "(Cardinal one)");
		add("prism", "(Class prism)");
		add("white", "(Color white)");
		add("two", "(Cardinal two)");
		add("blocks", "(Object (Class block) (Number plural))");
		add("above", "(Direction above)");
		add("square", "(Class tile)");
		add("bottom", "(Direction back)");
		add("between", "(SpatialRelationType between)");
		add("closest", "(SpatialRelationType nearest)");
		add("turquoise", "(Color cyan)");
		add("squares", "(Object (Class tile) (Numer plural))");
		add("placed", "(SpatialRelationType at)");
		add("at", "(SpatialRelationType at)");
		add("edge", "(Class edge)");
		add("gray", "(Color gray)");
		add("take", "(Action take)");
		add("over", "(SpatialRelationType above)");
		add("same", "(Relation same)");
		add("row", "(Class row)");
		add("pink", "(Color magenta)");
		add("tetrahedron", "(Class prism)");
		add("back", "(Direction back)");
		add("drop", "(Action drop)");
		add("nearest", "(SpatialRelationType nearest)");
		add("column", "(Class column)");
		add("cubes", "(Object (Class block) (Number plural))");
		add("single", "(Number single)");
		add("brick", "(Class block)");
		add("cyan", "(Color cyan)");
		add("board", "(Class board)");
		add("near", "(SpatialRelationType near)");
		add("next", "(SpatialRelationType adjacent)");
		add("with", "(SpatialRelationType with)");
		add("sky", "(Color cyan)");
		add("a", "(State indefinite)");
		add("parallelipiped", "(Class stack)");
		add("opposite", "(SpatialRelationType opposite)");
		add("three", "(Cardinal three)");
		add("other", "(SpatialRelationType other)");
		add("position", "(Class position)");
		add("grid", "(Class board)");
		add("side", "(Class edge)");
		add("purple", "(Color magenta)");
		add("towards", "(SpatialRelationType to)");
		add("remove", "(Action remove)");
		add("stack", "(Class stack)");
		add("line", "(Class line)");
		add("middle", "(SpatialRelationType middle)");
		add("far", "(SpatialRelationType far)");
		add("you", "(Class robot)");
		add("most", "(Relation most)");
		add("located", "(SpatialRelationType at)");
		add("away", "(Relation away)");
		add("four", "(Cardinal four)");
		add("your", "(Pronoun your)");
		add("forward", "(Direction forward)");
		add("hold", "(Action hold)");
		add("onto", "(SpatialRelationType onto)");
		add("slab", "(Class stack)");
		add("center", "(Class center)");
		add("current", "(Relation current)");
		add("second", "(Ordinal second)");
		add("sitting", "(SpatialRelationType at)");
		add("rows", "(Object (Class row) (Number plural))");
		add("down", "(Direction down)");
		add("empty", "(Relation empty)");
		add("highest", "(Relation highest)");
		add("robot", "(Class robot)");
		add("shift", "(Action shift)");
		add("its", "(Possessive its)");
		add("front", "(Direction front)");
		add("closer", "(SpatialRelationType near)");
		add("farthest", "(SpatialRelationType farthest)");
		add("not", "(Relation negative)");
		add("light", "(Color light)");
		add("backwards", "(Direction back)");
		add("columns", "(Object (Class column) (Number plural))");
		add("space", "(Class space)");
		add("this", "(Pronoun this)");
		add("below", "(SpatialRelationType below)");
		add("are", "(Copula are)");
		add("tallest", "(Relation tallest)");
		add("pyramids", "(Object (Class prism) (Number plural))");
		add("arm", "(Class arm)");
		add("has", "(Copula has)");
		add("only", "(Relation only)");
		add("magenta", "(Color magenta)");
		add("step", "(Class tile)");
		add("box", "(Class block)");
		add("sits", "(SpatialRelationType above)");
		add("dark", "(Color dark)");
		add("another", "(Relation another)");
		add("inside", "(SpatialRelationType inside)");
		add("then", "(Conjunction then)");
		add("towers", "(Object (Class stack) (Number plural))");
		add("steps", "(Object (Class tile) (Numer plural))");
		add("rightmost", "(SpatialRelationType rightmost)");
		add("centre", "(Class center)");
		add("triangle", "(Class prism)");
		add("third", "(Ordinal third)");
		add("leftmost", "(SpatialRelationType leftmost)");
		add("five", "(Cardinal five)");
		add("where", "(Determiner where)");
		add("into", "(SpatialRelationType into)");
		add("bricks", "(Object (Class block) (Number plural))");
		add("direction", "(SpatialRelationType direction)");
		add("beside", "(SpatialRelationType beside)");
		add("first", "(Ordinal first)");
		add("last", "(SpatialRelationType last)");
		add("after", "(SpatialRelationType after)");
		add("fourth", "(Ordinal fourth)");
		add("by", "(SpatialRelationType at)");
		add("elevation", "(Class elevation)");
		add("hand", "(Class gripper)");
		add("but", "(Conjunction but)");
		add("that's", "(Contraction that is)");
		add("exactly", "(Relation exactly)");
		add("underneath", "(SpatialRelationType below)");
		add("floor", "(Class board)");
		add("air", "(Class air)");
		add("smallest", "(Relation smallest)");
		add("furthest", "(SpatialRelationType farthest)");
		add("diagonally", "(SpatialRelationType diagonal)");
		add("directly", "(Relation direct)");
		add("lift", "(Action lift)");
		add("pillar", "(Class stack)");
		add("it's", "(Contraction it is)");
		add("east", "(Direction east)");
		add("be", "(Copula be)");
		add("behind", "(SpatialRelationType behind)");
		add("six", "(Cardinal six)");
		add("close", "(SpatialRelationType near)");
		add("diagonal", "(SpatialRelationType diagonal)");
		add("surrounded", "(SpatialRelationType surrounded)");
		add("do", "(Action do)");
		add("ones", "(Pronoun ones)");
		add("grab", "(Action grab)");
		add("all", "(Relation all)");
		add("south", "(Direction south)");
		add("nearer", "(SpatialRelationType nearer)");
		add("margin", "(Class margin)");
		add("lowest", "(Relation lowest)");
		add("lower", "(Relation lower)");
		add("cone", "(Class prism)");
		add("spaces", "(Object (Class tile) (Numer plural))");
		add("an", "(State indefinite)");
		add("border", "(Object (Class edge) (Number plural))");
		add("under", "(SpatialRelationType under)");
		add("them", "(Pronoun them)");
	}

	private static void add(String text, String entry) {
		if (entries.containsKey(text)) {
			throw new NlpException("Duplicate lexicon entry: " + text);
		}
		entries.put(text, Node.fromString(entry));
	}
}