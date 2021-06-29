package com.example.ex3_badichi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashSet;

/**
 * web crawler class to crawl on the url thread by thread
 */
public class WebCrawler implements Runnable{

    private final HashSet<String> links;// to store the links
    private final int MAX_DEPTH;// maximum depth we crawl on the url, so we dont crawl forever
    public int sumPictures;
    public static int howManyUsers = 0;
    public int myId;// id for each search
    private final String UrlTc;// the url to start crawling from
    boolean alive;// to know if is still running when we don't have access to .isAlice()

    /**
     * url getter
     * @return url
     */
    public String getUrlTc() {
        return UrlTc;
    }

    /**
     * sum of pictures getter
     * @return sum of pictures
     */
    public int getSumPictures() {
        return sumPictures;
    }

    /**
     * constructor of the class
     * @param urlTc to start crawling from
     * @param md to stop when we go to deep
     */
    public WebCrawler(String urlTc, int md) {
        this.links = new HashSet<>();
        this.myId = howManyUsers;//this is how we create id to each search
        howManyUsers++;
        this.UrlTc = urlTc;
        MAX_DEPTH = md;
    }

    /**
     * the func that recursively crawl on the web, starting on the url
     * @param URL to start crawling from
     * @param depth to stop when we go to deep
     */
    public void getPageLinks(String URL, int depth) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if ((!links.contains(URL))&&(depth < MAX_DEPTH)) {
            System.out.println("Begin crawling "+URL+" at depth "+depth);
            System.out.println(">> Depth: " + depth + " [" + URL + "]");
            try {
                //4. (i) If not add it to the index
                links.add(URL);
                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");
                depth++;
                System.out.println("End crawling "+URL+" at depth "+depth);
                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"),depth);
                }
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    /**
     * after we gathered the html pages that connect to the url we start from -
     * let's count the pictures on them, using Jsoup
     */
    public void countPictures() {
        for (String link : links) {
            Document doc;
            try {
                doc = Jsoup.connect(link).get();
                Elements imageElements = doc.select("img");
                System.out.println("number of images found for "+link+" is "+imageElements.size());
                this.sumPictures += imageElements.size();
            } catch (IOException | IllegalArgumentException | NullPointerException e) {
                System.err.println("For '" + link + "': " + e.getMessage());
            }
        }

    }

    /**
     * to work on thread, we have to create run() func, that started the thread
     */
    public void run(){
        alive = true;
        getPageLinks(this.UrlTc,0);
        try {
            countPictures();
            System.out.println("total images "+getSumPictures());
            System.out.println("End of thread for url "+this.UrlTc);
            alive = false;
        } catch (IllegalArgumentException e ) {
            System.err.println("For ': " + e.getMessage());
        }
    }
}
