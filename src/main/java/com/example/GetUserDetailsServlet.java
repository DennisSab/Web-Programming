package com.example;

import org.json.JSONObject; // For creating JSON responses
import javax.servlet.ServletException; // For handling servlets
import javax.servlet.annotation.WebServlet; // For mapping the servlet to a URL
import javax.servlet.http.HttpServlet; // Base class for HTTP servlets
import javax.servlet.http.HttpServletRequest; // For handling HTTP requests
import javax.servlet.http.HttpServletResponse; // For handling HTTP responses
import javax.servlet.http.HttpSession; // For session management
import java.io.IOException; // For input/output operations
import java.io.PrintWriter; // For writing responses
import java.sql.Connection; // For managing database connections
import java.sql.PreparedStatement; // For executing SQL queries
import java.sql.ResultSet; // For processing SQL query results
import java.sql.SQLException; // For handling SQL exceptions


@WebServlet("/getUserDetails")
public class GetUserDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        HttpSession session = request.getSession(false); // Get the session, but don't create a new one
        PrintWriter out = response.getWriter();

        if (session == null) {
            System.out.println("No active session.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\":false,\"message\":\"No active session.\"}");
            return;
        }

        String username = (String) session.getAttribute("username");
        if (username == null) {
            System.out.println("Username is not set in the session.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\":false,\"message\":\"User is not logged in.\"}");
            return;
        }

        System.out.println("Retrieved username: " + username);

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                System.out.println("Executing query: SELECT * FROM users WHERE username = '" + username + "'");

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Return all user details as JSON
                        JSONObject userDetails = new JSONObject();
                        userDetails.put("success", true);
                        userDetails.put("username", rs.getString("username"));
                        userDetails.put("email", rs.getString("email"));
                        userDetails.put("firstname", rs.getString("firstname"));
                        userDetails.put("lastname", rs.getString("lastname"));
                        userDetails.put("birthdate", rs.getString("birthdate"));
                        userDetails.put("gender", rs.getString("gender"));
                        userDetails.put("afm", rs.getString("afm"));
                        userDetails.put("country", rs.getString("country"));
                        userDetails.put("address", rs.getString("address"));
                        userDetails.put("municipality", rs.getString("municipality"));
                        userDetails.put("prefecture", rs.getString("prefecture"));
                        userDetails.put("job", rs.getString("job"));
                        userDetails.put("telephone", rs.getString("telephone"));
                        userDetails.put("latitude", rs.getString("lat"));
                        userDetails.put("longitude", rs.getString("lon"));

                        out.write(userDetails.toString());
                        return;
                    } else {
                        System.out.println("No user found with username: " + username);
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.write("{\"success\":false,\"message\":\"User not found.\"}");
                        return;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\":false,\"message\":\"Database error occurred.\"}");
            return;
        }
    }
}
