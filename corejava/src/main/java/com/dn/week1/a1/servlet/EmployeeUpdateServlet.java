package com.dn.week1.a1.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeUpdateServlet")
public class EmployeeUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String message = null;

        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/empl", "root", "Jambalakadijm@43");

            // Get employee details from request
            int eid = Integer.parseInt(request.getParameter("eid"));
            String fname = request.getParameter("fname");
            String lname = request.getParameter("lname");
            int dept = Integer.parseInt(request.getParameter("dept"));

            // SQL query to update employee
            String sql = "UPDATE Employee SET FirstName = ?, LastName = ?, DepartmentID = ? WHERE EmployeeID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setInt(3, dept);
            stmt.setInt(4, eid);

            // Execute update
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                message = "Employee updated successfully";
            } else {
                message = "No employee found with the given ID";
            }

        } catch (ClassNotFoundException | SQLException e) {
            message = "Error updating employee: " + e.getMessage();
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Set message attribute and forward to JSP
        request.setAttribute("message", message);
        request.getRequestDispatcher("updateEmployee.jsp").forward(request, response);
    }
}