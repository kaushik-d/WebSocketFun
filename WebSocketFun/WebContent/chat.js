/**
 * 
 */
var Chat = {};
Chat.socket = null;

Chat.connect = (function(host) {
	
	if ('WebSocket' in window) {
		Chat.socket = new WebSocket(host);
	} else if ('MozWebSocket' in window) {
		Chat.socket = new MozWebSocket(host);
	} else {
		Console.log('Error: WebSocket is not supported by this browser.');
		return;
	}

	Chat.socket.onopen = function() {
		Console.log('Info: WebSocket connection opened. Meeting Room#' + myMeeringRoomNum);
		document.getElementById('chat').onkeydown = function(event) {
			if (event.keyCode == 13) {
				Chat.sendMessage();
			}
		};
	};

	Chat.socket.onclose = function() {
		document.getElementById('chat').onkeydown = null;
		Console.log('Info: WebSocket closed.');
	};

	Chat.socket.onmessage = function(message) {
		// Console.log(message.data);
		processCommands(message.data);
	};
});

Chat.initialize = function() {
	if (window.location.protocol == 'http:') {
		Chat.connect('ws://' + window.location.host
				+ '/WebSocketFun/websocket/chat/'+myMeeringRoomNum);
	} else {
		Chat.connect('wss://' + window.location.host
				+ '/WebSocketFun/websocket/chat/'+myMeeringRoomNum);
	}
};

Chat.sendMessage = (function() {
	var message = document.getElementById('chat').value;
	if (message != '') {
		Chat.socket.send(JSON.stringify({
			command : "textMessage",
			text : message
		}));
		document.getElementById('chat').value = '';
	}
});

var Console = {};

Console.log = (function(message) {
	var console = document.getElementById('console');
	var p = document.createElement('p');
	p.style.wordWrap = 'break-word';
	p.innerHTML = message;
	console.appendChild(p);
	while (console.childNodes.length > 25) {
		console.removeChild(console.firstChild);
	}
	console.scrollTop = console.scrollHeight;
});

processCommands = function(message) {
	var mes = JSON.parse(message);
	if (mes.command === "initCanvasSlave") {
		initCanvasSlave(mes.canvasID.trim());
	} else if (mes.command === "drawLinesSlave") {
		drawLinesSlave(mes.slaveID, mes.type, mes.x, mes.y);
		saveDrawLinesSlave(mes);
	} else if (mes.command === "setMySlaveID") {
		setMySlaveID(mes);
	} else if (mes.command === "finalizeCanvasSlave") {
		finalizeCanvasSlave(mes.canvasID.trim());
	} else if (mes.command === "textMessage") {
		Console.log(mes.text);
	} else if (mes.command === "changePage") {
		changePage(mes.pageNum);
	}
}

setMySlaveID = function(mes){
	canvasName = mes.canvasID.trim()
	mySlaveID = parseInt(canvasName.substring(1, canvasName.length));
	isPresentation = mes.isPresentation;
	presentationURI = mes.presentationURI;
	
	isPresentation = true;
	presentationURI = "./pdfExample/TestPage.pdf";
	
	if(!isPresentation) {
		currentPage = 0;
		//savedDrawCommands["0"] = new Array(); 
	}
	else {
		currentPage = 0;
		initCanvasPresentation();
	}
}

saveDrawLinesSlave = function(mes){
	if( typeof savedDrawCommands[currentPage.toString()] == 'undefined' ){
		savedDrawCommands[currentPage.toString()] = new Array();
	}
	savedDrawCommands[currentPage.toString()].push(mes);
}