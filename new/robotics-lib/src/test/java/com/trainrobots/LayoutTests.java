/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under Version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.scenes.Layout;

public class LayoutTests {

	@Test
	public void shouldReadScenes() {
		int i = 0;
		for (Layout layout : Robotics.system().layouts()) {
			assertThat(layout.id(), is(++i));
			assertThat(layout.gripper(), is(not(nullValue())));
		}
	}
}