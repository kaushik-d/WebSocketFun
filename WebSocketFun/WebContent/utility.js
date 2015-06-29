/**
 * 
 */
function writeMessage(message) {
	document.getElementById('message').innerHTML = message;
}

function getMousePos(canvas, evt) {
	var rect = canvas.getBoundingClientRect();
	return {
		x : evt.clientX - rect.left,
		y : evt.clientY - rect.top
	};
	//offset = getOffset( canvas );
	//return {
	//	x : evt.pageX - offset.x,
	//	y : evt.pageY - offset.y
	//};
}

function updateMousePosition(evt) {
	var mousePos = getMousePos(this, evt);
	var message = 'Mouse position: ' + this.id + ':' + mousePos.x + ','
			+ mousePos.y;
	writeMessage(message);
}

function getOffset( elem )
{
    var offsetLeft = 0;
    var offsetTop = 0;
    do {
      if ( !isNaN( elem.offsetLeft ) )
      {
          offsetLeft += elem.offsetLeft;
      }
      if ( !isNaN( elem.offsetRight ) )
      {
    	  offsetTop += elem.offsetTop;
      }
    } while( elem = elem.offsetParent );
    return {x: offsetTop, y: offsetLeft};
}