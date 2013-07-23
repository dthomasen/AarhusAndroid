package dk.dthomasen.aarhus.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.models.HundeskovRatings;
import dk.dthomasen.aarhus.models.ShelterRatings;


public class Service {
    private static Service instance = null;
    ShelterRatings shelterRatingSaved;
    HundeskovRatings hundeskovRatingSaved;

    protected Service() {
        // Exists only to defeat instantiation.
    }
    public static Service getInstance() {
        if(instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public org.w3c.dom.Document convertStringToW3cDocument(String src){
        org.w3c.dom.Document dest = null;

        DocumentBuilderFactory dbFactory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder parser;

        try {
            parser = dbFactory.newDocumentBuilder();
            dest = parser.parse(new ByteArrayInputStream(src.getBytes(Charset.forName("UTF-8"))));
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dest;
    }

    public String readText(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String result = "";
        if(parser.next()==XmlPullParser.TEXT){
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public void skip(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public Bitmap mapCodeToIcon(Context context, String code){
        switch (Integer.valueOf(code)){
            case 0:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.storm);
            case 1:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.storm);
            case 2:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.windy);
            case 3:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.thunderstorm);
            case 4:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.thunderstorm);
            case 5:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.rain_and_snow);
            case 6:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.rain_snow);
            case 7:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.rain_and_snow);
            case 8:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.drizzle);
            case 9:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.drizzle);
            case 10:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.rain);
            case 11:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.showers);
            case 12:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.showers);
            case 13:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.light_snow);
            case 14:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.light_snow);
            case 15:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.snow);
            case 16:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.snow);
            case 17:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.flurries);
            case 18:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.sleet);
            case 19:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.haze);
            case 20:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.fog);
            case 21:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.haze);
            case 22:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.smoke);
            case 23:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.windy);
            case 24:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.windy);
            case 25:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.snow_showers);
            case 26:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.cloudy);
            case 27:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.mostly_cloudy);
            case 28:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.mostly_cloudy);
            case 29:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.partly_cloudy);
            case 30:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.partly_cloudy);
            case 31:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.clear);
            case 32:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny);
            case 33:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.clear);
            case 34:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.partly_cloudy);
            case 35:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.rain_snow);
            case 36:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.sunny);
            case 37:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.scattered_thunderstorms);
            case 38:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.scattered_thunderstorms);
            case 39:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.scattered_thunderstorms);
            case 40:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.scattered_showers);
            case 41:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.snow);
            case 42:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.snow_showers);
            case 43:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.snow);
            case 44:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.cloudy);
            case 45:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.thunderstorm);
            case 46:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.snow_showers);
            case 47:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.scattered_thunderstorms);
            default:
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.na);
        }
    }

    public int mapShelterNameToId(String name){
        if(name.equalsIgnoreCase("Shelter i Lisbjerg Gammel Skov")){
            return 1;
        }else if(name.equalsIgnoreCase("Shelterplads i Lisbjerg Ny Skov - Fem stk.")){
            return 2;
        }else if(name.equalsIgnoreCase("Shelterplads ved Egå Engsø - Fem stk.")){
            return 3;
        }else if(name.equalsIgnoreCase("Shelter i Mollerup Skov")){
            return 4;
        }else if(name.equalsIgnoreCase("Shelter på Vestereng")){
            return 5;
        }else if(name.equalsIgnoreCase("Shelter i Brendstrup Skov")){
            return 6;
        }else if(name.equalsIgnoreCase("Madpakkehus Delta i Skjoldhøjkilen")){
            return 7;
        }else if(name.equalsIgnoreCase("Madpakkehus Gamma i Skjoldhøjkilen")){
            return 8;
        }else if(name.equalsIgnoreCase("Madpakkehus Alfa i Skjoldhøjkilen")){
            return 9;
        }else if(name.equalsIgnoreCase("Shelter i Gjellerup Skov")){
            return 10;
        }else if(name.equalsIgnoreCase("Shelter i Hørhaven i skoven")){
            return 11;
        }else if(name.equalsIgnoreCase("Dagshelter i Hørhaven")){
            return 12;
        }else if(name.equalsIgnoreCase("Shelter i Hørhaven på Bakken")){
            return 13;
        }else if(name.equalsIgnoreCase("Shelter ved Moesgård Strand")){
            return 14;
        }else if(name.equalsIgnoreCase("Shelter i Vilhelmsborg Skov")){
            return 15;
        }else{
            return 0;
        }
    }

    public int mapHundeskovNameToId(String name){
        if(name.equalsIgnoreCase("Skødstrup skov")){
            return 1;
        }else if(name.equalsIgnoreCase("Lystrup Skov")){
            return 2;
        }else if(name.equalsIgnoreCase("Mollerup Skov")){
            return 3;
        }else if(name.equalsIgnoreCase("Vestereng")){
            return 4;
        }else if(name.equalsIgnoreCase("Riis Skov")){
            return 5;
        }else if(name.equalsIgnoreCase("Årslev Skov")){
            return 6;
        }else if(name.equalsIgnoreCase("Havreballe Skov")){
            return 7;
        }else if(name.equalsIgnoreCase("Tranbjerg Grønløkkepark")){
            return 8;
        }else if(name.equalsIgnoreCase("Moesgård Skov")){
            return 9;
        }else{
            return 0;
        }
    }

    public String convert12To24Hour(String amhour){
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        try {
            Date date = parseFormat.parse(amhour);
            return displayFormat.format(date);
        } catch (ParseException e) {
            return "";
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public float getAverageShelterRating(ShelterRatings ratings){
        int i;
        float sum = 0;
        for(i = 0; i<ratings.getShelterRatings().length; i++){
            sum += Float.parseFloat(ratings.getShelterRatings()[i].getRating());
        }
        return sum/i;
    }

    public float getAverageHundeskovRating(HundeskovRatings ratings){
        int i;
        float sum = 0;
        for(i = 0; i<ratings.getHundeskoveRatings().length; i++){
            sum += Float.parseFloat(ratings.getHundeskoveRatings()[i].getRating());
        }
        return sum/i;
    }

    public void saveShelterRatings(ShelterRatings shelterRatingSaved){
        this.shelterRatingSaved = shelterRatingSaved;
    }

    public void saveHundeskovRatings(HundeskovRatings hundeskovRatingSaved){
        this.hundeskovRatingSaved = hundeskovRatingSaved;
    }

    public ShelterRatings getSavedShelterRatings(){
        return shelterRatingSaved;
    }

    public HundeskovRatings getSavedHundeskovRatings(){
        return hundeskovRatingSaved;
    }
}
