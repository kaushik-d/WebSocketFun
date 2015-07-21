/**
 * 
 */
var canvasListSlave = new Array();
var contextListSlave = new Array();
var lineStartedListSlave = new Array();
var oldxListSlave = new Array();
var oldyListSlave = new Array();

function drawLinesSlave(ID, type, x, y) {

	if (contextListSlave[ID] != null
			&& typeof contextListSlave[ID] != 'undefined'
			&& contextListSlave[ID] != undefined) {

		x = x * CURRENTCANVASWIDTH;
		y = y * CURRENTCANVASHEIGHT;

		contextListSlave[ID].beginPath();
		if (type === "lineStart") {
			oldxListSlave[ID] = x;
			oldyListSlave[ID] = y;
			lineStartedListSlave[ID] = true;
		} else if (type === "lineUpdate" && lineStartedListSlave[ID]) {
			contextListSlave[ID].moveTo(oldxListSlave[ID], oldyListSlave[ID]);
			contextListSlave[ID].lineTo(x, y);
			oldxListSlave[ID] = x;
			oldyListSlave[ID] = y;
			contextListSlave[ID].strokeStyle = '#ff0000';
			contextListSlave[ID].stroke();
		} else {
			lineStartedListSlave[ID] = false;
		}

	}
}

function initCanvasSlave(canvasName) {
	var ID = parseInt(canvasName.substring(1, canvasName.length));
	var canvasDiv = window.document.getElementById('canvasDiv');
	canvasListSlave[ID] = document.createElement('canvas');
	canvasListSlave[ID].id = canvasName;
	canvasListSlave[ID].width = CURRENTCANVASWIDTH;
	canvasListSlave[ID].height = CURRENTCANVASHEIGHT;
	canvasListSlave[ID].left = 0;
	canvasListSlave[ID].right = 0;
	canvasListSlave[ID].style.zIndex = -ID - 1;
	canvasDiv.appendChild(canvasListSlave[ID]);
	contextListSlave[ID] = canvasListSlave[ID].getContext('2d');
	lineStartedListSlave[ID] = false;
	oldxListSlave[ID] = 0;
	oldyListSlave[ID] = 0;
}

function finalizeCanvasSlave(canvasName) {
	var ID = parseInt(canvasName.substring(1, canvasName.length));
	window.document.getElementById('canvasDiv')
			.removeChild(canvasListSlave[ID]);
	canvasListSlave[ID] = null;
	contextListSlave[ID] = null;
	lineStartedListSlave[ID] = false;
	oldxListSlave[ID] = 0;
	oldyListSlave[ID] = 0;
}