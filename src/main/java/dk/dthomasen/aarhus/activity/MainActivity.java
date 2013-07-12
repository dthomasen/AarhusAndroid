package dk.dthomasen.aarhus.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import java.util.concurrent.ExecutionException;

import dk.dthomasen.aarhus.R;
import dk.dthomasen.aarhus.service.XmlDownload;
import dk.dthomasen.aarhus.weather.Weather;
import dk.dthomasen.aarhus.weather.WeatherDownload;

public class MainActivity extends Activity{
    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;

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

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Friluft Aarhus");
        ab.setSubtitle("App'en om friluftsliv i Aarhus");

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            Weather weather = new WeatherDownload().execute().get();
            ((ImageView)findViewById(R.id.nowIcon)).setImageBitmap(mapCodeToIcon(weather.getNowCode()));
            ((TextView)findViewById(R.id.nowTemp)).setText("Temperatur: "+weather.getNowTemp()+ "°C");
            ((TextView)findViewById(R.id.nowWind)).setText("Vind: "+weather.getWindSpeed()+" km/t");
            ((TextView)findViewById(R.id.sunRise)).setText("Solopgang: "+weather.getSunrise());
            ((TextView)findViewById(R.id.sunDown)).setText("Solnedgang: "+weather.getSunset());
            ((TextView)findViewById(R.id.day1)).setText(weather.getTodayDay());
            ((TextView)findViewById(R.id.temp1)).setText(weather.getTodayTemp() + "°C");
            ((ImageView)findViewById(R.id.weathericon1)).setImageBitmap(mapCodeToIcon(weather.getTodayCode()));
            ((TextView)findViewById(R.id.day2)).setText(weather.getTommorowDay());
            ((TextView)findViewById(R.id.temp2)).setText(weather.getTommorowTemp()+"°C");
            ((ImageView)findViewById(R.id.weathericon2)).setImageBitmap(mapCodeToIcon(weather.getTommorowCode()));
            ((TextView)findViewById(R.id.day3)).setText(weather.gettTommorowDay());
            ((TextView)findViewById(R.id.temp3)).setText(weather.gettTommorowTemp()+"°C");
            ((ImageView)findViewById(R.id.weathericon3)).setImageBitmap(mapCodeToIcon(weather.gettTommorowCode()));
            new XmlDownload().execute(this).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
        }else if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.quit)
                    .setMessage(R.string.really_quit)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Stop the activity
                            MainActivity.this.finish();
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();

            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
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

    private Bitmap mapCodeToIcon(String code){
        switch (Integer.valueOf(code)){
            case 0:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.storm);
            case 1:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.storm);
            case 2:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.windy);
            case 3:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.thunderstorm);
            case 4:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.thunderstorm);
            case 5:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.rain_and_snow);
            case 6:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.rain_snow);
            case 7:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.rain_and_snow);
            case 8:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.drizzle);
            case 9:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.drizzle);
            case 10:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.rain);
            case 11:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.showers);
            case 12:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.showers);
            case 13:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.light_snow);
            case 14:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.light_snow);
            case 15:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.snow);
            case 16:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.snow);
            case 17:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.flurries);
            case 18:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.sleet);
            case 19:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.haze);
            case 20:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.fog);
            case 21:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.haze);
            case 22:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.smoke);
            case 23:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.windy);
            case 24:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.windy);
            case 25:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.snow_showers);
            case 26:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.cloudy);
            case 27:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.mostly_cloudy);
            case 28:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.mostly_cloudy);
            case 29:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.partly_cloudy);
            case 30:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.partly_cloudy);
            case 31:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.clear);
            case 32:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.sunny);
            case 33:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.clear);
            case 34:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.partly_cloudy);
            case 35:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.rain_snow);
            case 36:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.sunny);
            case 37:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.scattered_thunderstorms);
            case 38:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.scattered_thunderstorms);
            case 39:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.scattered_thunderstorms);
            case 40:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.scattered_showers);
            case 41:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.snow);
            case 42:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.snow_showers);
            case 43:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.snow);
            case 44:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.cloudy);
            case 45:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.thunderstorm);
            case 46:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.snow_showers);
            case 47:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.scattered_thunderstorms);
            default:
                return BitmapFactory.decodeResource(this.getResources(), R.drawable.na);
        }
    }
}
