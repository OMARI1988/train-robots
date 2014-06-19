/* This file is part of Train Robots (www.TrainRobots.com).
 * 
 * Copyright (C) Kais Dukes, 2014.
 * Email: kais@kaisdukes.com
 *
 * Released under version 3 of the GNU General Public License (GPL).
 */

package com.trainrobots.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.scenes.Scene;
import com.trainrobots.scenes.Scenes;
import com.trainrobots.web.Application;
import com.trainrobots.web.InstructionWriter;

public class ChatServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		// Request.
		String message = request.getParameter("message");
		Log.info("Received: %s", message);

		// Simulate latency during testing.
		try {
			Thread.sleep(500);
		} catch (InterruptedException exception) {
			throw new RoboticException(exception);
		}

		// Session.
		HttpSession session = request.getSession();
		Application application = Application.get(session.getServletContext());

		// Scene.
		Scenes scenes = application.treebank().scenes();
		Scene scene = scenes.get(new Random().nextInt(scenes.count()));

		// Result.
		String result = "OK";
		String instructions = new InstructionWriter(scene.before()).write();

		// Response.
		PrintWriter out = response.getWriter();
		out.write("{\"response\": \"");
		out.write(result);
		out.write("\", \"instructions\": ");
		out.write(instructions);
		out.write("}");
	}
}