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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.trainrobots.web.WebException;
import com.trainrobots.web.game.MarkedCommand;
import com.trainrobots.web.game.ResetToken;
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
				user.registrationUtc = getUtc(resultSet, 9);
				user.lastScoreUtc = getUtc(resultSet, 10);
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

	public int validateRegistration(ServletContext context, String email,
			String name) {
		try {

			// Connect.
			String databaseUrl = context.getInitParameter("database-url");
			Connection connection = DriverManager.getConnection(databaseUrl);

			// Initiate statement.
			CallableStatement statement = connection
					.prepareCall("{call validate_registration(?, ?)}");
			statement.setString(1, email);
			statement.setString(2, name);

			// Execute.
			int result = -1;
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				result = resultSet.getInt(1);
			}

			// Close connection.
			resultSet.close();
			statement.close();
			connection.close();

			// Result.
			return result;

		} catch (SQLException exception) {
			throw new WebException(exception);
		}
	}

	public void addUser(ServletContext context, String email, String name,
			String password) {

		try {

			// Connect.
			String databaseUrl = context.getInitParameter("database-url");
			Connection connection = DriverManager.getConnection(databaseUrl);

			// Initiate statement.
			CallableStatement statement = connection
					.prepareCall("{call add_user(?, ?, ?)}");
			statement.setString(1, email);
			statement.setString(2, name);
			statement.setString(3, password);

			// Execute.
			statement.executeUpdate();

			// Close connection.
			statement.close();
			connection.close();

		} catch (SQLException exception) {
			throw new WebException(exception);
		}
	}

	public String addPasswordResetToken(ServletContext context, String email) {

		try {

			// Connect.
			String databaseUrl = context.getInitParameter("database-url");
			Connection connection = DriverManager.getConnection(databaseUrl);

			// Initiate statement.
			CallableStatement statement = connection
					.prepareCall("{call add_password_reset_token(?)}");
			statement.setString(1, email);

			// Execute.
			String result = null;
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			if (resultSet.next()) {
				result = resultSet.getString(1);
			}

			// Close connection.
			resultSet.close();
			statement.close();
			connection.close();

			// Result.
			return result;

		} catch (SQLException exception) {
			throw new WebException(exception);
		}
	}

	public ResetToken getPasswordResetToken(ServletContext context, String token) {
		try {

			// Connect.
			String databaseUrl = context.getInitParameter("database-url");
			Connection connection = DriverManager.getConnection(databaseUrl);

			// Initiate statement.
			CallableStatement statement = connection
					.prepareCall("{call select_password_reset_token(?)}");
			statement.setString(1, token);

			// Execute.
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			ResetToken resetToken = null;
			if (resultSet.next()) {
				resetToken = new ResetToken();
				resetToken.userId = resultSet.getInt(1);
				resetToken.requestUtc = getUtc(resultSet, 2);
			}

			// Close connection.
			resultSet.close();
			statement.close();
			connection.close();

			// Return token.
			return resetToken;

		} catch (SQLException exception) {
			throw new WebException(exception);
		}
	}

	public void changePassword(ServletContext context, int userId,
			String password) {

		try {

			// Connect.
			String databaseUrl = context.getInitParameter("database-url");
			Connection connection = DriverManager.getConnection(databaseUrl);

			// Initiate statement.
			CallableStatement statement = connection
					.prepareCall("{call change_password(?, ?)}");
			statement.setInt(1, userId);
			statement.setString(2, password);

			// Execute.
			statement.executeUpdate();

			// Close connection.
			statement.close();
			connection.close();

		} catch (SQLException exception) {
			throw new WebException(exception);
		}
	}

	private static DateTime getUtc(ResultSet resultSet, int columnIndex)
			throws SQLException {
		DateTime x = new DateTime(resultSet.getTimestamp(columnIndex));
		return new DateTime(x.getYear(), x.getMonthOfYear(), x.getDayOfMonth(),
				x.getHourOfDay(), x.getMinuteOfHour(), x.getSecondOfMinute(),
				x.getMillisOfSecond(), DateTimeZone.UTC);
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

	public List<MarkedCommand> getMarkedCommands(ServletContext context) {
		try {

			// Connect.
			String databaseUrl = context.getInitParameter("database-url");
			Connection connection = DriverManager.getConnection(databaseUrl);

			// Initiate statement.
			CallableStatement statement = connection
					.prepareCall("{call select_marked_commands()}");

			// Execute.
			statement.execute();
			ResultSet resultSet = statement.getResultSet();
			List<MarkedCommand> commands = new ArrayList<MarkedCommand>();

			while (resultSet.next()) {
				MarkedCommand command = new MarkedCommand();
				command.sceneNumber = resultSet.getInt(1);
				command.command = resultSet.getString(2);
				command.commandMark = resultSet.getInt(3);
				commands.add(command);
			}

			// Close connection.
			resultSet.close();
			statement.close();
			connection.close();

			// Return commands.
			return commands;

		} catch (SQLException exception) {
			throw new WebException(exception);
		}
	}
}