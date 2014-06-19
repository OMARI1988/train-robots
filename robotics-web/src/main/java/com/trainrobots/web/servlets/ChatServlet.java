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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trainrobots.Log;
import com.trainrobots.RoboticException;

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

		// Response.
		PrintWriter out = response.getWriter();
		out.write("{\"response\": \"aaa\", \"instructions\": \"bbb\"}");
	}
}