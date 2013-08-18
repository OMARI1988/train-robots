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

package com.trainrobots.web.services;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MockServletContext implements ServletContext {

	@Override
	public Object getAttribute(String name) {
		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		return null;
	}

	@Override
	public ServletContext getContext(String uripath) {
		return null;
	}

	@Override
	public String getContextPath() {
		return null;
	}

	@Override
	public String getInitParameter(String name) {
		if (name.equals("database-url")) {
			return "jdbc:mysql://localhost/train_robots?user=java_user&password=Baz80931841";
		}
		return null;
	}

	@Override
	public Enumeration getInitParameterNames() {
		return null;
	}

	@Override
	public int getMajorVersion() {
		return 0;
	}

	@Override
	public String getMimeType(String file) {
		return null;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		return null;
	}

	@Override
	public String getRealPath(String path) {
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return null;
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return null;
	}

	@Override
	public Set getResourcePaths(String path) {
		return null;
	}

	@Override
	public String getServerInfo() {
		return null;
	}

	@Override
	public Servlet getServlet(String name) throws ServletException {
		return null;
	}

	@Override
	public String getServletContextName() {
		return null;
	}

	@Override
	public Enumeration getServletNames() {
		return null;
	}

	@Override
	public Enumeration getServlets() {
		return null;
	}

	@Override
	public void log(String msg) {
	}

	@Override
	public void log(Exception exception, String msg) {

	}

	@Override
	public void log(String msg, Throwable throwable) {
	}

	@Override
	public void removeAttribute(String name) {

	}

	@Override
	public void setAttribute(String name, Object object) {
	}
}