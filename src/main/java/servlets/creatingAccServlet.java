package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

//Servlet shows jsp-page
@WebServlet("/creatingAcc")
public class creatingAccServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // not needed
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getCookies() == null) {
            response.sendRedirect("/roomy/index.html");
            return;
        }
        String username = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username"))
                username = cookie.getValue();
        }

        if (username.isEmpty()) {
            response.sendRedirect("/roomy/index.html");
            return;
        }

        System.out.println(username);
        if(username.equals("admin")){
            response.sendRedirect("/roomy/creatingAcc.html");
            return;
        }
        else{
            response.sendRedirect("/roomy/index.html");
            return;
        }
    }
}