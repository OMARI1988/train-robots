/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp;

import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.Context;
import com.trainrobots.nlp.losr.StopWords;
import com.trainrobots.treebank.Command;

public class StopWordTests {

	private static Set<String> stopWords = new HashSet<>();

	static {
		stopWords.add(".");
		stopWords.add(",");
		stopWords.add("-");
		stopWords.add("!");
		stopWords.add("a");
		stopWords.add("is");
		stopWords.add("which");
		stopWords.add("that");
		stopWords.add("the");
		stopWords.add("and");
		stopWords.add("then");
		stopWords.add("to");
	}

	@Test
	@Ignore
	public void shouldFindStopWords() {
		FrequencyTable table = new FrequencyTable();
		for (Command command : Context.treebank().commands()) {
			if (command.losr() != null) {
				StopWords.visit(command, x -> {
					String text = x.context().text().toLowerCase();
					if (!stopWords.contains(text)) {
						table.add(text);
					}
				});
			}
		}
		for (FrequencyTable.Entry entry : table) {
			System.out.println(entry);
		}
	}
}