package servlets;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.text.SimpleDateFormat;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;


@WebServlet("/manageBookings")

public class manageBookingsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // not needed
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception, possibly rethrow as a RuntimeException
            throw new RuntimeException("Failed to load JDBC driver", e);
        }

        if(request.getCookies() == null){
            response.sendRedirect("/roomy/index.html");
            return;
        }
        Cookie[] cookies = request.getCookies();
        String username = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user"))
                username = cookie.getValue();
        }

        if(username.isEmpty()){
            response.sendRedirect("/roomy/index.html");
            return;
        }

        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        // Array content: officeName, Day of Week, Date, capacityID
        String[][] calendarSpaces = new String[5][4];

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        int y = 0;

        while (y < 5) {
            if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                calendarSpaces[y][0] = "";
                calendarSpaces[y][1] = sdf.format(c.getTime());
                calendarSpaces[y][2] = date.format(c.getTime());
                calendarSpaces[y][3] = "";
                y++;
            }
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Database opening
        String url = "jdbc:sqlite:C:/Users/lexiz/source/repos/roomy/src/main/db/db.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            // starting transaction
            conn.setAutoCommit(false);

            String query = "SELECT name, date, capacity.capacityID FROM offices " + 
            "INNER JOIN capacity ON offices.officeID=capacity.officeID " +
            "INNER JOIN reservation ON capacity.capacityID=reservation.capacityID " +
            "WHERE username = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, username);

                try (ResultSet rs = pstmt.executeQuery()) {
                    //todo: finding a better logik without redundancy
                    while (rs.next()){
                        if (calendarSpaces[0][2].equals(rs.getString("date"))) {
                            calendarSpaces[0][0] = rs.getString("name");
                            calendarSpaces[0][3] = rs.getString("capacityID");
                        }
                        if (calendarSpaces[1][2].equals(rs.getString("date"))) {
                            calendarSpaces[1][0] = rs.getString("name");
                            calendarSpaces[1][3] = rs.getString("capacityID");
                        }
                        if (calendarSpaces[2][2].equals(rs.getString("date"))) {
                            calendarSpaces[2][0] = rs.getString("name");
                            calendarSpaces[2][3] = rs.getString("capacityID");
                        }
                        if (calendarSpaces[3][2].equals(rs.getString("date"))) {
                            calendarSpaces[3][0] = rs.getString("name");
                            calendarSpaces[3][3] = rs.getString("capacityID");
                        }
                        if (calendarSpaces[4][2].equals(rs.getString("date"))) {
                            calendarSpaces[4][0] = rs.getString("name");
                            calendarSpaces[4][3] = rs.getString("capacityID");
                        }
                    } 
                }
            } catch (SQLException e) {
                System.err.println("Datenbankfehler: " + e.getMessage());

            }
        } 
        catch (SQLException e) {
            System.out.println("Error connecting to SQLite database: " + e.getMessage());
        }

        request.setAttribute("calendar", calendarSpaces);
        RequestDispatcher dispatcher = request.getRequestDispatcher("manageBookings.jsp");
        dispatcher.forward(request, response);
    }
}