package exemple.demo2;
import java.io.*;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/DeleteCourseServlet")
public class DeleteCourseServlet extends HttpServlet {

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/users";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] coursesToDelete = request.getParameterValues("deleteCheckbox");

        if (coursesToDelete != null && coursesToDelete.length > 0) {
            try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
                for (String courseId : coursesToDelete) {
                    // Prepare SQL statement to delete course by its ID
                    String sql = "DELETE FROM courses WHERE course_id = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, courseId);

                    // Execute the delete statement
                    statement.executeUpdate();
                }
                response.sendRedirect("adminPannel");
            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.sendRedirect("adminPannel?error=delete_error");
            }
        } else {
            // No courses selected for deletion
            response.sendRedirect("adminPannel");
        }
    }
}
