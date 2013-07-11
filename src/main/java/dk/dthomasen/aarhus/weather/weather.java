package dk.dthomasen.aarhus.weather;

import android.graphics.Bitmap;

/**
 * Created by Dennis on 10-07-13.
 */
public class Weather {
    String description;
    String city;
    String region;
    String country;
    String windChill;
    String windDirection;
    String windSpeed;

    String sunrise;
    String sunset;

    String nowText;
    String nowTemp;
    String nowDay;
    String nowCode;

    String todayText;
    String todayTemp;
    String todayDay;
    String todayCode;

    String tommorowText;
    String tommorowTemp;
    String tommorowDay;
    String tommorowCode;

    String tTommorowText;
    String tTommorowTemp;
    String tTommorowDay;
    String tTommorowCode;

    public String getNowCode() {
        return nowCode;
    }

    public String getNowTemp() {
        return nowTemp;
    }

    public String getNowText() {
        return nowText;
    }

    public String getNowDay() {
        return nowDay;
    }

    public String gettTommorowText() {
        return tTommorowText;
    }

    public String gettTommorowTemp() {
        return tTommorowTemp;
    }

    public String gettTommorowDay() {
        return tTommorowDay;
    }

    public String getTommorowText() {
        return tommorowText;
    }

    public String getTommorowTemp() {
        return tommorowTemp;
    }

    public String getTommorowDay() {
        return tommorowDay;
    }

    public String getDescription() {
        return description;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getWindChill() {
        return windChill;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getTodayText() {
        return todayText;
    }

    public String getTodayTemp() {
        return todayTemp;
    }

    public String getTodayDay() {
        return todayDay;
    }

    public String getTodayCode() {
        return todayCode;
    }

    public String getTommorowCode() {
        return tommorowCode;
    }

    public String gettTommorowCode() {
        return tTommorowCode;
    }
}