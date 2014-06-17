svgNS = "http://www.w3.org/2000/svg";
tileW = 7;
tileH = 4;
ox = 4;
oy = 72;
fw = 5.25;
fh = 3;
thinLine = 0.2;
thickLine = 0.4;

faces = [ [ "rgb(51, 133, 255)", "rgb(25, 66, 128)", "rgb(46, 119, 230)" ], // blue
[ "cyan", "cyan", "cyan" ], // cyan
[ "rgb(255, 0, 0)", "rgb(150, 0, 0)", "rgb(219, 0, 0)" ], // red
[ "rgb(255, 255, 0)", "rgb(150, 150, 0)", "rgb(219, 219, 0)" ], // yellow
[ "rgb(0, 255, 41)", "rgb(0, 128, 20)", "rgb(0, 230, 37)" ], // green
[ "magenta", "magenta", "magenta" ], // magenta
[ "rgb(255, 255, 255)", "rgb(150, 150, 150)", "rgb(219, 219, 219)" ], // white
[ "rgb(138, 138, 138)", "rgb(75, 75, 75)", "rgb(109, 109, 109)" ] ]; // gray

var renderer = {

	initiate : function(svg) {
		this.svg = svg;
		this.svg.setAttribute("viewBox", "0, 0, 120, 120");
	},

	board : function() {

		var tiles = 8;
		var boardW = tileW * tiles;
		var boardH = tileH * tiles;

		// Board sides edges.
		var colors = faces[6];
		this.fill(colors[0], "M", ox + boardW, oy + boardH, "l", boardW,
				-boardH, "v", 3, "l", -boardW, boardH);
		this.fill(colors[1], "M", ox, oy, "l", boardW, boardH, "v", 3, "l",
				-boardW, -boardH);

		// Top face of board.
		this.fill(colors[2], "M", ox, oy, "l", boardW, boardH, "l", boardW,
				-boardH, "l", -boardW, -boardH);

		// Grid lines.
		var dx = 0;
		var dy = 0;
		for (var i = 0; i <= tiles; i++) {
			this.line(thinLine, "black", ox + dx, oy - dy, boardW, boardH);
			this.line(thinLine, "black", ox + dx, oy + dy, boardW, -boardH);
			dx += tileW;
			dy += tileH;
		}

		// Thin lines.
		this.path(thinLine, "black", "M", ox, oy, "l", boardW, boardH, "l",
				boardW, -boardH, "m", -boardW, boardH, "v", 3);

		// Thick lines.
		this.path(thickLine, "black", "M", ox, oy, "l", boardW, -boardH, "l",
				boardW, boardH, "v", 3, "l", -boardW, boardH, "l", -boardW,
				-boardH, "Z");
	},

	shapeX : function(x, y) {
		return ox + (x + y) * tileW + 1.75;
	},

	shapeY : function(x, y, z) {
		return oy + (x - y) * tileH - (z * 1.95) * fh;
	},

	cube : function(x, y, z, color) {

		// Color.
		var colors = faces[color];

		// Position.
		var sx = this.shapeX(x, y);
		var sy = this.shapeY(x, y, z);

		// X-direction.
		this.fill(colors[0], "M", sx + fw, sy + fh, "v", -fh * 2, "l", fw, -fh,
				"v", fh * 2);

		// Y-direction.
		this.fill(colors[1], "M", sx + fw, sy + fh, "v", -fh * 2, "l", -fw,
				-fh, "v", fh * 2);

		// Z-direction.
		this.fill(colors[2], "M", sx, sy - fh * 2, "l", fw, fh, "l", fw, -fh,
				"l", -fw, -fh);

		// Thin lines.
		this.path(thinLine, "black", "M", sx, sy - 2 * fh, "l", fw, fh, "l",
				fw, -fh, "m", -fw, fh, "v", fh * 2);

		// Thick lines.
		this.path(thickLine, "black", "M", sx, sy, "v", -2 * fh, "l", fw, -fh,
				"l", fw, fh, "v", 2 * fh, "l", -fw, fh, "Z");
	},

	prism : function(x, y, z, color) {

		// Color.
		var colors = faces[color];

		// Position.
		var sx = this.shapeX(x, y);
		var sy = this.shapeY(x, y, z);

		// X-direction.
		this.fill(colors[0], "M", sx + fw, sy + fh, "v", -fh * 3, "l", fw,
				fh * 2);

		// Y-direction.
		this.fill(colors[2], "M", sx + fw, sy + fh, "v", -fh * 3, "l", -fw,
				fh * 2);

		// Thin line.
		this.line(thinLine, "black", sx + fw, sy - 2 * fh, 0, 3 * fh);

		// Thick lines.
		this.path(thickLine, "black", "M", sx, sy, "l", fw, -2 * fh, "l", fw,
				fh * 2, "l", -fw, fh, "Z");
	},

	fill : function() {
		this.element("path", {
			fill : arguments[0],
			d : [].splice.call(arguments, 1).join(" ")
		});
	},

	path : function() {
		this.element("path", {
			"stroke-width" : arguments[0],
			"stroke-linejoin" : "round",
			stroke : arguments[1],
			fill : "none",
			d : [].splice.call(arguments, 2).join(" "),
		});
	},

	line : function(strokeWidth, stroke, x, y, w, h) {
		this.element("line", {
			x1 : x,
			y1 : y,
			x2 : x + w,
			y2 : y + h,
			"stroke-width" : strokeWidth,
			stroke : stroke
		});
	},

	element : function(name, attrs) {
		var e = document.createElementNS(svgNS, name);
		for ( var attr in attrs) {
			e.setAttribute(attr, attrs[attr]);
		}
		this.svg.appendChild(e);
	}
};

