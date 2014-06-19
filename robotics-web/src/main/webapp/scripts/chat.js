function write(name, message) {

	// Format message.
	var p = document.createElement("p");
	p.setAttribute("class", "chat");
	p.innerHTML = "<span class='name'>" + name + "</span>: " + message;

	// Append to chat log and scroll down.
	var chatLog = document.getElementById("chatLog");
	chatLog.appendChild(p);
	chatLog.scrollTop = chatLog.scrollHeight;
}

function handleResponse(responseText) {
	try {

		// Parse the response.
		var responseData = JSON.parse(responseText);

		// Write response.
		write("robot", responseData.response);
		render(responseData.instructions);

		// UI elements.
		var inputBox = document.getElementById("inputBox");
		var sayButton = document.getElementById("sayButton");
		var loadingImage = document.getElementById("loadingImage");

		// Update UI.
		inputBox.disabled = false;
		inputBox.focus();
		sayButton.disabled = false;
		loadingImage.style.visibility = "hidden";

	} catch (exception) {
		alert(exception.message);
	}
}

function chat() {
	try {

		// UI elements.
		var inputBox = document.getElementById("inputBox");
		var sayButton = document.getElementById("sayButton");
		var loadingImage = document.getElementById("loadingImage");

		// Write message.
		var message = inputBox.value;
		write("you", message);

		// Update UI.
		inputBox.value = null;
		inputBox.disabled = true;
		sayButton.disabled = true;
		loadingImage.style.visibility = "visible";

		// Request.
		var postData = "message=" + escape(message);
		var http = new XMLHttpRequest();
		http.onreadystatechange = function() {
			if (http.readyState == 4 && http.status == 200) {
				handleResponse(http.responseText);
			}
		}
		http.open("POST", "chat", true);
		http.setRequestHeader("content-type",
				"application/x-www-form-urlencoded");
		http.setRequestHeader("content-Length", postData.length);
		http.setRequestHeader("connection", "close");
		http.send(postData);

	} catch (exception) {
		alert(exception.message);
	}
}