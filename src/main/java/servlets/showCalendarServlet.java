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

@WebServlet("/showCalendar")
public class showCalendarServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
        String url = "jdbc:sqlite:C:/Users/lexiz/source/repos/roomy/src/main/db/db.db";
        
        //Access only, if user is logged in
        if(request.getCookies() == null){
            response.sendRedirect("/roomy/index.html");
            return;
        }
        Cookie[] cookies = request.getCookies();
        String username = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username"))
                username = cookie.getValue();
        }
        if(username.isEmpty()){
            response.sendRedirect("/roomy/index.html");
            return;
        }

        //calendar stuff
        SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        // Array content: spaces, bookedspaces, Day of Week, Date, capacityID
        String[][] calendarSpaces = new String[5][5];

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        int y = 0;

        while (y < 5) {
            if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                calendarSpaces[y][0] = request.getParameter("spaces");
                calendarSpaces[y][1] = "0";
                calendarSpaces[y][2] = sdf.format(c.getTime());
                calendarSpaces[y][3] = date.format(c.getTime());
                calendarSpaces[y][4] = "";
                y++;
            }
            c.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Connecting to Database
        try (Connection conn = DriverManager.getConnection(url)) {
            // starting transaction
            conn.setAutoCommit(false);

            // INSERT Statement
            String query = "SELECT bookedSpaces, date, capacityID FROM capacity WHERE officeID = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, request.getParameter("officeID"));

                // Daten abfragen
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        if (calendarSpaces[0][3].equals(rs.getString("date"))) {
                            calendarSpaces[0][1] = rs.getString("bookedSpaces");
                            calendarSpaces[0][4] = rs.getString("capacityID");
                        }
                        if (calendarSpaces[1][3].equals(rs.getString("date"))) {
                            calendarSpaces[1][1] = rs.getString("bookedSpaces");
                            calendarSpaces[1][4] = rs.getString("capacityID");
                        }
                        if (calendarSpaces[2][3].equals(rs.getString("date"))) {
                            calendarSpaces[2][1] = rs.getString("bookedSpaces");
                            calendarSpaces[2][4] = rs.getString("capacityID");
                        }
                        if (calendarSpaces[3][3].equals(rs.getString("date"))) {
                            calendarSpaces[3][1] = rs.getString("bookedSpaces");
                            calendarSpaces[3][4] = rs.getString("capacityID");
                        }
                        if (calendarSpaces[4][3].equals(rs.getString("date"))) {
                            calendarSpaces[4][1] = rs.getString("bookedSpaces");
                            calendarSpaces[4][4] = rs.getString("capacityID");
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error executing query: " + e.getMessage());
                }
            } catch (SQLException e) {
                System.err.println("Error creating prepared statement: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to SQLite database: " + e.getMessage());
        }

        // Create a cookie named "office" with the officeID as its value
        Cookie officeCookie = new Cookie("officeID", request.getParameter("officeID"));
        // Set expiry for 30 minutes
        officeCookie.setMaxAge(30 * 60);
        // Add cookie to response
        response.addCookie(officeCookie);

        request.setAttribute("calendar", calendarSpaces);
        // send request to JSP 
        RequestDispatcher dispatcher = request.getRequestDispatcher("calendar.jsp");
        dispatcher.forward(request, response);
    }
}
