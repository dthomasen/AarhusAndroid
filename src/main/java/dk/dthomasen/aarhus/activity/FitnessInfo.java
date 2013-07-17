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
import dk.dthomasen.aarhus.cards.ImageCard;
import dk.dthomasen.aarhus.cards.TitleCard;
import dk.dthomasen.aarhus.models.Baalplads;
import dk.dthomasen.aarhus.models.Fitness;
import dk.dthomasen.aarhus.service.Service;

public class FitnessInfo extends Activity implements View.OnClickListener{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private Fitness fitness;
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
        setContentView(R.layout.fitnessinfo);

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
        fitness = parseDocument("fitness.xml");

        // init CardView
        mCardView = (CardUI) findViewById(R.id.fitnessinfocardsview);
        mCardView.setSwipeable(false);

        // add AndroidViews Cards
        TitleCard nameCard = new TitleCard(fitness.getNavn());
        DescCard descCard = new DescCard("Beskrivelse", fitness.getBeskrivelse());
        DescCard praktiskCard = new DescCard("Praktisk", fitness.getPraktisk());
        ImageCard imageCard = new ImageCard(this, "Billeder", fitness.getBillede1(), fitness.getBillede2(), fitness.getBillede3(), fitness.getBillede4());

        mCardView.addCard(nameCard);

        if(fitness.getBillede1() != ""){
            mCardView.addCard(imageCard);
        }
        if(fitness.getBeskrivelse() != ""){
            mCardView.addCard(descCard);
        }

        if(fitness.getPraktisk() != ""){
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

    private Fitness parseDocument(String file){
        Fitness fitness = new Fitness();
        XmlPullParserFactory pullParserFactory = null;
        XmlPullParser parser = null;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            parser = pullParserFactory.newPullParser();
            InputStream in_s = openFileInput(file);
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, ns, "fitnesspladser");

            while(parser.next() != XmlPullParser.END_DOCUMENT){
                if(parser.getEventType() != XmlPullParser.START_TAG){
                    continue;
                }

                String name = parser.getName();
                if(name.equals("fitness")){
                    fitness = parseFitness(parser);
                }else{
                    service.skip(parser);
                }
                if(fitness.getLatitude().equals(latitudeToFind) && fitness.getLongitude().equals(longitudeToFind)){
                    return fitness;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fitness;
    }

    private Fitness parseFitness(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "fitness");

        Fitness currentFitness = new Fitness();
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();

            if(name.equals("navn")){
                currentFitness.setNavn(service.readText(parser));
            }else if(name.equals("beskrivelse")){
                currentFitness.setBeskrivelse(service.readText(parser));
            }else if(name.equals("praktisk")){
                currentFitness.setPraktisk(service.readText(parser));
            }else if(name.equals("latitude")){
                currentFitness.setLatitude(service.readText(parser));
            }else if(name.equals("longitude")){
                currentFitness.setLongitude(service.readText(parser));
            }else if(name.equals("billeder")){
                currentFitness = parseBilleder(parser, currentFitness);
            }else{
                service.skip(parser);
            }
        }
        return currentFitness;
    }

    private Fitness parseBilleder(XmlPullParser parser, Fitness currentFitness) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "billeder");
        while(parser.next() != XmlPullParser.END_TAG){
            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }
            String name = parser.getName();

            if(name.equals("billede")){
                if(currentFitness.getBillede1().equals("")){
                    currentFitness.setBillede1(service.readText(parser));
                }else if(currentFitness.getBillede2().equals("")){
                    currentFitness.setBillede2(service.readText(parser));
                }else if(currentFitness.getBillede3().equals("")){
                    currentFitness.setBillede3(service.readText(parser));
                }else if(currentFitness.getBillede4().equals("")){
                    currentFitness.setBillede4(service.readText(parser));
                }
            }
        }

        return currentFitness;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, TouchImageViewActivity.class);
        if(v.getId() == R.id.cardImage1){
            intent.putExtra("url",fitness.getBillede1());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage2){
            intent.putExtra("url",fitness.getBillede2());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage3){
            intent.putExtra("url",fitness.getBillede3());
            startActivity(intent);
        }else if(v.getId() == R.id.cardImage4){
            intent.putExtra("url",fitness.getBillede4());
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
