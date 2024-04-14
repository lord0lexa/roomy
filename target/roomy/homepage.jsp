<%@ page import="java.util.List" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <html>

        <head>
            <link rel="stylesheet" href="stylesheet.css">
            <title>Willkommen bei Roomy!</title>
        </head>

        <body>
            <div class="container">
                <header>
                    <h1>Roomy</h1>
                    <h2>Welcome back!</h2>
                </header>
                <main>
                    <nav>
                        <article>
                            <a href="/roomy/showOffices">Book a room</a>
                            <p>Book a room for this week</p>
                        </article>
                        <article>
                            <a href="/roomy/manageBookings">Manage your Booking</a>
                            <p>Bookingmanagement</p>
                        </article>
                        <article>
                            <a href="/roomy/calender">Calendar</a>
                            <p>Manage your bookings in this calendar</p>
                        </article>
                    </nav>
                    <nav>
                        <br><br><br>

                        <form action="/roomy/logout" method="post">
                            <input type="submit" value="Logout &crarr;" class="cuteBackwardsbutton">
                        </form>
                        <% if(request.getAttribute("admin").toString().equalsIgnoreCase("true")){%>
                            <form action="/roomy/createAcc" method="post">
                                <input type="submit" value="Neuen Account erstellen" class="cuteBackwardsbutton">
                            </form>

                            <form action="/roomy/createOffice" method="post">
                                <input type="submit" value="Office hinzufügen" class="cuteBackwardsbutton">
                            </form>
                            <%} %>
                    </nav>
                </main>
            </div>
        </body>

        </html>