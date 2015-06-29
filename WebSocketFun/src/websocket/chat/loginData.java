package websocket.chat;

public class loginData {
	
	private String functName;
	private boolean isPresentation = false;
	String name = "Unknown";
	String topic = "Unknown";
	String presentationURI = "NotAPresentation";
	
	public loginData(){
		
	}
	
	public String getFunction(){
		return functName;
	}
	
	public String getNme(){
		return name;
	}
	
	public String getTopic(){
		return topic;
	}
	
	public boolean getisPresentation(){
		return isPresentation;
	}
	
	public String getPresentationURI(){
		if(isPresentation) return  presentationURI;
		return null;
	}
}
 