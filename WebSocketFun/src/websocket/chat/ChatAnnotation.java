/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package websocket.chat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

//import util.HTMLFilter;

import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/websocket/chat/{room}", configurator = ServletAwareConfig.class)
public class ChatAnnotation {

	private static final Logger log = Logger.getLogger(ChatAnnotation.class
			.getName());
	private EndpointConfig config;
	private static final String GUEST_PREFIX = "S";
	private static final AtomicInteger connectionIds = new AtomicInteger(0);
	private static final HashMap<String, CopyOnWriteArraySet<ChatAnnotation>> roomToConnections = new HashMap<String, CopyOnWriteArraySet<ChatAnnotation>>();
	
	private Set<String> receivedDrawMessages = new CopyOnWriteArraySet<String>();

	private final String nickname;
	private Session session;

	public ChatAnnotation() {
		nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
	}

	@OnOpen
	public void start(Session session, @PathParam("room") final String room,
			EndpointConfig config) {
		this.session = session;
		this.config = config;

		ServletContext servletContext = (ServletContext) config
				.getUserProperties().get("servletContext");
		HashMap<String, meetingRoomData> meetingRooms = (HashMap<String, meetingRoomData>) servletContext
				.getAttribute("meetingRoomList");

		session.getUserProperties().put("room", room);

		if(!roomToConnections.containsKey(room)) {
			CopyOnWriteArraySet<ChatAnnotation> connections = new CopyOnWriteArraySet<ChatAnnotation>();
			connections.add(this);
			roomToConnections.put(room,connections);
		} else {
			roomToConnections.get(room).add(this);
		}

		String message = String.format("%s %s %s %s %s %s %s %s", "{",
				"\"command\":", "\"initCanvasSlave\",", "\"room\":\"",
				room.trim(), "\",\"canvasID\":\"", nickname, "\"}");
		broadcast(session, message, nickname, true);
		firstMessageToOwn(this, session, nickname);
	}

	@OnClose
	public void end(final Session session) {
		String room = (String) session.getUserProperties().get("room");

		roomToConnections.get(room).remove(this);
		
		String message = String.format("%s %s %s %s %s %s %s %s", "{",
				"\"command\":", "\"finalizeCanvasSlave\",", "\"room\":\"",
				room.trim(), "\",\"canvasID\":\"", nickname, "\"}");
		broadcast(session, message, nickname, false);
	}

	@OnMessage
	public void incoming(final Session session, String message) {
		//
		// HttpSession httpSession = (HttpSession)
		// config.getUserProperties().get("httpSession");
		// ServletContext servletContext = httpSession.getServletContext();
		//
		String filteredMessage = message.toString();
		boolean toAllButMe = true;
		if (filteredMessage.contains("drawLinesSlave")) {
			receivedDrawMessages.add(filteredMessage);
		}
		if (filteredMessage.contains("textMessage")) {
			toAllButMe = false;
		}
		broadcast(session, filteredMessage, nickname, toAllButMe);
	}

	@OnError
	public void onError(Throwable t) throws Throwable {
		log.log(Level.WARNING, "Chat Error: " + t.toString(), t);
	}

	private static void broadcast(Session session, String msg, String nickname,
			boolean toAllButMe) {
		String room = (String) session.getUserProperties().get("room");
		CopyOnWriteArraySet<ChatAnnotation> connections = roomToConnections
				.get(room);
		
		for (ChatAnnotation client : connections) {
			
			if (toAllButMe && nickname == client.nickname) {
				continue;
			}
			sendMessage(session, msg, nickname, room, client);
		}
	}

	private static void sendMessage(Session session, String msg,
			String nickname, String room, ChatAnnotation client) {
				
		try {
			synchronized (client) {
				client.session.getBasicRemote().sendText(msg);
			}
		} catch (IOException e) {
			log.log(Level.WARNING,
					"Chat Error: Failed to send message to client", e);
			CopyOnWriteArraySet<ChatAnnotation> connections = roomToConnections
					.get(room);
			connections.remove(client);
			try {
				client.session.close();
			} catch (IOException e1) {
				// Ignore
			}
			String message = String.format("%s %s %s %s %s %s %s %s", "{",
					"\"command\":", "\"finalizeCanvasSlave\",", "\"room\":\"",
					room.trim(), "\",\"canvasID\":\"", client.nickname, "\"}");
			broadcast(session, message, nickname, false);
		}
	}

	private static void firstMessageToOwn(ChatAnnotation client, Session session, String nickname) {
		//String room = (String) client.session.getUserProperties().get("room");
		String room = (String) session.getUserProperties().get("room");
		CopyOnWriteArraySet<ChatAnnotation> connections = roomToConnections
				.get(room);
		String message = String.format("%s %s %s %s %s %s %s %s", "{",
				"\"command\":", "\"setMySlaveID\",", "\"room\":\"",
				room.trim(), "\",\"canvasID\":\"", client.nickname, "\"}");
		
		sendMessage(session, message, nickname, room, client);

		for (ChatAnnotation otherclient : connections) {
			if (otherclient.nickname != client.nickname) {
				// Send the other client's canvas to this new client
				String messageOther = String.format("%s %s %s %s %s %s %s %s",
						"{", "\"command\":", "\"initCanvasSlave\",",
						"\"room\":\"", room.trim(), "\",\"canvasID\":\"",
						otherclient.nickname, "\"}");
				
				sendMessage(session, messageOther, nickname, room, client);
				
				// Send the other client's draw commands to this new client
				for (String command : otherclient.receivedDrawMessages) {
					sendMessage(session, command, nickname, room, client);
				}
			}
		}
		
	}
	
}
