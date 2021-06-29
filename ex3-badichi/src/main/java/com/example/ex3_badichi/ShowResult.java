package com.example.ex3_badichi;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ShowResult", value = "/ShowResult")
/**
 * servlet that show the results
 */
public class ShowResult extends HttpServlet {
    /**
     * do nothing. "if it's working don't touch"...
     * @param request the request
     * @param response the response
     * @throws ServletException if happened
     * @throws IOException when needed
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * post func that show the results
     * @param request to carry the session
     * @param response to write the results
     * @throws ServletException if happened
     * @throws IOException when needed
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        ServletContext context = getServletContext();
        Crawlers c = (Crawlers) context.getAttribute("crawlers");
        HttpSession session = request.getSession();
        int id = (int)session.getAttribute("id");//here we got the id for each search
        PrintWriter out = response.getWriter();
        out.println("<html><body><head><title>WebCrawler</title></head>");
        out.println("<h1>Crawling on: "+c.getOne(id).getUrlTc()+"</h1>");
        if (c.getOne(id).alive)
            out.println("<p>so far: "+c.getOne(id).getSumPictures()+" images</p><BR>" +
                    "<button onclick='location.reload()'>reload</button>");
        else
            out.println("<p>Found Total: "+c.getOne(id).getSumPictures()+" images</p><BR>Done!");
        out.println("<BR><a href=\"/LandingWebCrawler\">back to main page</a>");
        out.println("</body></html>");
        out.close();
    }
}
