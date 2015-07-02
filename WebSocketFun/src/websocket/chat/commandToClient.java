package websocket.chat;

public class commandToClient {
	
	private String command;
	private String room;
	private String canvasID;
	private boolean isPresentation = false;
	private String presentationURI = "NotAPresentation";
	
	public commandToClient(){
		
		
	}

	public commandToClient(String command, String room, String nickname,
			boolean isPresentation, String presentationURI){
		this.command = command;
		this.room = room;
		this.canvasID = nickname;
		this.isPresentation = isPresentation;
		this.presentationURI = presentationURI;
	}
}
