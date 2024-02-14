package exemple.demo2;

import java.io.*;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter out = response.getWriter();
        String referer = request.getHeader("referer");
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql:///users", "root", ""))
            {
                String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Login successful
                    //After the login we create a session for the user
                    HttpSession session = request.getSession(true);
                    session.setAttribute("authenticated", true);
                    session.setAttribute("username", username); // Store username in the session
                    if (referer != null && referer.contains("adminLogin.html"))
                    {
                        session.setAttribute("role", "admin");
                    }
                    else if (referer != null && referer.contains("frontapp.html")) {
                        session.setAttribute("role", "user");
                    }
                    session.setMaxInactiveInterval(60 * 60 * 24);

                    // Set a cookie with the session ID
                    Cookie sessionCookie = new Cookie("sessionid", session.getId());
                    sessionCookie.setMaxAge(60 * 60); // Set cookie expiry time : in one hour
                    response.addCookie(sessionCookie);
                    //response.sendRedirect("welcome.jsp"); // Redirect to a welcome page
                    if(session != null && session.getAttribute("authenticated") != null && (boolean) session.getAttribute("authenticated"))
                    {
                        if((session.getAttribute("role") != null) && "admin".equals(session.getAttribute("role")))
                        {
                            response.sendRedirect("adminPannel");
                        }
                        else if ((session.getAttribute("role") != null) && "user".equals(session.getAttribute("role")))
                        {
                            response.sendRedirect("listingServlet");
                        }
                    }
        } else {
            // Login failed
            out.println("<html><body><h3>Login failed. Invalid username or password.</h3></body></html>");
        }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        } catch (ClassNotFoundException e) {
            // Handle driver class loading error
            e.printStackTrace();
            // Redirect to an error page
            response.sendRedirect("error.html");
        }
    }
}
