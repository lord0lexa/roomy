package servlets;

import java.io.IOException;

import jakarta.servlet.http.Cookie;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // not needed
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //deleting cookie
        Cookie deleteCookie = new Cookie("username", "");
        deleteCookie.setMaxAge(0); 
        // Add cookie to response
        response.addCookie(deleteCookie);
        
        //redirecting to login page
        RequestDispatcher dispatcher = request.getRequestDispatcher("index.html");
        dispatcher.forward(request, response);
    }
}
