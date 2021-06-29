package com.example.ex3_badichi;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "LandingWebCrawler", value = "/LandingWebCrawler")
/**
 * servlet that start everything
 */
public class LandingWebCrawler extends HttpServlet {
    private String message;

    /**
     * here we start important variables for the whole program
     */
    public void init() {
        message = "Web Crawler";
        Crawlers c = new Crawlers();
        ServletContext context = getServletContext();
        context.setAttribute("crawlers",c);//now we can access the Crawlers to different servlet
    }

    /**
     * get func to get the url to crawl on
     * @param request the request
     * @param response to send the url to web crawler
     * @throws IOException if happened
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>WebCrawler</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css\" integrity=\"sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB\" crossorigin=\"anonymous\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body>");
        out.println("<h1 style=\"width:800px; margin:0 auto;\">" + message + "</h1>");
        //form to get the url
        out.println("<div style=\"width:800px; margin:0 auto;\"><Form method = post>enter url:<INPUT name='urltc' type='text'>" +
                "<INPUT type='submit' value='crawl'>" +
                "</Form></div>");
        out.println("</body></html>");
        out.close();
    }

    /**
     * post func to create web crawler on its own thread
     * @param request that carry the url
     * @param response the response
     * @throws ServletException if happened
     * @throws IOException when needed
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        ServletContext context = getServletContext();
        PrintWriter out = response.getWriter();
        Crawlers c = (Crawlers) context.getAttribute("crawlers");// the wrapper
        int md = Integer.parseInt(context.getInitParameter("maxdepth"));// max depth from web.xml
        String url;
        try {
            url = String.valueOf(new URL(request.getParameter("urltc")));
            WebCrawler wc = new WebCrawler(url,md);
            HttpSession session = request.getSession();
            session.setAttribute("id",wc.myId);// session for each thread
            Thread t = new Thread(wc);
            t.start();
            c.add(wc);
            System.out.println("Starting Thread for url "+url);
            out.println("<html><head><title>WebCrawler</title>\n" +
                    "<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css\" integrity=\"sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB\" crossorigin=\"anonymous\">" +"\n" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body>");
            out.println("<h1 style=\"width:800px; margin:0 auto;\">" + message + "</h1>");
            if(t.isAlive()) {
                out.println("<div style=\"width:800px; margin:0 auto;\"><p><h2>crawling!</h2><BR>" +
                        "to see result:</p>");
            } else
                out.println("<p>Done!<br>to see result:</p>");
            out.println("<form method ='post' action ='/ShowResult'>\n" +
                    "<INPUT type ='submit' value ='show' >" +
                    "</form></div>");
            out.println("</body></html>");
            out.close();
        } catch (MalformedURLException e) {// when url is not return 200
            System.out.println("bad url");
            response.sendRedirect("badUrl.html");
        }
    }

    /**
     * to finish nicely
     */
    public void destroy() {
    }
}