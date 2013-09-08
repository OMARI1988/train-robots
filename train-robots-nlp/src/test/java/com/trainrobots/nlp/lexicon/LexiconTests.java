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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.nlp.commands.Command;
import com.trainrobots.nlp.commands.Corpus;
import com.trainrobots.nlp.tokenizer.Tokenizer;
import com.trainrobots.nlp.trees.Node;

public class LexiconTests {

	@Test
	@Ignore
	public void shouldFindUnknownWords() {
		
		class Item {
			String key;
			int count;
		}

		Map<String, Item> items = new HashMap<String, Item>();

		int total = 0;
		int unknown = 0;
		for (Command command : Corpus.getCommands()) {
			for (Node token : Tokenizer.getTokens(command.text).children) {
				if (!token.hasTag("Text")) {
					continue;
				}
				total++;
				String key = token.getValue();
				if (Lexicon.get(key) != null) {
					continue;
				}
				unknown++;

				Item item = items.get(key);
				if (item == null) {
					item = new Item();
					item.key = key;
					items.put(key, item);
				}
				item.count++;
			}
		}

		System.out.println("Unknown: " + unknown + " / " + total);

		List<Item> list = new ArrayList<Item>(items.values());

		Collections.sort(list, new Comparator<Item>() {
			public int compare(Item x, Item y) {
				if (x.count == y.count) {
					return 0;
				}
				return x.count < y.count ? -1 : 1;
			}
		});

		for (Item item : list) {
			System.out.println(item.count + " " + item.key);
		}
	}
}