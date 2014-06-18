/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.treebank;

import org.junit.Ignore;
import org.junit.Test;

import com.trainrobots.Context;

public class CommentTests {

	@Test
	@Ignore
	public void shouldShowComments() {
		for (Command command : Context.treebank().commands()) {
			String filter = "near";
			if (command.comment() != null
					&& (filter == null || command.comment().equals(filter))) {
				System.out.println(command.id() + ": " + command.comment());
			}
		}
	}
}