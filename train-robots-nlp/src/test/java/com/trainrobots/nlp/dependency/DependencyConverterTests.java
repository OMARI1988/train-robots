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

package com.trainrobots.nlp.dependency;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.core.corpus.Command;
import com.trainrobots.core.corpus.Corpus;
import com.trainrobots.core.rcl.Rcl;

public class DependencyConverterTests {

	@Test
	@Ignore
	public void shouldConvertCorpus() {
		for (Command command : Corpus.getCommands()) {
			Rcl rcl = command.rcl;
			if (rcl == null) {
				continue;
			}
			try {
				DependencyConverter.convert(command.text, command.rcl);
			} catch (Exception exception) {
				System.err.println(command.id + ": " + command.text);
				System.err.println(command.rcl.format());
				exception.printStackTrace();
			}
		}
	}
}