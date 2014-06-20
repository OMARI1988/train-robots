/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.trainrobots.nlp.agent.Agent;
import com.trainrobots.scenes.Layout;

public class AgentTests {

	@Test
	public void shouldExecuteCommand() {

		// Execute.
		Agent agent = NlpContext.agent();
		Layout layout = NlpContext.treebank().scene(124).before();
		String response = agent.process(layout, "pick up the red prism");

		// Verify.
		assertThat(response, is("OK"));
	}
}