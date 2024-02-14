package exemple.demo2;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/registerServlet")
public class registerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            int age = Integer.parseInt(request.getParameter("age"));
            String university = request.getParameter("university");
            String interests = request.getParameter("interests");
            PrintWriter out = response.getWriter();
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection con = DriverManager.getConnection("jdbc:mysql:///users", "root", "")) {
                    PreparedStatement statement = con.prepareStatement("INSERT INTO accounts (username, password, age, university, interests) VALUES (?, ?, ?, ?, ?)");
                    statement.setString(1, username);
                    statement.setString(2, password);
                    statement.setInt(3, age);
                    statement.setString(4, university);
                    statement.setString(5, interests);
                    statement.executeUpdate();
                    out.println("<script type=\"text/javascript\">");
                    out.println("alert('Registration succeeded!');");
                    out.println("window.location.href = 'frontapp.html';");
                    out.println("</script>");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (ClassNotFoundException e)
            {
                // Handle driver class loading error
                e.printStackTrace();
                // Redirect to an error page
                response.sendRedirect("error.html");
            }
        }

    }