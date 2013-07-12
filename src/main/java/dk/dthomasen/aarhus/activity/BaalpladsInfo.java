package dk.dthomasen.aarhus.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.models.Baalplads;
import dk.dthomasen.aarhus.service.DownloadImage;
import dk.dthomasen.aarhus.service.Service;

public class BaalpladsInfo extends Activity implements View.OnClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private Baalplads baalplads;
    private String latitudeToFind;
    private String longitudeToFind;
    private static final String ns = null;
    private String userLatitude;
    private String userLongitude;
    private Service service = Service.getInstance();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baalpladsinfo);

        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        Intent intent = getIntent();
        latitudeToFind = intent.getExtras().getString("latitude");
        longitudeToFind = intent.getExtras().getString("longitude");
        ActionBar ab = getActionBar();
        ab.setCustomView(R.layout.infoactionbar);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true);

        ((ImageButton)findViewById(R.id.ABNavigateButton)).setOnClickListener(this);

        userLatitude = intent.getExtras().getString("userLatitude");
        userLongitude = intent.getExtras().getString("userLongitude");
        baalplads = parseDocument("baalsteder.xml");

        ImageView baalImage1 = (ImageView) findViewById(R.id.baalImage1);
        ImageView baalImage2 = (ImageView) findViewById(R.id.baalImage2);
        ImageView baalImage3 = (ImageView) findViewById(R.id.baalImage3);
        ImageView baalImage4 = (ImageView) findViewById(R.id.baalImage4);

        if(baalplads.getBillede1() != ""){
            new DownloadImage(baalImage1)
                    .execute(baalplads.getBillede1());
        }else{
            baalImage1.setVisibility(View.GONE);
        }
        if(baalplads.getBillede2() != ""){
            new DownloadImage(baalImage2)
                    .execute(baalplads.getBillede2());
        }else{
            baalImage2.setVisibility(View.GONE);
        }
        if(baalplads.getBillede3() != ""){
            new DownloadImage(baalImage3)
                    .execute(baalplads.getBillede3());
        }else{
            baalImage3.setVisibility(View.GONE);
        }
        if(baalplads.getBillede4() != ""){
            new DownloadImage(baalImage4)
                    .execute(baalplads.getBillede4());
        }else{
            baalImage4.setVisibility(View.GONE);
        }

        ((TextView)findViewById(R.id.baalName)).setText(baalplads.getNavn());
        ((TextView)findViewById(R.id.baalBeskrivelse)).setText(baalplads.getBeskrivelse());
        ((TextView)findViewById(R.id.baalPraktisk)).setText(baalplads.getPraktisk());

        baalImage1.setOnClickListener(this);
        baalImage2.setOnClickListener(this);
        baalImage3.setOnClickListener(this);

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
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private Baalplads parseDocument(String file){
        Baalplads baalplads = new Baalplads();
        XmlPullParserFactory pullParserFactory = null;
        XmlPullParser parser = null;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            parser = pullParserFactory.newPullParser();
            InputStream in_s = openFileInput(file);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, ns, "baalsteder");

            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if(parser.getEventType() != XmlPullParser.START_TAG){
                    continue;
                }

                String name = parser.getName();
                if(name.equals("baalsted")){
                    baalplads = parseBaalsted(parser);
                }else{
                    service.skip(parser);
                }
                if(baalplads.getLatitude().equals(latitudeToFind) && baalplads.getLongitude().equals(longitudeToFind)){
                    return baalplads;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baalplads;
    }

    private Baalplads parseBaalsted(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "baalsted");

        Baalplads currentBaalplads = new Baalplads();
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();

            if(name.equals("navn")){
                currentBaalplads.setNavn(service.readText(parser));
            }else if(name.equals("beskrivelse")){
                currentBaalplads.setBeskrivelse(service.readText(parser));
            }else if(name.equals("praktisk")){
                currentBaalplads.setPraktisk(service.readText(parser));
            }else if(name.equals("latitude")){
                currentBaalplads.setLatitude(service.readText(parser));
            }else if(name.equals("longitude")){
                currentBaalplads.setLongitude(service.readText(parser));
            }else if(name.equals("billeder")){
                currentBaalplads = parseBilleder(parser, currentBaalplads);
            }else{
                service.skip(parser);
            }
        }
        return currentBaalplads;
    }

    private Baalplads parseBilleder(XmlPullParser parser, Baalplads currentBaalPlads) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "billeder");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();

            if(name.equals("billede")){
                if(currentBaalPlads.getBillede1().equals("")){
                    currentBaalPlads.setBillede1(service.readText(parser));
                }else if(currentBaalPlads.getBillede2().equals("")){
                    currentBaalPlads.setBillede2(service.readText(parser));
                }else if(currentBaalPlads.getBillede3().equals("")){
                    currentBaalPlads.setBillede3(service.readText(parser));
                }else if(currentBaalPlads.getBillede4().equals("")){
                    currentBaalPlads.setBillede4(service.readText(parser));
                }
            }
        }

        return currentBaalPlads;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, TouchImageViewActivity.class);
        if(v.getId() == R.id.baalImage1){
            intent.putExtra("url",baalplads.getBillede1());
            startActivity(intent);
        }else if(v.getId() == R.id.baalImage2){
            intent.putExtra("url",baalplads.getBillede2());
            startActivity(intent);
        }else if(v.getId() == R.id.baalImage3){
            intent.putExtra("url",baalplads.getBillede3());
            startActivity(intent);
        }else if(v.getId() == R.id.ABNavigateButton){
            try{
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + userLatitude + "," + userLongitude + "&daddr=" + latitudeToFind + "," + longitudeToFind + "&dirflg=w"));
                startActivity(intent);
            }catch(NullPointerException e){
                Toast.makeText(this, "Tænd for GPS eller Placeringsdeling for at benytte rutevejledning", Toast.LENGTH_LONG).show();
            }
        }

    }
}
