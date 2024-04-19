package servlets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HexFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/createAcc")
public class createAccServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // not needed
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("username").isEmpty()){
            response.sendRedirect("/roomy/index.html");
            return;
        }
        String username = request.getParameter("username");
        //password hashing
        String password = "";

        try{
        password = HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(request.getParameter("pw").getBytes(StandardCharsets.UTF_8)));
        }
        catch(NoSuchAlgorithmException e){
            System.out.println("Passwort konnte nicht gehasht werden");
        }
        boolean creatingSuccessful = false;

        
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception, possibly rethrow as a RuntimeException
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
        
        // Database opening
        String url = "jdbc:sqlite:C:/Users/lexiz/source/repos/roomy/src/main/db/db.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            // starting transaction 
            conn.setAutoCommit(false);

            // INSERT Statement
            String query = "INSERT INTO loginData (username, pw) VALUES (?,?)";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                // setting parameters
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                int rowInserted = pstmt.executeUpdate();
                if (rowInserted > 0) {
                    creatingSuccessful = true;
                }
            } catch (SQLException e) {
                System.err.println("Datenbankfehler: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to SQLite database: " + e.getMessage());
        }

        // redirecting to page

        if (creatingSuccessful) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/homepage");
            dispatcher.forward(request, response);
        } 
        else {
            response.sendRedirect("/roomy/creatingError.html");
        }
    }
}