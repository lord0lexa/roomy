<%@ page import="java.util.List" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <html>

        <head>
            <title>Verfügbare Offices</title>
            <link rel="stylesheet" href="stylesheet.css">
        </head>

        <body>
            <h1>Verfügbare Offices</h1>

            <div class="container">
                <main>
                    <ul class="nav__list">
                        <% List<String[]> officeAttributes = (List<String[]>) request.getAttribute("offices");
                                for (String[] attributes : officeAttributes) {
                                %>
                                <li class="nav__item">
                                    <form action="/roomy/showCalendar" method="get" class="centered">
                                        <p class="thickstyling">
                                            <% for (int i=0; i < 3; i++) { %>
                                                <%= attributes[i] %>
                                                    <% if (i < 2) { %>, <% } else { %> Plätze<% } %> <br>
                                                                <% } %>
                                        </p>

                                        <input type="hidden" name="name" value="<%= attributes[0] %>">
                                        <input type="hidden" name="officeID" value="<%=attributes[3] %>">
                                        <input type="hidden" name="spaces" value="<%= attributes[2] %>">
                                        <input type="submit" value="Office auswählen" class="calendarbutton">
                                    </form>
                                </li>
                                <% } %>
                    </ul>
                </main>
            </div>
            <br><br>
            <form action="/roomy/homepage">
                <input type="submit" value="&crarr;" class="cuteBackwardsbutton">
            </form>
        </body>

        </html>