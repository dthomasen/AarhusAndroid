package dk.dthomasen.aarhus.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fima.cardsui.views.CardUI;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.cards.DescCard;
import dk.dthomasen.aarhus.cards.ImageCard;
import dk.dthomasen.aarhus.cards.ShelterTitleCard;
import dk.dthomasen.aarhus.models.Baalplads;
import dk.dthomasen.aarhus.service.Service;

public class BaalpladsInfo extends Activity implements View.OnClickListener, LocationListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private Baalplads baalplads;
    private String latitudeToFind;
    private String longitudeToFind;
    private static final String ns = null;
    private String userLatitude;
    private String userLongitude;
    private Service service = Service.getInstance();
    private CardUI mCardView;
    private LocationManager locationManager;

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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(gpsIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
            }
        }
        userLatitude = intent.getExtras().getString("userLatitude");
        userLongitude = intent.getExtras().getString("userLongitude");
        baalplads = parseDocument("baalsteder.xml");

        // init CardView
        mCardView = (CardUI) findViewById(R.id.baalinfocardsview);
        mCardView.setSwipeable(false);

        // add AndroidViews Cards
        ShelterTitleCard nameCard = new ShelterTitleCard(baalplads.getNavn(), 0);
        DescCard descCard = new DescCard("Beskrivelse", baalplads.getBeskrivelse());
        DescCard praktiskCard = new DescCard("Praktisk", baalplads.getPraktisk());
        ImageCard imageCard = new ImageCard(this, "Billeder", baalplads.getBillede1(), baalplads.getBillede2(), baalplads.getBillede3(), baalplads.getBillede4());

        mCardView.addCard(nameCard);

        if(baalplads.getBillede1() != ""){
            mCardView.addCard(imageCard);
        }

        if(baalplads.getBeskrivelse() != ""){
            mCardView.addCard(descCard);
        }

        if(baalplads.getPraktisk() != ""){
            mCardView.addCard(praktiskCard);
        }

        mCardView.refresh();
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
        if(v.getId() == R.id.cardImage1){
            intent.putExtra("url",baalplads.getBillede1());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage2){
            intent.putExtra("url",baalplads.getBillede2());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage3){
            intent.putExtra("url",baalplads.getBillede3());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage4){
            intent.putExtra("url",baalplads.getBillede4());
            startActivity(intent);
        }else if(v.getId() == R.id.ABNavigateButton){
            Location userlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            try{
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + userlocation.getLatitude() + "," + userlocation.getLongitude() + "&daddr=" + latitudeToFind + "," + longitudeToFind + "&dirflg=w"));
                startActivity(intent);
            }catch(NullPointerException e){
                Toast.makeText(this, "Tænd for GPS for at benytte rutevejledning", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
