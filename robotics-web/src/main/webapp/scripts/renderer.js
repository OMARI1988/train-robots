svgNS = "http://www.w3.org/2000/svg";
xlinkNS = "http://www.w3.org/1999/xlink";
tileW = 7;
tileH = 4;
ox = 4;
oy = 84;
fw = 5.25;
fh = 3;
thinLine = 0.2;
thickLine = 0.4;

faces = [ [ "rgb(51, 133, 255)", "rgb(25, 66, 128)", "rgb(46, 119, 230)" ], // blue
[ "rgb(0, 255, 255)", "rgb(0, 150, 150)", "rgb(0, 219, 219)" ], // cyan
[ "rgb(255, 0, 0)", "rgb(150, 0, 0)", "rgb(219, 0, 0)" ], // red
[ "rgb(255, 255, 0)", "rgb(150, 150, 0)", "rgb(219, 219, 0)" ], // yellow
[ "rgb(0, 255, 41)", "rgb(0, 128, 20)", "rgb(0, 230, 37)" ], // green
[ "rgb(255, 0, 255)", "rgb(150, 0, 150)", "rgb(219, 0, 219)" ], // magenta
[ "rgb(255, 255, 255)", "rgb(150, 150, 150)", "rgb(219, 219, 219)" ], // white
[ "rgb(138, 138, 138)", "rgb(75, 75, 75)", "rgb(109, 109, 109)" ] ]; // gray

var renderer = {

	highlight : function(x, y) {

		var tx = ox + (x + y) * tileW;
		var ty = oy + (x - y) * tileH;
		var ex = 0.2;
		var ey = 0.2;

		this.fill("white", "M", tx - ex, ty, "l", tileW + ex, tileH + ey, "l",
				tileW + ex, -tileH - ey, "l", -tileW - ex, -tileH - ey);
	},

	board : function() {

		var tiles = 8;
		var boardW = tileW * tiles;
		var boardH = tileH * tiles;

		// Board sides edges.
		this.fill("white", "M", ox + boardW, oy + boardH, "l", boardW, -boardH,
				"v", 3, "l", -boardW, boardH);
		this.fill("rgb(120, 120, 120)", "M", ox, oy, "l", boardW, boardH, "v",
				3, "l", -boardW, -boardH);

		// Top face of board.
		this.fill("rgb(200, 200, 200)", "M", ox, oy, "l", boardW, boardH, "l",
				boardW, -boardH, "l", -boardW, -boardH);

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
		return oy + (x - y) * tileH - 2 * z * fh;
	},

	gripper : function(x, y, z) {
		var e = document.createElementNS(svgNS, "image");
		e.setAttributeNS(xlinkNS, "href", "images/gripper.svg");
		e.setAttribute("x", this.shapeX(x - 0.06, y + 0.18));
		e.setAttribute("y", this.shapeY(x - 0.06, y + 0.18, z) - 63.4);
		e.setAttribute("width", 8.8);
		e.setAttribute("height", 63.4);
		this.svg.appendChild(e);
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

function render(svg, instructions) {
	try {

		// Board.
		renderer.svg = svg;
		renderer.board();

		// Instructions.
		for (var i = 0; i < instructions.length; i++) {
			var t = instructions[i];
			switch (t[0]) {
			case 1:
				renderer.highlight(t[1], t[2])
				break;
			case 2:
				renderer.cube(t[1], t[2], t[3], t[4]);
				break;
			case 3:
				renderer.prism(t[1], t[2], t[3], t[4]);
				break;
			case 4:
				renderer.gripper(t[1], t[2], t[3]);
				break;
			}
		}
	} catch (exception) {
		alert(exception.message);
	}
}