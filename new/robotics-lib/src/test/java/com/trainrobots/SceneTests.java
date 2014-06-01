/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.scenes.Scene;

public class SceneTests {

	@Test
	public void shouldReadScenes() {
		int i = 0;
		for (Scene scene : Robotics.system().scenes()) {
			assertThat(scene.id(), is(++i));
		}
		assertThat(i, is(1000));
	}
}