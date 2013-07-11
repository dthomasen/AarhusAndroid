package dk.dthomasen.aarhus.weather;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import dk.dthomasen.aarhus.service.Service;

/**
 * Created by Dennis on 10-07-13.
 */
public class WeatherDownload extends AsyncTask<Void, Void, Weather>{

    @Override
    protected Weather doInBackground(Void... params) {
        try {
            String weatherString = QueryYahooWeather();
            Document weatherDoc = Service.getInstance().convertStringToW3cDocument(weatherString);
            Weather weatherResult = parseWeather(weatherDoc);
            return weatherResult;
        } catch (Throwable t) {
            Log.e("AsyncTask", "OMGCrash", t);
            // maybe throw it again
            throw new RuntimeException(t);
        }

    }

    private Weather parseWeather(Document srcDoc) throws IOException {

        Weather myWeather = new Weather();

        myWeather.description = srcDoc.getElementsByTagName("description")
                .item(0)
                .getTextContent();

        Node locationNode = srcDoc.getElementsByTagName("yweather:location").item(0);
        myWeather.city = locationNode.getAttributes()
                .getNamedItem("city")
                .getNodeValue()
                .toString();
        myWeather.region = locationNode.getAttributes()
                .getNamedItem("region")
                .getNodeValue()
                .toString();
        myWeather.country = locationNode.getAttributes()
                .getNamedItem("country")
                .getNodeValue()
                .toString();

        Node windNode = srcDoc.getElementsByTagName("yweather:wind").item(0);
        myWeather.windChill = windNode.getAttributes()
                .getNamedItem("chill")
                .getNodeValue()
                .toString();
        myWeather.windDirection = windNode.getAttributes()
                .getNamedItem("direction")
                .getNodeValue()
                .toString();
        myWeather.windSpeed = windNode.getAttributes()
                .getNamedItem("speed")
                .getNodeValue()
                .toString();

        Node astronomyNode = srcDoc.getElementsByTagName("yweather:astronomy").item(0);
        myWeather.sunrise = astronomyNode.getAttributes()
                .getNamedItem("sunrise")
                .getNodeValue()
                .toString();
        myWeather.sunset = astronomyNode.getAttributes()
                .getNamedItem("sunset")
                .getNodeValue()
                .toString();

        Node nowNode = srcDoc.getElementsByTagName("yweather:condition").item(0);
        myWeather.nowText = nowNode.getAttributes()
                .getNamedItem("text")
                .getNodeValue()
                .toString();
        myWeather.nowTemp = nowNode.getAttributes()
                .getNamedItem("temp")
                .getNodeValue()
                .toString();
        myWeather.nowDay = nowNode.getAttributes()
                .getNamedItem("date")
                .getNodeValue()
                .toString();
        myWeather.nowCode = nowNode.getAttributes()
                .getNamedItem("code")
                .getNodeValue()
                .toString();

        Node forecastNode0 = srcDoc.getElementsByTagName("yweather:forecast").item(0);
        myWeather.todayText = forecastNode0.getAttributes()
                .getNamedItem("text")
                .getNodeValue()
                .toString();
        myWeather.todayTemp = forecastNode0.getAttributes()
                .getNamedItem("high")
                .getNodeValue()
                .toString();
        myWeather.todayDay = mapToDanishDay(forecastNode0.getAttributes()
                .getNamedItem("day")
                .getNodeValue()
                .toString());
        myWeather.todayCode = forecastNode0.getAttributes()
                .getNamedItem("code")
                .getNodeValue()
                .toString();


        Node forecastNode1 = srcDoc.getElementsByTagName("yweather:forecast").item(1);
        myWeather.tommorowText = forecastNode1.getAttributes()
                .getNamedItem("text")
                .getNodeValue()
                .toString();
        myWeather.tommorowTemp = forecastNode1.getAttributes()
                .getNamedItem("high")
                .getNodeValue()
                .toString();
        myWeather.tommorowDay = mapToDanishDay(forecastNode1.getAttributes()
                .getNamedItem("day")
                .getNodeValue()
                .toString());
        myWeather.tommorowCode = forecastNode1.getAttributes()
                .getNamedItem("code")
                .getNodeValue()
                .toString();

        Node forecastNode2 = srcDoc.getElementsByTagName("yweather:forecast").item(2);
        myWeather.tTommorowText = forecastNode2.getAttributes()
                .getNamedItem("text")
                .getNodeValue()
                .toString();
        myWeather.tTommorowTemp = forecastNode2.getAttributes()
                .getNamedItem("high")
                .getNodeValue()
                .toString();
        myWeather.tTommorowDay = mapToDanishDay(forecastNode2.getAttributes()
                .getNamedItem("day")
                .getNodeValue()
                .toString());
        myWeather.tTommorowCode = forecastNode2.getAttributes()
                .getNamedItem("code")
                .getNodeValue()
                .toString();
        return myWeather;
    }

    private String QueryYahooWeather(){
        // use the api to get WOEID which i used directly in this demo
        //http://where.yahooapis.com/geocode?q=bangalore=%5Byourappidhere%5D
        String qResult = "";
        String queryString = "http://weather.yahooapis.com/forecastrss?w=552015&u=c";

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(queryString);

        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

            if (httpEntity != null){
                InputStream inputStream = httpEntity.getContent();
                Reader in = new InputStreamReader(inputStream);
                BufferedReader bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();

                String stringReadLine = null;

                while ((stringReadLine = bufferedreader.readLine()) != null) {
                    stringBuilder.append(stringReadLine + "\n");
                }

                qResult = stringBuilder.toString();
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return qResult;
    }

    private String mapToDanishDay(String day){
        if(day.equals("Mon")){
            return "Mandag";
        }else if(day.equals("Tue")){
            return "Tirsdag";
        }else if(day.equals("Wed")){
            return "Onsdag";
        }else if(day.equals("Thu")){
            return "Torsdag";
        }else if(day.equals("Fri")){
            return "Fredag";
        }else if(day.equals("Sat")){
            return "Lørdag";
        }else if(day.equals("Sun")){
            return "Søndag";
        }
        return "";
    }
}
