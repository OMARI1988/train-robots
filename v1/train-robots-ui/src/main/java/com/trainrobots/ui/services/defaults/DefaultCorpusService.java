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

package com.trainrobots.ui.services.defaults;

import java.util.List;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.ui.services.CorpusService;

public class DefaultCorpusService implements CorpusService {

	@Override
	public Command getCommand(int id) {
		return Corpus.getCommand(id);
	}

	@Override
	public List<Command> getCommands(int sceneNumber) {
		return Corpus.getCommands(sceneNumber);
	}

	@Override
	public void save() {
		Corpus.saveAnnotation();
	}
}