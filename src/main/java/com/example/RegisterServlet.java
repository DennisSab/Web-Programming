package com.example;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // Read JSON data
            BufferedReader reader = request.getReader();
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            // Parse JSON
            JSONObject jsonObject = new JSONObject(json.toString());
            String userType = jsonObject.getString("type");

            // Decide based on userType
            if ("simple".equals(userType)) {
                saveSimpleUser(jsonObject);
                out.write("{\"success\":true,\"message\":\"Simple user saved successfully.\"}");
            } else if ("volunteer".equals(userType)) {
                saveVolunteer(jsonObject);
                out.write("{\"success\":true,\"message\":\"Volunteer saved successfully.\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\":false,\"message\":\"Invalid user type.\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\":false,\"message\":\"An error occurred: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    private void saveSimpleUser(JSONObject jsonObject) {
        // Insert into `users` table
        String query = "INSERT INTO users (username, email, password, firstname, lastname, birthdate, gender, afm, country, address, municipality, prefecture, job, telephone, lat, lon) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, jsonObject.getString("username"));
            stmt.setString(2, jsonObject.getString("email"));
            stmt.setString(3, jsonObject.getString("password"));
            stmt.setString(4, jsonObject.getString("firstname"));
            stmt.setString(5, jsonObject.getString("lastname"));
            stmt.setString(6, jsonObject.getString("birthdate"));
            stmt.setString(7, jsonObject.getString("gender"));
            stmt.setString(8, jsonObject.getString("afm"));
            stmt.setString(9, jsonObject.getString("country"));
            stmt.setString(10, jsonObject.getString("address"));
            stmt.setString(11, jsonObject.getString("municipality"));
            stmt.setString(12, jsonObject.getString("prefecture"));
            stmt.setString(13, jsonObject.getString("job"));
            stmt.setString(14, jsonObject.getString("telephone"));
            stmt.setString(15, jsonObject.getString("latitude"));
            stmt.setString(16, jsonObject.getString("longitude"));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveVolunteer(JSONObject jsonObject) {
        // Insert into `volunteers` table
        String query = "INSERT INTO volunteers (username, email, password, firstname, lastname, birthdate, gender, afm, country, address, municipality, prefecture, job, telephone, lat, lon, volunteer_type, height, weight) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, jsonObject.getString("username"));
            stmt.setString(2, jsonObject.getString("email"));
            stmt.setString(3, jsonObject.getString("password"));
            stmt.setString(4, jsonObject.getString("firstname"));
            stmt.setString(5, jsonObject.getString("lastname"));
            stmt.setString(6, jsonObject.getString("birthdate"));
            stmt.setString(7, jsonObject.getString("gender"));
            stmt.setString(8, jsonObject.getString("afm"));
            stmt.setString(9, jsonObject.getString("country"));
            stmt.setString(10, jsonObject.getString("address"));
            stmt.setString(11, jsonObject.getString("municipality"));
            stmt.setString(12, jsonObject.getString("prefecture"));
            stmt.setString(13, jsonObject.getString("job"));
            stmt.setString(14, jsonObject.getString("telephone"));
            stmt.setString(15, jsonObject.getString("latitude"));
            stmt.setString(16, jsonObject.getString("longitude"));
            stmt.setString(17, jsonObject.getString("volunteer_type"));
            stmt.setString(18, jsonObject.getString("height"));
            stmt.setString(19, jsonObject.getString("weight"));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
