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

//import javax.inject.Singleton;

import com.trainrobots.ui.views.MainWindow;

//import com.dependencytreebank.ui.services.ParsingService;
//import com.dependencytreebank.ui.services.TreebankService;
//import com.dependencytreebank.ui.services.WindowService;
//import com.dependencytreebank.ui.services.implementation.DefaultParsingService;
//import com.dependencytreebank.ui.services.implementation.DefaultTreebankService;
//import com.dependencytreebank.ui.services.implementation.DefaultWindowService;
//import com.dependencytreebank.ui.views.MainWindow;

import dagger.Module;
//import dagger.Provides;

@Module(entryPoints = { MainWindow.class })
public class UiModule {

//	@Provides
//	@Singleton
//	public WindowService provideWindowService() {
//		return new DefaultWindowService();
//	}
//
//	@Provides
//	@Singleton
//	public ParsingService provideParsingService() {
//		return new DefaultParsingService();
//	}
//
//	@Provides
//	@Singleton
//	public TreebankService provideTreebankService() {
//		return new DefaultTreebankService();
//	}
}