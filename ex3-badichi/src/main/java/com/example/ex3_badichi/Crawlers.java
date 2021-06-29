package com.example.ex3_badichi;

import java.util.ArrayList;
import java.util.List;

/**
 * wrapper to the web Crawlers
 */
public class Crawlers {
    List<WebCrawler> crawlerList;

    /**
     * here we add 1 wc to the list. synchronized to not step on each other data
     * @param wc to add to the list
     */
    public synchronized void add(WebCrawler wc){
        if (crawlerList == null) {
            crawlerList = new ArrayList<>();
        }
        crawlerList.add(wc);
    }

    /**
     * to get specific wc
     * @param i as id of the wc
     * @return the wc we asked
     */
    public synchronized WebCrawler getOne(int i){
        return crawlerList.get(i);
    }
}