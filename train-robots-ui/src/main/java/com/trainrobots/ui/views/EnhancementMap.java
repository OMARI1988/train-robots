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

package com.trainrobots.ui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.trainrobots.core.corpus.Enhancement;

public class EnhancementMap {

	private static final String[] descriptions;
	private static final int[] idToDescriptionIndex;
	private static final int[] descriptionIndexToId;

	private EnhancementMap() {
	}

	static {

		// Sort.
		ArrayList<Item> items = new ArrayList<Item>();
		String[] list = Enhancement.getDescriptions();
		for (int i = 0; i < list.length; i++) {
			Item item = new Item();
			item.description = list[i];
			item.id = i + 1;
			items.add(item);
		}
		Collections.sort(items, new Comparator<Item>() {
			public int compare(Item a, Item b) {
				return a.description.compareTo(b.description);
			}
		});
		Item defaultItem = new Item();
		defaultItem.description = "No enhacement";
		items.add(0, defaultItem);

		// Descriptions.
		int size = items.size();
		descriptions = new String[size];
		for (int i = 0; i < size; i++) {
			descriptions[i] = items.get(i).description;
		}

		// Indices.
		idToDescriptionIndex = new int[size];
		descriptionIndexToId = new int[size];
		for (int i = 0; i < size; i++) {
			int id = items.get(i).id;
			idToDescriptionIndex[id] = i;
			descriptionIndexToId[i] = id;
		}
	}

	public static String[] getDescriptions() {
		return descriptions;
	}

	public static int getId(int descriptionIndex) {
		return descriptionIndexToId[descriptionIndex];
	}

	public static int getDescriptionIndex(int id) {
		return idToDescriptionIndex[id];
	}

	private static class Item {
		String description;
		int id;
	}
}