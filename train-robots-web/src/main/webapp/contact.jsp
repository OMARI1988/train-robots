<%@page pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<style type="text/css">
#information {
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
<title>Train Robots - Contact</title>
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
			<td><a href="/game">play</a>
			</td>
		</tr>
		<tr>
			<td><img src="images/info.png" width="39" height="39" />
			</td>
			<td><a href="instructions.jsp">instructions</a>
			</td>
		</tr>
	</table>
	<div id="information">
		<h2>Feedback</h2>
		<p>
			Train Robots is an artificial intelligence research project organised
			by <a href="http://www.kaisdukes.com">Kais Dukes</a>. For questions
			or feedback, please email:
		</p>
		<p>
			<a href="mailto:kais@kaisdukes.com">kais@kaisdukes.com</a>
		</p>
		<h2>Project Information</h2>
		<p>Robots are not yet as smart as humans when trying to understand
			natural language.</p>
		<p>A big challenge is that computational linguistics and natural
			language processing systems require large amounts of training data.
			This project aims to collect data for machine learning by building an
			annotated corpus of robot commands.</p>
		<p>Train Robots is a game with a purpose. By playing the game
			well, you are helping us build a large annotated dataset that robots
			can learn from.</p>
		<p>With your help, we can teach robots to become much smarter.</p>
	</div>
	<p class="next">
		<img src="images/next-small.png" /><a href="signin.jsp">Play</a>
	</p>
</body>
</html>