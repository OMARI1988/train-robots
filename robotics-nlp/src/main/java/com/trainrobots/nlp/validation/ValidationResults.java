/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.trainrobots.collections.Items;
import com.trainrobots.treebank.Command;

public class ValidationResults implements Items<ValidationResult> {

	private final List<ValidationResult> list = new ArrayList<>();
	private final Map<Integer, ValidationResult> map = new HashMap<>();

	public void add(Command command, String message) {
		ValidationResult result = new ValidationResult(command, message);
		list.add(result);
		map.put(result.command().id(), result);
	}

	@Override
	public int count() {
		return list.size();
	}

	@Override
	public ValidationResult get(int index) {
		return list.get(index);
	}

	public ValidationResult get(Command command) {
		return map.get(command.id());
	}

	@Override
	public Iterator<ValidationResult> iterator() {
		return list.iterator();
	}

	@Override
	public ValidationResult[] toArray() {
		ValidationResult[] array = new ValidationResult[list.size()];
		list.toArray(array);
		return array;
	}
}