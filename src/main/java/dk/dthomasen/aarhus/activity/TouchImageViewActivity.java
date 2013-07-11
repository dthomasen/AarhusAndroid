package dk.dthomasen.aarhus.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.service.DownloadImage;
import dk.dthomasen.aarhus.service.TouchImageView;


public class TouchImageViewActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimage);
        ActionBar ab = getActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        new DownloadImage((ImageView) findViewById(R.id.fullImage))
                .execute(getIntent().getExtras().getString("url"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }
}