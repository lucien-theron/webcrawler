package com.company;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Spider {

    private CSVWriter mCSVWriter;
    private Set<String> pagesVisited = new HashSet<>();
    private List<String> pagesToVisit = new LinkedList<>();
    private String[] mDomainsToSearch;
    private int mMaxPagesToSearch;
    private String mStartPage;
    private String[] mWordsToSearch;
    private boolean mMatchAllWords;

    public void search() {
        //check if number of pages visited is less than number of pages to search
        try {
            mCSVWriter = new CSVWriter(new FileWriter("results.csv"));
            while (this.pagesVisited.size() < mMaxPagesToSearch) {

                String currentUrl;

                SpiderLeg leg = new SpiderLeg();

                if (this.pagesToVisit.isEmpty()) {
                    currentUrl = mStartPage;
                    this.pagesVisited.add(mStartPage);
                } else {
                    currentUrl = this.nextUrl();
                }
                // Start crawl
                if (leg.crawl(currentUrl, mDomainsToSearch)) {
                    // Search for words
                    List<String> wordsFound;
                    if (mMatchAllWords) {
                        wordsFound = leg.searchForWordAnd(mWordsToSearch);
                    } else {
                        wordsFound = leg.searchForWordOr(mWordsToSearch);
                    }
                    if (wordsFound != null) {
                        // if page contains words, add to link and words to a list
                        List<String> line = new ArrayList<>();
                        line.add(currentUrl);
                        System.out.println(currentUrl);
                        for (String word : wordsFound) {
                            line.add(word);
                            System.out.print(word + " ");
                        }
                        String[] array = line.toArray(new String[0]);
                        System.out.println("towrite: " + array[0]);
                        mCSVWriter.writeNext(array);
                        // Either save to file or memory
                    }
                    this.pagesToVisit.addAll(leg.getLinks());
                }

            }
            System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
            mCSVWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the next URL to visit (in the order that they were found). We also do a check to make
     * sure this method doesn't return a URL that has already been visited.
     *
     * @return
     */
    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while (this.pagesVisited.contains(nextUrl));

        this.pagesVisited.add(nextUrl);

        return nextUrl;
    }

    public void setMaxPagesToSearch(int maxPagesToSearch) {
        this.mMaxPagesToSearch = maxPagesToSearch;
    }

    public void setDomainsToSearch(String[] domainsToSearch) {
        this.mDomainsToSearch = domainsToSearch;
    }

    public void setStartPage(String mStartPage) {
        this.mStartPage = mStartPage;
    }

    public void setWordsToSearch(String[] mWordsToSearch) {
        this.mWordsToSearch = mWordsToSearch;
    }

    public void setMatchAllWords(boolean mMatchAllWords) {
        this.mMatchAllWords = mMatchAllWords;
    }
}