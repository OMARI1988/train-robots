<!DOCTYPE html>
<%@page import="static com.trainrobots.web.Application.get"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Train Robots</title>
<script type="text/javascript" src="scripts/renderer.js"></script>
</head>
<body style="background: rgb(33, 33, 33)">
	<br />
	<br />
	<p style="color: white">
		Loaded
		<%=get(application).treebank().commands().count()%>
		commands.
	</p>
	<br />
	<br />
	<svg id="scene" xmlns="http://www.w3.org/2000/svg"
		xmlns:xlink="http://www.w3.org/1999/xlink" width="400" height="400"
		viewBox="0 0 120 120">
		<image xlink:href="images/robot.svg" x="-8" y="5" width="70"
			height="70" />
	</svg>
</body>
<script type="text/javascript">
	var instructions = [ [ 1, 4, 6 ], [ 2, 0, 7, 0, 4 ], [ 2, 1, 7, 0, 4 ],
			[ 2, 4, 7, 0, 6 ], [ 2, 4, 7, 1, 6 ], [ 2, 5, 7, 0, 6 ],
			[ 2, 5, 7, 1, 6 ], [ 2, 0, 6, 0, 4 ], [ 2, 1, 6, 0, 4 ],
			[ 2, 4, 6, 0, 6 ], [ 2, 4, 6, 1, 6 ], [ 2, 5, 6, 0, 6 ],
			[ 2, 5, 6, 1, 6 ], [ 2, 7, 5, 0, 7 ], [ 2, 7, 5, 1, 2 ],
			[ 2, 7, 5, 2, 7 ], [ 2, 7, 5, 3, 2 ], [ 2, 7, 5, 4, 7 ],
			[ 2, 7, 5, 5, 2 ], [ 3, 7, 5, 6, 7 ], [ 2, 4, 4, 0, 0 ],
			[ 2, 5, 4, 0, 0 ], [ 2, 0, 3, 0, 3 ], [ 2, 0, 3, 1, 2 ],
			[ 2, 1, 3, 0, 3 ], [ 2, 1, 3, 1, 2 ], [ 2, 2, 3, 0, 2 ],
			[ 2, 2, 3, 1, 2 ], [ 2, 3, 3, 0, 2 ], [ 2, 3, 3, 1, 2 ],
			[ 2, 4, 3, 0, 0 ], [ 2, 4, 3, 1, 2 ], [ 2, 5, 3, 0, 0 ],
			[ 2, 5, 3, 1, 2 ], [ 2, 0, 2, 0, 3 ], [ 2, 0, 2, 1, 2 ],
			[ 2, 1, 2, 0, 3 ], [ 2, 1, 2, 1, 2 ], [ 2, 2, 2, 0, 2 ],
			[ 2, 2, 2, 1, 2 ], [ 2, 3, 2, 0, 2 ], [ 2, 3, 2, 1, 2 ],
			[ 2, 4, 2, 0, 0 ], [ 2, 4, 2, 1, 2 ], [ 2, 5, 2, 0, 0 ],
			[ 2, 5, 2, 1, 2 ], [ 2, 0, 1, 0, 3 ], [ 2, 1, 1, 0, 3 ],
			[ 2, 0, 0, 0, 3 ], [ 3, 0, 0, 1, 2 ], [ 2, 1, 0, 0, 3 ],
			[ 2, 4, 6, 5, 3 ], [ 4, 4, 6, 5 ] ];

	render(document.getElementById('scene'), instructions);
</script>
</html>