function render(svg) {
	try {

		// Board.
		renderer.initiate(svg);
		renderer.board();

		// Shapes.
		renderer.cube(0, 7, 0, 4);
		renderer.cube(1, 7, 0, 4);
		renderer.cube(4, 7, 0, 6);
		renderer.cube(4, 7, 1, 6);
		renderer.cube(5, 7, 0, 6);
		renderer.cube(5, 7, 1, 6);
		renderer.cube(0, 6, 0, 4);
		renderer.cube(1, 6, 0, 4);
		renderer.cube(4, 6, 0, 6);
		renderer.cube(4, 6, 1, 6);
		renderer.cube(5, 6, 0, 6);
		renderer.cube(5, 6, 1, 6);
		renderer.cube(7, 5, 0, 7);
		renderer.cube(7, 5, 1, 2);
		renderer.cube(7, 5, 2, 7);
		renderer.cube(7, 5, 3, 2);
		renderer.cube(7, 5, 4, 7);
		renderer.cube(7, 5, 5, 2);
		renderer.prism(7, 5, 6, 7);
		renderer.cube(4, 4, 0, 0);
		renderer.cube(5, 4, 0, 0);
		renderer.cube(0, 3, 0, 3);
		renderer.cube(0, 3, 1, 2);
		renderer.cube(1, 3, 0, 3);
		renderer.cube(1, 3, 1, 2);
		renderer.cube(2, 3, 0, 2);
		renderer.cube(2, 3, 1, 2);
		renderer.cube(3, 3, 0, 2);
		renderer.cube(3, 3, 1, 2);
		renderer.cube(4, 3, 0, 0);
		renderer.cube(4, 3, 1, 2);
		renderer.cube(5, 3, 0, 0);
		renderer.cube(5, 3, 1, 2);
		renderer.cube(0, 2, 0, 3);
		renderer.cube(0, 2, 1, 2);
		renderer.cube(1, 2, 0, 3);
		renderer.cube(1, 2, 1, 2);
		renderer.cube(2, 2, 0, 2);
		renderer.cube(2, 2, 1, 2);
		renderer.cube(3, 2, 0, 2);
		renderer.cube(3, 2, 1, 2);
		renderer.cube(4, 2, 0, 0);
		renderer.cube(4, 2, 1, 2);
		renderer.cube(5, 2, 0, 0);
		renderer.cube(5, 2, 1, 2);
		renderer.cube(0, 1, 0, 3);
		renderer.cube(1, 1, 0, 3);
		renderer.cube(0, 0, 0, 3);
		renderer.prism(0, 0, 1, 2);
		renderer.cube(1, 0, 0, 3);

	} catch (exception) {
		alert(exception.message);
	}
}