package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

@WebServlet("/bookDate")
public class bookDateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // not needed
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getCookies() == null) {
            response.sendRedirect("/roomy/index.html");
            return;
        }
        Cookie[] cookies = request.getCookies();
        String officeID = "";
        String username = "";

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username"))
                username = cookie.getValue();

            if (cookie.getName().equals("officeID"))
                officeID = cookie.getValue();
        }
        if (username.isEmpty()) {
            response.sendRedirect("/roomy/index.html");
            return;
        }
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception, possibly rethrow as a RuntimeException
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
        String url = "jdbc:sqlite:C:/Users/lexiz/source/repos/roomy/src/main/db/db.db";
        String capacityID = request.getParameter("capacityID");
        String date = request.getParameter("date");
        boolean dateBooked = false;

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            String reservingPermitted = "SELECT username FROM reservation" +
                    "INNER JOIN  capacity ON reservation.capacityID=capacity.capacityID WHERE username = ?";
            String insertQuery = "INSERT INTO capacity ( officeID, bookedSpaces, date) VALUES ( ?, ?, ?)";
            String updateQuery = "UPDATE capacity SET bookedSpaces = bookedSpaces + 1 WHERE officeID = ? AND capacityID = ?";
            String query2 = "INSERT INTO reservation (capacityID, username) VALUES (?, ?)";
            // if there is no capacityID, there is no reservation
            if (capacityID.isEmpty()) {
                try (PreparedStatement insertStatement = conn.prepareStatement(insertQuery,
                        Statement.RETURN_GENERATED_KEYS)) {
                    insertStatement.setInt(1, Integer.parseInt(officeID));
                    insertStatement.setInt(2, 1);
                    insertStatement.setString(3, date);

                    int rowsInserted = insertStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        dateBooked = true;
                        try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                capacityID = String.valueOf(generatedKeys.getInt(1));
                            }
                        }
                    }
                }
            } else {
                try (PreparedStatement permittedStatement = conn.prepareStatement(reservingPermitted)) {
                    permittedStatement.setString(1, username);
                    try (ResultSet rs = permittedStatement.executeQuery()) {
                        if (rs.next()) {
                            response.sendRedirect("/roomy/bookingError.html");
                        }
                    }
                }
                try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, Integer.parseInt(officeID));
                    updateStatement.setInt(2, Integer.parseInt(capacityID));

                    int rowsUpdated = updateStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        dateBooked = true;
                    }
                }
            }

            conn.commit();

            try (PreparedStatement pstmt = conn.prepareStatement(query2)) {
                pstmt.setString(1, capacityID);
                pstmt.setString(2, username);
                // insert data
                pstmt.executeUpdate();

                // commit changes
                conn.commit();
                dateBooked = true;
            }
        } catch (SQLException e) {
            System.err.println("Error executing SQL query: " + e.getMessage());
        }
        if (dateBooked) {
            response.sendRedirect("/roomy/dateBooked.html");
        } else {
            response.sendRedirect("/roomy/bookingError.html");
        }
    }
}