<%@ page import="java.util.List" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <html>

        <head>
            <title>Book a room</title>
            <link rel="stylesheet" href="stylesheet.css">
        </head>

        <body>
            <h1>Aktuelle Reservierungen</h1>
            <div class="container">
                <main>
                    <ul class="nav__list">
                        <% String[][] calendar=(String[][]) request.getAttribute("calendar"); for (int i=0; i < 5; i++)
                            {%>
                            <li class="nav__item">
                                <form action="/roomy/edit" method="post" class="centered">

                                    <% if(calendar[i][0].equals("")) { %>
                                        <p> - </p>
                                        <% } else{ %>
                                            <p> Platz in <%= calendar[i][0] %> gebucht </p>
                                            <% } %>
                                                <input type="hidden" name="capacityID" value="<%= calendar[i][3] %>">
                                                <input type="hidden" name="date" value="<%= calendar[i][2] %>">

                                                <% if (calendar[i][1].equals(calendar[i][0])) { %>
                                                    <input type="submit"
                                                        value="<%= calendar[i][1] %>, der <%= calendar[i][2] %>"
                                                        class="calendarbutton" disabled>
                                                    <% } else { %>
                                                        <input type="submit"
                                                            value="<%= calendar[i][1] %>, der <%= calendar[i][2] %>"
                                                            class="calendarbutton">
                                                        <% } %>
                                </form>
                            </li>
                            <% } %>
                    </ul>
                </main>
                <br><br>
            </div>
            <form action="/roomy/homepage">
                <input type="submit" value="&crarr;" class="cuteBackwardsbutton">
            </form>
        </body>

        </html>