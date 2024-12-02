package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve form data
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String birthdate = request.getParameter("birthdate");
        String gender = request.getParameter("gender");
        String afm = request.getParameter("afm");
        String country = request.getParameter("country");
        String address = request.getParameter("address");
        String municipality = request.getParameter("municipality");
        String prefecture = request.getParameter("prefecture");
        String job = request.getParameter("job");
        String telephone = request.getParameter("telephone");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        // Log received data for debugging
        System.out.println("Received data: username=" + username + ", email=" + email);

        // Database connection and insertion logic
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (username, email, password, firstname, lastname, birthdate, gender, afm, country, address, municipality, prefecture, job, telephone, lat, lon) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4, firstname);
                stmt.setString(5, lastname);
                stmt.setString(6, birthdate);
                stmt.setString(7, gender);
                stmt.setString(8, afm);
                stmt.setString(9, country);
                stmt.setString(10, address);
                stmt.setString(11, municipality);
                stmt.setString(12, prefecture);
                stmt.setString(13, job);
                stmt.setString(14, telephone);
                stmt.setDouble(15, Double.parseDouble(latitude));
                stmt.setDouble(16, Double.parseDouble(longitude));

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User registered successfully: " + username);

                    // Send a success response
                    response.setContentType("application/json");
                    response.getWriter().write("{\"message\":\"Registration successful for user: " + username + "\"}");
                } else {
                    // Send a failure response
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"message\":\"Registration failed. Please try again.\"}");
                }
            }
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"An error occurred: " + e.getMessage() + "\"}");
        }
    }
}
