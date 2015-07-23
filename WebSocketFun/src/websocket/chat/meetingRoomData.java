package websocket.chat;

import java.io.InputStream;

public class meetingRoomData {

	private String functName;
	private boolean isPresentation = false;
	private String name = "Unknown";
	private String topic = "Unknown";
	private String presentationURI = "NotAPresentation";
	private String roomNumber;
	private String meetingHostIP;

	public meetingRoomData(String functName, boolean isPresentation,
			String name, String topic, String presentationURI,
			String roomNumber, String meetingHostIP) {

		this.functName = functName;
		this.isPresentation = isPresentation;
		this.name = name;
		this.topic = topic;
		this.presentationURI = presentationURI;
		this.roomNumber = roomNumber;
		this.meetingHostIP = meetingHostIP;

	}

	public meetingRoomData() {

	}

	public meetingRoomData(loginData logindata, String roomNumber) {
		name = logindata.getNme();
		topic = logindata.getTopic();
		presentationURI = logindata.getPresentationURI();
		isPresentation = logindata.getisPresentation();
		this.roomNumber = roomNumber;
	}

	public void setMeetingRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getFunction() {
		return functName;
	}

	public void setFunction(String functName) {
		this.functName = functName;
	}

	public String getName() {
		return name;
	}

	public String getTopic() {
		return topic;
	}

	public String getMeetingRoomNumber() {
		return this.roomNumber;
	}

	public boolean getisPresentation() {
		return isPresentation;
	}

	public String getPresentationURI() {
		if (isPresentation)
			return presentationURI;
		return "NotAPresentation";
	}

	public String getMeetingHostIP() {
		return this.meetingHostIP;
	}
}
