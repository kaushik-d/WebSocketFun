/**
 * 
 */
function writeMessage(message) {
	document.getElementById('message').innerHTML = message;
}

function getMousePos(canvas, evt) {
	var rect = canvas.getBoundingClientRect();
	return {
		x : (evt.clientX - rect.left)/CURRENTCANVASWIDTH,
		y : (evt.clientY - rect.top)/CURRENTCANVASHEIGHT
	};
	// offset = getOffset( canvas );
	// return {
	// x : evt.pageX - offset.x,
	// y : evt.pageY - offset.y
	// };
}

function updateMousePosition(evt) {
	var mousePos = getMousePos(this, evt);
	var message = 'Mouse position: ' + this.id + ':' + mousePos.x + ','
			+ mousePos.y;
	writeMessage(message);
}

function getOffset(elem) {
	var offsetLeft = 0;
	var offsetTop = 0;
	do {
		if (!isNaN(elem.offsetLeft)) {
			offsetLeft += elem.offsetLeft;
		}
		if (!isNaN(elem.offsetRight)) {
			offsetTop += elem.offsetTop;
		}
	} while (elem = elem.offsetParent);
	return {
		x : offsetTop,
		y : offsetLeft
	};
}

function addFullScreenListners() {
	document.addEventListener('webkitfullscreenchange', function(e) {
		restore();
	}, false);

	document.addEventListener('mozfullscreenchange', function(e) {
		restore();
	}, false);

	document.addEventListener('fullscreenchange', function(e) {
		restore();
	}, false);
}

function goFullScreen() {
	var parentDiv = document.getElementById("canvasDiv");
	if (parentDiv.requestFullScreen) {
		parentDiv.requestFullscreen();
	} else if (parentDiv.webkitRequestFullScreen) {
		parentDiv.webkitRequestFullscreen();
	} else if (parentDiv.mozRequestFullScreen) {
		parentDiv.mozRequestFullScreen();
	}

}

function restore() {

	if (!window.screenTop && !window.screenY) {
		console.log('not fullscreen');
		CURRENTCANVASWIDTH = screen.width;
		CURRENTCANVASHEIGHT = screen.height;
		$("#canvasDiv").attr({
			"width" : CURRENTCANVASWIDTH,
			"height" : CURRENTCANVASHEIGHT
		});
		$("#canvasDiv").css({
			"width" : CURRENTCANVASWIDTH,
			"height" : CURRENTCANVASHEIGHT
		});
		$("canvas").attr({
			"width" : CURRENTCANVASWIDTH,
			"height" : CURRENTCANVASHEIGHT
		});
		$("#menuDiv").css({
			"top" : CURRENTCANVASHEIGHT - MENUBARHEIGHT,
		// "buttom":CURRENTCANVASHEIGHT-MENUBARHEIGHT
		});

		redrawCurrentPageContents();

	} else {
		CURRENTCANVASWIDTH = INITICANVASWIDTH;
		CURRENTCANVASHEIGHT = INITICANVASHEIGHT;
		$("#canvasDiv").attr({
			"width" : CURRENTCANVASWIDTH,
			"height" : CURRENTCANVASHEIGHT
		});
		$("#canvasDiv").css({
			"width" : CURRENTCANVASWIDTH,
			"height" : CURRENTCANVASHEIGHT
		});
		$("canvas").attr({
			"width" : CURRENTCANVASWIDTH,
			"height" : CURRENTCANVASHEIGHT
		});
		$("#menuDiv").css({
			"top" : CURRENTCANVASHEIGHT - MENUBARHEIGHT,
		// "buttom":CURRENTCANVASHEIGHT-MENUBARHEIGHT
		});

		redrawCurrentPageContents();

		console.log('fullscreen');
	}
}

redrawCurrentPageContents = function() {
	var arrayLength = contextListMaster.length;
	for (var i = 0; i < arrayLength; i++) {
		if (contextListMaster[i] != null && contextListMaster[i] != undefined
				&& typeof contextListMaster[i] != 'undefined') {
			contextListMaster[i].clearRect(0, 0, CURRENTCANVASWIDTH,
					CURRENTCANVASHEIGHT);
		}
	}
	arrayLength = contextListSlave.length;
	for (var i = 0; i < arrayLength; i++) {
		if (contextListSlave[i] != null && contextListSlave[i] != undefined
				&& typeof contextListSlave[i] != 'undefined') {
			contextListSlave[i].clearRect(0, 0, CURRENTCANVASWIDTH,
					CURRENTCANVASHEIGHT);
		}
	}
	
	if (typeof savedDrawCommands[currentPage.toString()] != 'undefined') {

		saveDrawCommandsForCurrentPage = savedDrawCommands[currentPage
				.toString()];

		for (i = 0; i < saveDrawCommandsForCurrentPage.length; i++) {
			
			var drawCommand = saveDrawCommandsForCurrentPage[i];
			drawLinesSlave(drawCommand.slaveID, drawCommand.type,
					drawCommand.x, drawCommand.y);
		}

	}

	for (i = 0; i < savedDrawCommandsMaster.length; i++) {
		var drawCommand = savedDrawCommandsMaster[i];
		drawLinesMaster(0, drawCommand.type, drawCommand.x, drawCommand.y);
	}
	
	renderPresentationPage(presentationPdf);

}