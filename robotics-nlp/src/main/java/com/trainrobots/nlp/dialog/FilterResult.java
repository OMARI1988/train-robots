/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.nlp.dialog;

import com.trainrobots.collections.Items;
import com.trainrobots.losr.Terminal;

public class FilterResult {

	private final String response;
	private final Items<Terminal> reduction;

	public FilterResult(String response) {
		this.response = response;
		this.reduction = null;
	}

	public FilterResult(Items<Terminal> reduction) {
		this.reduction = reduction;
		this.response = null;
	}

	public String response() {
		return response;
	}

	public Items<Terminal> reduction() {
		return reduction;
	}
}