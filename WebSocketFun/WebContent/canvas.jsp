<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="canvas.css">
<title>Insert title here</title>
</head>
<body>
	<div id="canvasDiv"></div>
	<div id="messageDiv">
		<p id="message">Mouse position:</p>
	</div>
	<div id="chatArea">
		<p>
			<input type="text" placeholder="type and press enter to chat"
				id="chat" />
		</p>
		<div id="console-container">
			<div id="console"></div>
		</div>
	</div>
	<script src="utility.js"></script>
	<script src="masterCanvas.js"></script>
	<script src="slaveCanvas.js"></script>
	<script src="chat.js"></script>
	<%String roomNum = (String)request.getParameter("room");%>
	<script>
		var mySlaveID = -1;
		var myCurrentPage = 0;
		var myMeeringRoomNum ="<%=(String)request.getParameter("room")%>";
		var savedDrawCommands = Array();

		initCanvasMaster("M0");

		Chat.initialize();
	</script>
</body>
</html>