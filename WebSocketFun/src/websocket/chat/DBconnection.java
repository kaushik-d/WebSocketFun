package websocket.chat;

import java.sql.Connection;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class DBconnection {

	private static String dbName = "rooms";
	private static String dbURL = "jdbc:mysql://localhost:3306/"+dbName;
	private static String dbUser = "root";
	private static String dbPass = "1234";
	private static String dbTable = "roomdata";

	public DBconnection() {

	}

	public void saveToDb(meetingRoomData roomData, InputStream fileContent) {
		Connection conn = null; // connection to the database
		String message = null; // message will be sent back to client

		try {
			// connects to the database
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

			// constructs SQL statement
			String sql = "INSERT INTO " + dbTable
					+ " (isPresentation, host_name, meeting_topic, presentationURI,"
					+ " roomNumber, fileContent, create_datetime, start_datetime,"
					+ " end_datetime, IPaddress) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setBoolean(1, roomData.getisPresentation());
			statement.setString(2, roomData.getName());
			statement.setString(3, roomData.getTopic());
			statement.setString(4, roomData.getPresentationURI());
			statement.setString(5, roomData.getMeetingRoomNumber());
			statement.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
			statement.setNull(8, Types.TIMESTAMP);
			statement.setNull(9, Types.TIMESTAMP);
			statement.setString(10, roomData.getMeetingHostIP());

			if (fileContent != null) {
				// fetches input stream of the upload file for the blob column
				statement.setBlob(6, fileContent);
			}

			// sends the statement to the database server
			int row = statement.executeUpdate();
			if (row > 0) {
				message = "File uploaded and saved into database";
			}
		} catch (SQLException ex) {
			message = "ERROR: " + ex.getMessage();
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				// closes the database connection
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}

		}
	}

}
