package com.example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ExampleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");

        try (Connection conn = DBConnection.getConnection()) {
            // Query the database
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users"); // Replace with your table name

            // Display results
            response.getWriter().println("<h1>Users:</h1>");
            while (rs.next()) {
                String username = rs.getString("username"); // Replace with your column name
                response.getWriter().println("<p>" + username + "</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error connecting to the database.</p>");
        }
    }
}
