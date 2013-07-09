package dk.dthomasen.aarhus.service;

import android.os.AsyncTask;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import dk.dthomasen.aarhus.rss.RssFeed;
import dk.dthomasen.aarhus.rss.RssItem;
import dk.dthomasen.aarhus.rss.RssReader;

public class GetFeedTask extends AsyncTask<String, Void, ArrayList<RssItem>> {

    protected final String TAG = this.getClass().getName();

    protected ArrayList<RssItem> doInBackground(String... urls) {
        URL url = null;
        RssFeed feed = null;
        try {
            url = new URL("http://www.aarhus.dk/da/Global/RSSfeeds/Nyheder-Aarhus-kommune.aspx");
            feed = RssReader.read(url);
            ArrayList<RssItem> rssItems = feed.getRssItems();
            return rssItems;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(ArrayList feed) {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
}