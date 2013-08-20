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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.joda.time.DateTime;

import com.trainrobots.web.WebException;
import com.trainrobots.web.game.User;

public class DataService {

	public DataService() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException exception) {
			throw new WebException(exception);
		}
	}

	public User getUser(ServletContext context, String email) {
		try {

			// Connect.
			String databaseUrl = context.getInitParameter("database-url");
			Connection connection = DriverManager.getConnection(databaseUrl);

			// Initiate statement.
			CallableStatement statement = connection
					.prepareCall("{call select_user(?)}");
			statement.setString(1, email);

			// Execute.
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			User user = null;
			if (resultSet.next()) {
				user = new User();
				user.userId = resultSet.getInt(1);
				user.status = resultSet.getInt(2);
				user.round = resultSet.getInt(3);
				user.score = resultSet.getInt(4);
				user.potential = resultSet.getInt(5);
				user.gameName = resultSet.getString(6);
				user.email = resultSet.getString(7);
				user.password = resultSet.getString(8);
				user.registrationUtc = new DateTime(resultSet.getTimestamp(9));
				user.lastScoreUtc = new DateTime(resultSet.getTimestamp(10));
				user.signInMessage = resultSet.getString(11);
			}

			// Close connection.
			resultSet.close();
			statement.close();
			connection.close();

			// Return user.
			return user;

		} catch (SQLException exception) {
			throw new WebException(exception);
		}
	}

	public void addRound(ServletContext context, int userId, int round,
			int score, int potential, int sceneNumber, int expectedOption,
			int selectedOption, String ipAddress, String command) {
		try {

			// Connect.
			String databaseUrl = context.getInitParameter("database-url");
			Connection connection = DriverManager.getConnection(databaseUrl);

			// Initiate statement.
			CallableStatement statement = connection
					.prepareCall("{call add_round(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			statement.setInt(1, userId);
			statement.setInt(2, round);
			statement.setInt(3, score);
			statement.setInt(4, potential);
			statement.setInt(5, sceneNumber);
			statement.setInt(6, expectedOption);
			statement.setInt(7, selectedOption);
			statement.setString(8, ipAddress);
			statement.setString(9, command);

			// Execute.
			statement.executeUpdate();

			// Close connection.
			statement.close();
			connection.close();

		} catch (SQLException exception) {
			throw new WebException(exception);
		}
	}
}