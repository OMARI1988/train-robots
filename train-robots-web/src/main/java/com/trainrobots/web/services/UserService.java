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

import javax.servlet.http.HttpSession;

import com.trainrobots.web.game.User;

public class UserService {

	public void signIn(HttpSession session, User user) {

		// Sign in.
		session.setAttribute("user", user);
		user.signedIn = true;
	}

	public void signOut(HttpSession session) {

		// Get cached user details from session.
		User user = (User) session.getAttribute("user");

		// Not cached?
		if (user == null) {
			return;
		}

		// Clear user details.
		user.signedIn = false;

		// Remove from session cache.
		session.removeAttribute("user");
	}
}