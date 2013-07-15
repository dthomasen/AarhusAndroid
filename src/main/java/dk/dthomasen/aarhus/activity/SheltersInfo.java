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
import dk.dthomasen.aarhus.cards.LargeImageCard;
import dk.dthomasen.aarhus.cards.TitleCard;
import dk.dthomasen.aarhus.models.Shelter;
import dk.dthomasen.aarhus.service.Service;

public class SheltersInfo extends Activity implements View.OnClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private Shelter shelter;
    private String latitudeToFind;
    private String longitudeToFind;
    private static final String ns = null;
    private String userLatitude;
    private String userLongitude;
    private Service service = Service.getInstance();
    private CardUI mCardView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheltersinfo);

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
        shelter = parseDocument("shelters.xml");

        // init CardView
        mCardView = (CardUI) findViewById(R.id.shelterInfoCardsView);
        mCardView.setSwipeable(false);

        // add AndroidViews Cards
        TitleCard nameCard = new TitleCard(shelter.getNavn());
        DescCard descCard = new DescCard("Beskrivelse", shelter.getBeskrivelse());
        DescCard praktiskCard = new DescCard("Praktisk", shelter.getPraktisk());
        DescCard vejledningCard = new DescCard("Vejledning", shelter.getVejledning());
        LargeImageCard imageCard = new LargeImageCard(this, "Billeder", shelter.getBillede1(), shelter.getBillede2(), shelter.getBillede3(), shelter.getBillede4(), shelter.getBillede5(), shelter.getBillede6());

        mCardView.addCard(nameCard);
        mCardView.addCard(imageCard);
        mCardView.addCard(descCard);
        mCardView.addCard(praktiskCard);
        mCardView.addCard(vejledningCard);
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

    private Shelter parseDocument(String file){
        Shelter shelter = new Shelter();
        XmlPullParserFactory pullParserFactory = null;
        XmlPullParser parser = null;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            parser = pullParserFactory.newPullParser();
            InputStream in_s = openFileInput(file);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, ns, "shelters");

            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if(parser.getEventType() != XmlPullParser.START_TAG){
                    continue;
                }

                String name = parser.getName();
                if(name.equals("shelter")){
                    shelter = parseShelter(parser);
                }else{
                    service.skip(parser);
                }
                if(shelter.getLatitude().equals(latitudeToFind) && shelter.getLongitude().equals(longitudeToFind)){
                    return shelter;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return shelter;
    }

    private Shelter parseShelter(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "shelter");

        Shelter currentShelter = new Shelter();
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();

            if(name.equals("navn")){
                currentShelter.setNavn(service.readText(parser));
            }else if(name.equals("beskrivelse")){
                currentShelter.setBeskrivelse(service.readText(parser));
            }else if(name.equals("praktisk")){
                currentShelter.setPraktisk(service.readText(parser));
            }else if(name.equals("vejledning")){
                currentShelter.setVejledning(service.readText(parser));
            }else if(name.equals("latitude")){
                currentShelter.setLatitude(service.readText(parser));
            }else if(name.equals("longitude")){
                currentShelter.setLongitude(service.readText(parser));
            }else if(name.equals("billeder")){
                currentShelter = parseBilleder(parser, currentShelter);
            }else{
                service.skip(parser);
            }
        }
        return currentShelter;
    }

    private Shelter parseBilleder(XmlPullParser parser, Shelter currentShelter) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "billeder");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();

            if(name.equals("billede")){
                if(currentShelter.getBillede1().equals("")){
                    currentShelter.setBillede1(service.readText(parser));
                }else if(currentShelter.getBillede2().equals("")){
                    currentShelter.setBillede2(service.readText(parser));
                }else if(currentShelter.getBillede3().equals("")){
                    currentShelter.setBillede3(service.readText(parser));
                }else if(currentShelter.getBillede4().equals("")){
                    currentShelter.setBillede4(service.readText(parser));
                }else if(currentShelter.getBillede5().equals("")){
                    currentShelter.setBillede5(service.readText(parser));
                }else if(currentShelter.getBillede6().equals("")){
                    currentShelter.setBillede6(service.readText(parser));
                }
            }
        }

        return currentShelter;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, TouchImageViewActivity.class);
        if(v.getId() == R.id.cardImage1){
            intent.putExtra("url", shelter.getBillede1());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage2){
            intent.putExtra("url", shelter.getBillede2());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage3){
                intent.putExtra("url", shelter.getBillede3());
                startActivity(intent);
        }else if(v.getId() == R.id.cardImage4){
            intent.putExtra("url", shelter.getBillede4());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage5){
            intent.putExtra("url", shelter.getBillede5());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage6){
            intent.putExtra("url", shelter.getBillede6());
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
