package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<>();
    private Document htmlDocument;


    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     *
     * @param url - The URL to visit
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url, String[] domains) {

        //Todo: check if url is correct format
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT); // creates a valid user to browse with, else thinks it is a bot
            Document htmlDocument = connection.get(); // get all data on page
            this.htmlDocument = htmlDocument; // assign to global if successful

            //check if page is valid:
            if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            // indicating that everything is great.
            {
                System.out.println("\n**Visiting** Received web page at " + url);
            }
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }

            Elements linksOnPage = htmlDocument.select("a[href]"); // get all links on page

            //System.out.println("Found (" + linksOnPage.size() + ") links"); //indicate how many links on page

            for (Element link : linksOnPage) {
                String nextUrl = link.absUrl("href");
                for (String domain : domains) {
                    if (nextUrl.contains(domain)) {
                        //System.out.println(nextUrl);
                        this.links.add(nextUrl); // add all links to linked list
                    }
                }
            }
            return true; // success!
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
            return false;
        }
    }

    public List<String> searchForWordOr(String[] wordsToSearch) {
        List<String> wordsFound = new ArrayList<>();
        String bodyText = this.htmlDocument.body().text();
        for (String searchWord : wordsToSearch) {
            if(bodyText.toLowerCase().matches(".*\\b"+searchWord.toLowerCase()+"\\b.*")) {
                wordsFound.add(searchWord);
            }
        }
        if(wordsFound.size()==0)
            return null;
        return wordsFound;
    }

    public List<String> searchForWordAnd(String[] wordsToSearch) {
        List<String> wordsFound = new ArrayList<>();
        String bodyText = this.htmlDocument.body().text();
        for (String searchWord : wordsToSearch) {
            if(bodyText.toLowerCase().matches(".*\\b"+searchWord.toLowerCase()+"\\b.*")) {
                wordsFound.add(searchWord);
            } else {
                return null;
            }
        }
        return wordsFound;
    }


    public List<String> getLinks() {
        return this.links;
    }

}