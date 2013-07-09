package dk.dthomasen.aarhus.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.rss.RssItem;
import dk.dthomasen.aarhus.service.GetFeedTask;
import dk.dthomasen.aarhus.service.RssAdapter;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private ListView rssList;
    private ArrayList<RssItem> feed = new ArrayList<RssItem>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        ActionBar ab = getActionBar();
        rssList = (ListView) findViewById(R.id.rssList);

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Friluft Aarhus");
        ab.setSubtitle("App'en om friluftsliv i Aarhus");

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showRss = sharedPreferences.getBoolean("prefShowRss", true);

        if(showRss){


            try {
                feed = new GetFeedTask().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(feed != null){
                rssList.setAdapter(new RssAdapter(this, feed));
                rssList.setOnItemClickListener(this);
            }else{
            }
        }else{
            findViewById(R.id.nyhederHL).setVisibility(View.GONE);
            findViewById(R.id.nyhederSep).setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if ( slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            this.slidingMenu.toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.slidingMenu.toggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        RssItem rssItem = (RssItem) rssList.getItemAtPosition(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssItem.getLink()));
        startActivity(intent);

    }

}
