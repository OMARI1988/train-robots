<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<style type="text/css">
#instructions {
	color: rgb(200, 200, 200);
	width: 580px;
	font-size: 12pt;
	line-height: 16pt;
}

h2 {
	color: rgb(141, 244, 50);
	font-size: 12pt;
	font-weight: blue;
	margin-top: 2.5em;
	margin-bottom: 0;
}

p.next {
	margin-top: 1.6em;
}

p.next img {
	vertical-align: bottom;
}

p.next a {
	margin-left: 0.4em;
}
</style>
<title>Train Robots - Instructions</title>
</head>
<body>
	<h1>train robots</h1>
	<p class="tagline">help teach robots to become smart as humans</p>
	<table class="menu" cellspacing="0" cellpadding="0">
		<tr>
			<td><img src="images/home.png" width="39" height="39" /></td>
			<td><a href="/">home</a>
			</td>
		</tr>
		<tr>
			<td><img src="images/next.png" width="39" height="39" /></td>
			<td><a href="signin.jsp">play</a>
			</td>
		</tr>
		<tr>
			<td><img src="images/contact.png" width="39" height="39" />
			</td>
			<td><a href="contact.jsp">contact</a>
			</td>
		</tr>
	</table>
	<div id="instructions">
		<h2>How do I play?</h2>
		<p>The first time you play you have to create a free account. The
			aim of the game is to teach a robot how to move colored blocks by
			responding to instructions in English.</p>
		<p>We want the robot to become just as smart as a human being by
			learning from you. Be natural and imagine its not a robot. Pretend
			you are telling a friend what to do.</p>
		<h2>Who gets the top score?</h2>
		<p>You will be asked to judge other player's commands to see if
			the robot made the right move. You can also teach the robot new
			commands.</p>
		<p>Players with the best scores teach the robot how to move blocks
			using clear and correct commands. These are the best commands for the
			robot to learn.</p>
	</div>
	<p class="next">
		<img src="images/next-small.png" /><a href="signin.jsp">Play</a>
	</p>
</body>
</html>