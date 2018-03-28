package wso2;

import org.apache.log4j.Logger;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(
        name = "getcards",
        urlPatterns = "/getcards"
)

public class servlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(servlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        try {

            HttpHandler httpHandler = new HttpHandler();
            String strProject = req.getParameter("project").replaceAll(" ","%20");
            logger.info("Requesting backend project cards ...");
            String url = "/githubProjectCardTracker/dashboard/?project=" + strProject;
            String backResponse = httpHandler.get(url);

//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("credentials", "same origin");
//            response.setHeader("Access-Control-Allow-Credentials", "true");
//            response.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
//            response.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
            resp.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = resp.getOutputStream();
            out.print(backResponse);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
