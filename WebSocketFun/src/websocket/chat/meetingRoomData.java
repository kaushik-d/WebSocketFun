package websocket.chat;

public class meetingRoomData {
	
	private String functName;
	private boolean isPresentation = false;
	String name = "Unknown";
	String topic = "Unknown";
	String presentationURI = "NotAPresentation";
	String roomNumber;
	
	public meetingRoomData(){
		
	}
	
    public meetingRoomData(loginData logindata, String roomNumber){
		name = logindata.getNme();
		topic = logindata.getTopic();
		presentationURI = logindata.getPresentationURI();
		isPresentation = logindata.getisPresentation();
		this.roomNumber = roomNumber;
	}
    
    public void setMeetingRoomNumber(String roomNumber){
		this.roomNumber = roomNumber;
	}
	
	public String getFunction(){
		return functName;
	}
	
	public void setFunction(String functName){
		this.functName = functName;
	}
	
	public String getName(){
		return name;
	}
	
	public String getTopic(){
		return topic;
	}
	
	public String getMeetingRoomNumber(){
		return this.roomNumber;
	}
	
	public boolean getisPresentation(){
		return isPresentation;
	}
	
	public String getPresentationURI(){
		if(isPresentation) return  presentationURI;
		return null;
	}
}
