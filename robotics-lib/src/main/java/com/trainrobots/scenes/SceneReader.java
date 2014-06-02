/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.scenes;

import org.xml.sax.Attributes;

import com.trainrobots.XmlReader;
import com.trainrobots.collections.ItemsList;

public class SceneReader extends XmlReader {

	private final Layouts layouts;
	private final ItemsList<Scene> scenes = new ItemsList<Scene>();
	private int sceneId;
	private int beforeId;
	private int afterId;

	public SceneReader(Layouts layouts) {
		this.layouts = layouts;
	}

	public Scenes scenes() {
		return new Scenes(scenes);
	}

	@Override
	protected void handleElementStart(String name, Attributes attributes) {

		switch (name) {
		case "scene":
			sceneId = Integer.parseInt(attributes.getValue("id"));
			beforeId = 0;
			afterId = 0;
			break;
		case "before":
			beforeId = Integer.parseInt(attributes.getValue("layoutId"));
			break;
		case "after":
			afterId = Integer.parseInt(attributes.getValue("layoutId"));
			break;
		}
	}

	@Override
	protected void handleElementEnd(String name) {
		switch (name) {
		case "scene":
			scenes.add(new Scene(sceneId, layouts.layout(beforeId), layouts
					.layout(afterId)));
			break;
		}
	}
}