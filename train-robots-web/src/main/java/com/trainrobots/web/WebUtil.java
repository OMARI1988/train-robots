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

package com.trainrobots.web;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class WebUtil {

	private static final String EMAIL_REGEX = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

	private static final PeriodFormatter PERIOD_FORMATTER = new PeriodFormatterBuilder()
			.appendDays().appendSuffix(" day", " days").appendSeparator(", ")
			.appendHours().appendSuffix(" hour", " hours")
			.appendSeparator(", ").appendMinutes()
			.appendSuffix(" minute", " minutes").appendSeparator(", ")
			.appendSeconds().appendSuffix(" second", " seconds").toFormatter();

	private WebUtil() {
	}

	public static String formatElapsedTime(DateTime timeUtc) {
		DateTime now = new DateTime(DateTimeZone.UTC);
		return PERIOD_FORMATTER.print(new Period(timeUtc, now));
	}

	public static boolean isValidEmail(String email) {
		return email != null && email.length() > 0 && email.length() <= 32
				&& email.matches(EMAIL_REGEX);
	}

	public static boolean isValidPassword(String password) {
		if (password == null) {
			return false;
		}
		int size = password.length();
		if (size == 0 || size > 32) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			char ch = password.charAt(i);
			if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
					|| (ch >= '0' && ch <= '9') || ch == '_' || ch == '-'
					|| ch == '*' || ch == '!' || ch == '$' || ch == '#'
					|| ch == '+' || ch == '@' || ch == '|') {
				// valid
			} else {
				return false;
			}
		}
		return true;
	}
}