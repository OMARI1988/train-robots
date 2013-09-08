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

import java.util.ArrayList;
import java.util.List;

import com.trainrobots.core.configuration.Configuration;
import com.trainrobots.core.configuration.ConfigurationReader;
import com.trainrobots.core.configuration.ConfigurationWriter;
import com.trainrobots.ui.services.DataService;

public class DefaultDataService implements DataService {

	private static final String FILE = "../data/configuration.txt";
	private Configuration[][] items = new Configuration[125][5];

	public DefaultDataService() {
		for (Configuration c : ConfigurationReader.read(FILE)) {
			update(c);
		}
	}

	@Override
	public int getGroupCount() {
		return 125;
	}

	@Override
	public void update(Configuration configuration) {
		items[configuration.groupNumber - 1][configuration.imageNumber - 1] = configuration;
	}

	@Override
	public Configuration get(int groupNumber, int imageNumber) {
		return items[groupNumber - 1][imageNumber - 1];
	}

	@Override
	public void save() {
		List<Configuration> list = new ArrayList<Configuration>();
		for (int g = 0; g < 125; g++) {
			for (int i = 0; i < 5; i++) {
				list.add(items[g][i]);
			}
		}
		ConfigurationWriter.write(FILE, list);
	}
}