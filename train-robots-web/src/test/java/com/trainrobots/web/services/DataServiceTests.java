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

import static org.junit.Assert.assertEquals;

import javax.servlet.ServletContext;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.Test;

import com.trainrobots.web.game.User;

public class DataServiceTests {

	@Test
	public void testFoo() {

		DateTime last = new DateTime(2013, 8, 31, 01, 22, 16, 323,
				DateTimeZone.UTC);
		System.out.println(last);

		DateTime now = new DateTime(DateTimeZone.UTC);
		System.out.println(now);

		PeriodFormatter daysHoursMinutes = new PeriodFormatterBuilder()
				.appendDays().appendSuffix(" day", " days")
				.appendSeparator(", ").appendHours().appendSuffix(" hour", " hours")
				.appendSeparator(", ").appendMinutes().appendSuffix(" minute", " minutes")
				.appendSeparator(", ").appendSeconds().appendSuffix(" second", " seconds")
				.toFormatter();

		Period p = new Period(last, now);
		System.out.println(p);
		System.out.println(daysHoursMinutes.print(p));
	}

	@Test
	public void shouldGetUser() {

		ServletContext context = new MockServletContext();
		DataService dataService = new DataService();
		User user = dataService.getUser(context, "kais@kaisdukes.com");
		assertEquals("Kais", user.gameName);
		assertEquals(1, user.status);
	}
}