<%@ page import="java.util.List" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <html>

        <head>
            <title>Book a room</title>
            <link rel="stylesheet" href="stylesheet.css">
        </head>

        <body>
            <h1>Book a room</h1>
            <div class="container">
                <main>
                    <ul class="nav__list">
                        <% String[][] calendar=(String[][]) request.getAttribute("calendar"); for (int i=0; i < 5; i++)
                            {%>
                            <li class="nav__item">
                                <form action="/roomy/bookDate" method="post" class="centered">
                                    <p class="thickstyling">
                                        <%= calendar[i][1]%> / <%= calendar[i][0] %> Plätze besetzt
                                    </p>
                                    <% if (calendar[i][1].equals(calendar[i][0])) { %>
                                        <p> Büro ausgebucht.</p> <br>
                                        <%} else{ %>
                                            <p></p> <br>
                                            <%}%>
                                                <input type="hidden" name="capacityID" value="<%= calendar[i][4] %>">
                                                <input type="hidden" name="date" value="<%= calendar[i][3] %>">
                                                <% if (calendar[i][1].equals(calendar[i][0])) { %>
                                                    <input type="submit"
                                                        value="<%= calendar[i][2] %>, der <%= calendar[i][3] %>"
                                                        class="calendarbutton" disabled>
                                                    <% } else { %>
                                                        <input type="submit"
                                                            value="<%= calendar[i][2] %>, der <%= calendar[i][3] %>"
                                                            class="calendarbutton">
                                                        <% } %>
                                </form>
                            </li>
                            <% } %>
                    </ul>
                </main>
            </div>
            <br><br>
            <form action="/roomy/showOffices">
                <input type="submit" value="&crarr;" class="cuteBackwardsbutton">
            </form>
        </body>

        </html>