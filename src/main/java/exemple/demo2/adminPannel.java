package exemple.demo2;

import java.io.*;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/adminPannel")
public class adminPannel extends HttpServlet {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/users";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses");
            ResultSet resultSet = statement.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Courses List</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; }");
            out.println("h1 { color: #333; }");
            out.println("ul { list-style-type: none; padding: 0; }");
            out.println("li { margin-bottom: 10px; }");
            out.println("a { color: #007bff; text-decoration: none; }");
            out.println("a:hover { text-decoration: underline; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Courses List</h1>");
            out.println("<form action='DeleteCourseServlet' method='post'>");
            out.println("<ul>");

            while (resultSet.next()) {
                String courseId = resultSet.getString("course_id");
                String courseName = resultSet.getString("course_name");
                String courseLink = resultSet.getString("course_link");

                // Generate HTML for course list item with link to PDF
                out.println("<li>");
                out.println("<a href='" + courseLink + "' target='_blank'>" + courseName + "</a>");
                out.println("<input type='checkbox' name='deleteCheckbox' value='" + courseId + "'>");
                out.println("</li>");
            }

            out.println("</ul>");
            out.println("<input type='submit' value='Delete Selected Courses'>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("<h1>Error retrieving courses list</h1>");
        }
    }
}