package websocket.chat;

import java.sql.Connection;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBconnection {

	private String dbURL = "jdbc:mysql://localhost:3306/AppDB";
	private String dbUser = "root";
	private String dbPass = "secret";

	public DBconnection() {

	}

	public void saveToDb() {
		Connection conn = null; // connection to the database
		String message = null; // message will be sent back to client

		try {
			// connects to the database
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

			// constructs SQL statement
			String sql = "INSERT INTO contacts (first_name, last_name, photo) values (?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, firstName);
			statement.setString(2, lastName);

			if (inputStream != null) {
				// fetches input stream of the upload file for the blob column
				statement.setBlob(3, inputStream);
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
			// sets the message in request scope
			request.setAttribute("Message", message);

			// forwards to the message page
			getServletContext().getRequestDispatcher("/Message.jsp").forward(
					request, response);
		}
	}

}
