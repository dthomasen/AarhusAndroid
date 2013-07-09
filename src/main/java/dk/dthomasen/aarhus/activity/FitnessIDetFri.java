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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import dk.dthomasen.aarhus.R;

public class FitnessIDetFri extends Activity implements LocationListener, LocationSource, GoogleMap.OnInfoWindowClickListener {

    protected final String TAG = this.getClass().getName();
    private SlidingMenu slidingMenu;
    private GoogleMap map;
    private OnLocationChangedListener mListener;
    private LocationManager locationManager;

    /**LOCATIONS**/
    LatLng riisSkov = new LatLng(56.170016, 10.221158);
    LatLng tangKrogen = new LatLng(56.13883, 10.210955);
    LatLng mindeParken = new LatLng(56.129052, 10.203855);
    LatLng skjoldhoejkilen = new LatLng(56.167978, 10.131504);
    LatLng frederiksbjergBypark = new LatLng(56.147309, 10.190007);
    LatLng gaasehaven = new LatLng(56.111279, 10.227528);
    LatLng harlevBypark = new LatLng(56.14564, 9.990274);
    LatLng brabrandstien = new LatLng(56.148723, 10.117077);
    LatLng egelund = new LatLng(56.04813, 10.197042);
    LatLng lyseng = new LatLng(56.112574, 10.193219);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitnessidetfri);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);

        ActionBar ab = getActionBar();

        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle("Fitnesspladser");
        ab.setSubtitle("Alle udendørs fitnesspladser i Aarhus");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locationManager != null)
        {
            boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(gpsIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
                Toast.makeText(this, "Finder din placering via GPS \nVent venligst...", Toast.LENGTH_LONG).show();
            }
            else if(networkIsEnabled)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
                Toast.makeText(this, "Finder din placering via netværks udbyder. \nVent venligst...", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this, "Aktiver din GPS eller netværks placering", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            //Show some generic error dialog because something must have gone wrong with location manager.
        }
        setUpMapIfNeeded();
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
        if(locationManager != null)
        {
            locationManager.removeUpdates(this);
        }
        overridePendingTransition(0, 0);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        setUpMapIfNeeded();

        if(locationManager != null)
        {
            map.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if( mListener != null )
        {
            mListener.onLocationChanged( location );

            LatLngBounds bounds = this.map.getProjection().getVisibleRegion().latLngBounds;

            if(!bounds.contains(new LatLng(location.getLatitude(), location.getLongitude())))
            {
                //Move the camera to the user's location once it's available!
                map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        }
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

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null)
        {
            // Try to obtain the map from the SupportMapFragment.
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.

            if (map != null)
            {
                setUpMap();
            }

            //This is how you register the LocationSource
            map.setLocationSource(this);
        }
    }

    private void setUpMap()
    {
        addTrainingMarkers();
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.170016, 10.201158), 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        map.setOnInfoWindowClickListener(this);
    }

    private void addTrainingMarkers(){
        map.addMarker(new MarkerOptions().position(riisSkov)
                .title("Riis Skov")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(tangKrogen)
                .title("Tangkrogen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(mindeParken)
                .title("Mindeparken")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(skjoldhoejkilen)
                .title("Skjoldhøjkilen")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(frederiksbjergBypark)
                .title("Frederiksbjerg Bypark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(gaasehaven)
                .title("Fitnessplads Gåsehaven")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(harlevBypark)
                .title("Harlev Bypark")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(brabrandstien)
                .title("Brabrandstien")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(egelund)
                .title("Egelund Idrætsanlæg")
                .snippet("Tryk for rutevejledning"));
        map.addMarker(new MarkerOptions().position(lyseng)
                .title("Lyseng Idrætsanlæg")
                .snippet("Tryk for rutevejledning"));

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Location userlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Intent intent = new Intent();
        Double longitude = 0.0;
        Double latitude = 0.0;

        switch (Integer.valueOf(marker.getId().replace("m",""))){
            case 0:
                longitude = riisSkov.longitude;
                latitude = riisSkov.latitude;
                break;
            case 1:
                longitude = tangKrogen.longitude;
                latitude = tangKrogen.latitude;
                break;
            case 2:
                longitude = mindeParken.longitude;
                latitude = mindeParken.latitude;
                break;
            case 3:
                longitude = skjoldhoejkilen.longitude;
                latitude = skjoldhoejkilen.latitude;
                break;
            case 4:
                longitude = frederiksbjergBypark.longitude;
                latitude = frederiksbjergBypark.latitude;
                break;
            case 5:
                longitude = gaasehaven.longitude;
                latitude = gaasehaven.latitude;
                break;
            case 6:
                longitude = harlevBypark.longitude;
                latitude = harlevBypark.latitude;
                break;
            case 7:
                longitude = brabrandstien.longitude;
                latitude = brabrandstien.latitude;
                break;
            case 8:
                longitude = egelund.longitude;
                latitude = egelund.latitude;
                break;
            case 9:
                longitude = lyseng.longitude;
                latitude = lyseng.latitude;
                break;
        }
        try{
            intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=" + userlocation.getLatitude() + "," + userlocation.getLongitude() + "&daddr=" + latitude + "," + longitude + "&dirflg=w"));
            startActivity(intent);
        }catch(NullPointerException e){
            Toast.makeText(this, "Tænd for GPS eller Placeringsdeling for at benytte rutevejledning", Toast.LENGTH_LONG).show();
        }
    }
}

