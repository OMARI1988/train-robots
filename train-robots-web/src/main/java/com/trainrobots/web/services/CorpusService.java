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

package com.trainrobots.web.services;

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.DataContext;
import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.nlp.tokenizer.Tokenizer;

public class CorpusService {

	private int wordCount;
	private int commandCount;
	private final List<int[]> sceneInfo = new ArrayList<int[]>();

	static {
		DataContext.setDataPath("data");
	}

	public CorpusService() {
		for (int i = 1; i <= 1000; i++) {
			List<Command> list = Corpus.getAccurateCommands(i);
			if (list != null && list.size() >= 1) {
				process(i, list);
			}
		}
	}

	public int getWordCount() {
		return wordCount;
	}

	public int getCommandCount() {
		return commandCount;
	}

	public List<Command> getCommands(int sceneNumber) {
		return Corpus.getAccurateCommands(sceneNumber);
	}

	public List<int[]> getSceneInfo() {
		return sceneInfo;
	}

	private void process(int sceneNumber, List<Command> commands) {
		int count = 0;
		for (Command command : commands) {
			count++;
			wordCount += Tokenizer.getTokens(command.text).children.size();
		}
		commandCount += count;
		sceneInfo.add(new int[] { sceneNumber, count });
	}
}