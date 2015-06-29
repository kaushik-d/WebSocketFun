package websocket.chat;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static HashMap<String,meetingRoomData> meetingRooms;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String json = request.getParameter("data");
		
		Gson gson = new Gson();
		
		meetingRoomData MeetingRoomData = gson.fromJson(json, meetingRoomData.class);
		
		if(MeetingRoomData.getFunction().contains("createRoom")) {
			String roomNumber = createMeetingRoom();
			MeetingRoomData.setMeetingRoomNumber(roomNumber);
			meetingRooms.put(roomNumber, MeetingRoomData);
			response.setContentType("application/json");
			response.getWriter().write(gson.toJson(MeetingRoomData,meetingRoomData.class));
		}
		
		if(MeetingRoomData.getFunction().contains("joinMeeting")) {
			if(meetingRooms.containsKey(MeetingRoomData.getMeetingRoomNumber())){
				//request.getRequestDispatcher("/canvas.jsp").forward(request, response);
				response.getWriter().write("/WebSocketFun/canvas.jsp?room="+MeetingRoomData.getMeetingRoomNumber());
			} 
			else {
				MeetingRoomData.setFunction("MeetingRoomNotFound");
				response.getWriter().write(gson.toJson(MeetingRoomData,meetingRoomData.class));
			}
		}
		
		
	}
	
	@Override
	public void init(){
		meetingRooms = new HashMap<String,meetingRoomData>();
		getServletConfig().getServletContext().setAttribute("meetingRoomList", meetingRooms);
	}
	
	public String createMeetingRoom(){
		
		int meetingRoomNumberlength = 4;
		RandomString randomString = new RandomString(meetingRoomNumberlength);
		return randomString.nextString(); 
	}

}
