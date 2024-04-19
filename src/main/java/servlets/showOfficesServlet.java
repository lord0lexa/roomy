package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.Cookie;

@WebServlet("/showOffices")

public class showOfficesServlet extends HttpServlet {
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

        //Access only if user is logged in
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

        // Database opening
        String url = "jdbc:sqlite:C:/Users/lexiz/source/repos/roomy/src/main/db/db.db";

        List<String[]> officeAttributes = new ArrayList<>();

        // Connecting to Database
        try (Connection conn = DriverManager.getConnection(url)) {
            // starting transaction
            conn.setAutoCommit(false);

            // INSERT Statement
            String query = "SELECT name, place, spaces, officeID FROM offices";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {

                // Daten abfragen
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()){
                        officeAttributes.add(new String[]{rs.getString("name"), rs.getString("place"), rs.getString("spaces"), rs.getString("officeID")});
                    } 
                }
            } catch (SQLException e) {
                System.err.println("Datenbankfehler: " + e.getMessage());

            }
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database: " + e.getMessage());
        }

        // redirecting to page


        request.setAttribute("offices", officeAttributes);
        // Request an eine JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("offices.jsp");
        dispatcher.forward(request, response);

    }
}
