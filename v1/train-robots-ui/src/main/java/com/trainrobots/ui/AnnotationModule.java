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

package com.trainrobots.ui;

import javax.inject.Singleton;

import com.trainrobots.ui.services.ConfigurationService;
import com.trainrobots.ui.services.CorpusService;
import com.trainrobots.ui.services.WindowService;
import com.trainrobots.ui.services.defaults.DefaultConfigurationService;
import com.trainrobots.ui.services.defaults.DefaultCorpusService;
import com.trainrobots.ui.services.defaults.DefaultWindowService;
import com.trainrobots.ui.views.AnnotationWindow;

import dagger.Module;
import dagger.Provides;

@Module(entryPoints = { AnnotationWindow.class })
public class AnnotationModule {

	@Provides
	@Singleton
	public WindowService provideWindowService() {
		return new DefaultWindowService();
	}

	@Provides
	@Singleton
	public CorpusService provideCorpusService() {
		return new DefaultCorpusService();
	}

	@Provides
	@Singleton
	public ConfigurationService provideConfigurationService() {
		return new DefaultConfigurationService();
	}
}