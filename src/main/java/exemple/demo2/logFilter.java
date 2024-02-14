package exemple.demo2;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

@WebFilter("/*")
public class logFilter implements Filter {
    private static final String LOG_FILE_PATH = "C:\\Users\\MSI\\Desktop\\Java\\MessagingApp\\demo2\\src\\main\\java\\exemple\\demo2\\logfile";
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Log the request information
        logRequest(request);

        // Proceed with the request
        chain.doFilter(req, res);

        // Log the response information
        logResponse(response);
    }

    private void logRequest(HttpServletRequest request) throws IOException {
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String ipAddress = request.getRemoteAddr();
        String timestamp = new Date().toString();

        // Log the request details
        System.out.println(timestamp + " - Request: " + method + " " + url + " from IP: " + ipAddress +"\n");
        String logMessage = timestamp + " - Request: " + method + " " + url + " from IP: " + ipAddress +"\n";
        writeToFile(logMessage);
    }

    private void logResponse(HttpServletResponse response) throws IOException {
        int statusCode = response.getStatus();
        String timestamp = new Date().toString();

        // Log the response details

        System.out.println(timestamp + " - Response: Status " + statusCode +"\n");
        String LogMessage = timestamp + " - Response: Status " + statusCode +"\n" ;
        writeToFile(LogMessage);
    }
    private void writeToFile(String logMessage) throws IOException {
        File logFile = new File(LOG_FILE_PATH);
        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(logMessage + "\n");
        }
    }

}
