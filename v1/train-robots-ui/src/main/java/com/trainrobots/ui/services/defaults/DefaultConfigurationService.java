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

import com.trainrobots.core.DataContext;
import com.trainrobots.core.configuration.Configuration;
import com.trainrobots.core.configuration.ConfigurationReader;
import com.trainrobots.core.configuration.ConfigurationWriter;
import com.trainrobots.ui.services.ConfigurationService;

public class DefaultConfigurationService implements ConfigurationService {

	private final SceneInfo[] scenes = new SceneInfo[1000];
	private final Configuration[][] items = new Configuration[125][5];

	public DefaultConfigurationService() {

		// Configuration.
		for (Configuration c : ConfigurationReader.read(DataContext
				.getFile("configuration.txt"))) {
			update(c);
		}

		// Scenes.
		int i = 0;
		for (int g = 0; g < 125; g++) {
			for (int c = 1; c < 5; c++) {
				scenes[i++] = new SceneInfo(g, 0, c);
				scenes[i++] = new SceneInfo(g, c, 0);
			}
		}
	}

	@Override
	public int getGroupCount() {
		return 125;
	}

	@Override
	public int getSceneCount() {
		return 1000;
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
	public Configuration getBefore(int sceneNumber) {
		SceneInfo s = scenes[sceneNumber - 1];
		return items[s.groupNumber][s.fromImage];
	}

	@Override
	public Configuration getAfter(int sceneNumber) {
		SceneInfo s = scenes[sceneNumber - 1];
		return items[s.groupNumber][s.toImage];
	}

	@Override
	public void save() {
		List<Configuration> list = new ArrayList<Configuration>();
		for (int g = 0; g < 125; g++) {
			for (int i = 0; i < 5; i++) {
				list.add(items[g][i]);
			}
		}
		ConfigurationWriter.write(DataContext.getFile("configuration.txt"),
				list);
	}

	private static class SceneInfo {

		public SceneInfo(int groupNumber, int fromImage, int toImage) {
			this.groupNumber = groupNumber;
			this.fromImage = fromImage;
			this.toImage = toImage;
		}

		public final int groupNumber;
		public final int fromImage;
		public final int toImage;
	}
}