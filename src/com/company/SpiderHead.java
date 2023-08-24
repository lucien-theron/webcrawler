package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SpiderHead {

    // Domains to search for {".com", ".co.za", "etc"}
    private String[] mDomainsToSearch = new String[]{".com", ".co.za"};

    // Max pages to search
    private int mMaxPagesToSearch = 100;

    // Starting link
    private String mStartPage = "http://heraldlive.co.za/";

    // Words to search for {"Zuma", "etc", "etc"};
    private String[] mWordsToSearch = new String[] {"Zuma", "ANC"};

    // True for match all words listed in mWordsToSearch, false match any words listed in mWordsToSearch
    private boolean mMatchAllWords = false;

    public static void main(String[] args) {
        new SpiderHead();
    }

    public SpiderHead() {

        Spider spider = new Spider();
        spider.setMaxPagesToSearch(mMaxPagesToSearch);
        spider.setDomainsToSearch(mDomainsToSearch);
        spider.setStartPage(mStartPage);
        spider.setWordsToSearch(mWordsToSearch);
        spider.setMatchAllWords(mMatchAllWords);
        spider.search();
    }
}