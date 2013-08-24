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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.trainrobots.web.game.User;

public class UserService {

	private final Map<Integer, List<User>> logins = new HashMap<Integer, List<User>>();

	public synchronized void signIn(HttpSession session, User user) {

		// Sign in.
		session.setAttribute("user", user);

		// Add to map.
		List<User> users = logins.get(user.userId);
		if (users == null) {
			logins.put(user.userId, users = new ArrayList<User>());
		} else {

			// Mark previous logins as superseded.
			for (User existingUser : users) {
				existingUser.superseded = true;
			}
		}
		users.add(user);
	}

	public synchronized void signOut(HttpSession session) {

		// Get cached user details from session.
		User user = (User) session.getAttribute("user");

		// Not cached?
		if (user == null) {
			return;
		}

		// Remove from session cache.
		session.removeAttribute("user");

		// Remove instance from map.
		List<User> users = logins.get(user.userId);
		if (users != null) {
			users.remove(user);
		}
	}
}