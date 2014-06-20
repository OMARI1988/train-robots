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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;
import com.trainrobots.nlp.agent.Agent;
import com.trainrobots.web.Application;
import com.trainrobots.web.ChatState;
import com.trainrobots.web.InstructionWriter;

public class ChatServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		// Request.
		String message = request.getParameter("message");
		Log.info("Received: %s", message);

		// Handle.
		try {
			handleRequest(request, response);
		} catch (Exception exception) {
			Log.error("Failed to handle request.", exception);
			throw exception;
		}
	}

	private void handleRequest(HttpServletRequest request,
			HttpServletResponse response) {

		// Simulate latency during testing.
		try {
			Thread.sleep(500);
		} catch (InterruptedException exception) {
			throw new RoboticException(exception);
		}

		// Session.
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		// Agent.
		Agent agent = Application.get(context).agent();

		// Request.
		ChatState state = ChatState.get(session);
		String message = request.getParameter("message");

		// Result.
		String result;
		try {
			result = agent.process(state.layout(), message);
		} catch (Exception exception) {
			Log.error("Failed to handle message.", exception);
			result = exception.getMessage();
		}

		// Output.
		try {
			PrintWriter out = response.getWriter();
			out.write("{\"response\": \"");
			out.write(result);
			out.write("\", \"instructions\": ");
			out.write(new InstructionWriter(state.layout()).write());
			out.write("}");
		} catch (IOException exception) {
			throw new RoboticException(exception);
		}
	}
}